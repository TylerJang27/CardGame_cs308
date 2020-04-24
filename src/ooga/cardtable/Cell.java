package ooga.cardtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Cell implements ICell {

  private IDeck deck;
  private ICell parent;
  private String name;
  private Function<IDeck, ICell> cellDeckBuilder;
  private Map<IOffset, ICell> children;

  public Cell(String nm) {
    name = nm;
    deck = new Deck();
    children = new HashMap<>();
  }

  public Cell(String nm, IDeck d) {
    this(nm);
    deck = d;
    //System.out.println(d.size() + "how big is it " + nm);
  }

  @Override
  public boolean isFixed() {
    return deck.isFixed();
  }

  /**
   * Return all the ICells represented by the name for either a group or an individual cell.
   *
   * @param name the query
   * @return a List of ICells matching the name
   */
  @Override
  public List<ICell> getCellsbyName(String name) {
    List<ICell> cellList = new ArrayList<>();
    if (isInGroup(name)) {
      cellList.add(this);
    }
    return cellList;
  }

  /**
   * Returns whether or not the name is contained by getCellsByName() for this name.
   *
   * @param name the query
   * @return whether or not the name is relevant
   */
  @Override
  public boolean isInGroup(String name) {
    return this.name.equals(name);
  }

  @Override
  public void setDraw(Function<IDeck, ICell> initializer) {
    cellDeckBuilder = initializer;
  }

  @Override
  public void initializeCards(IDeck mainDeck) {
    if (cellDeckBuilder != null) {
      //TODO: DOUBLE CHECK THIS WORKS
      ICell toAdd = cellDeckBuilder.apply(mainDeck);
      this.children = toAdd.getAllChildren();
      this.deck = toAdd.getDeck();
      updateParentage();
      //addCell(Offset.NONE, toAdd);
    }
    //cellDeckBuilder = null;
  }

  @Override
  public IDeck getDeck() {
    return deck;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getTotalSize() {
    return getTotalSize(new ArrayList<>());
  }

  @Override
  public int getTotalSize(List<ICell> visited) {
    int total = 0;
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (!visited.contains(e.getValue())) {
        visited.add(e.getValue());
        total += e.getValue().getDeck().size();
        total += e.getValue().getTotalSize(visited); //TODO: MAKE SURE THIS DOESN'T INFINITE RECURSE
      }
    }
    return total;
  }

  @Override
  public ICell getParent() {
    return parent;
  }

  private void setParent(ICell cell) {
    parent = cell;
  }

  @Override
  public IOffset getOffsetFromParent() {
    if (parent == null) {
      return Offset.NONE;
    }
    for (Entry<IOffset, ICell> e : parent.getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE && e.getValue().equals(this)) {
        return e.getKey();
      }
    }
    throw new RuntimeException("parent doesn't own child, something is terrible"); //fixme
  }

  @Override
  public boolean hasOffsetChildren() {
    return !children.isEmpty();
  }

  @Override
  public ICell removeCellAtOffset(IOffset offset) {
    if (children.containsKey(offset)) {
      Cell ret = (Cell) children.remove(offset); //fixme monster
      ret.parent = null;
      return ret;
    }
    return null;
  }

  @Override
  public boolean isEmpty() {
    if (getDeck().size() > 0) {
      return false;
    }
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (!e.getKey().equals(Offset.NONE)) {
        if (!e.getValue().isEmpty()) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public Map<IOffset, ICell> getAllChildren() { //fixme do a proper copy?
    Map<IOffset, ICell> ret = new HashMap<>(children);
    ret.put(Offset.NONE, new Cell(name, deck));
    return ret;
  }

  @Override
  public Map<IOffset, ICell> getHeldCells() {
    return getAllChildren();
  }

  @Override
  public void addCard(IOffset offset, ICard card) {
    if (offset.equals(Offset.NONE)) {
      deck.addCard(card);
      return;
    }
    children.putIfAbsent(offset,
        new Cell(getName() + "," + offset.getOffset())); //fixme namespace shenanigans
    children.get(offset).addCard(Offset.NONE, card);
  }

  /*@Override
  public void addCell(IOffset offset, ICell addition) {
    ICell recipient = this;
    Map<IOffset, ICell> recChildren = recipient.getAllChildren();
    if (recChildren.containsKey(offset)) {

    } else {
      recipient.setCellAtOffset(offset, addition);
    }



    for (int k = 0; k < Offset.values().length; k ++) {
      IOffset off = Offset.values()[k];
      Map<IOffset, ICell> recChildren = recipient.getAllChildren();
      Map<IOffset, ICell> addChildren = addition.getAllChildren();
      if (off.equals(Offset.NONE)) {
        recipient.getDeck().addDeck(addition.getDeck());
        continue;
      }
      if (recChildren.containsKey(off)) {
        if (addChildren.containsKey(off)) {
          recChildren.get(off).addCell(Offset.NONE, addChildren.get(off));
          continue;
        }

        continue;
      }

      if (addition.getAllChildren().keySet().size() <= 1) {
        recipient.getDeck().addDeck(addition.getDeck());
        updateParentage();
        return;
      }
    }
    updateParentage();
  }*/
  @Override
  public void addCell(IOffset offset, ICell cell) { //fixme 90% this infinite recurses
    //TODO: ADD NAMES OR EVERYTHING BREAKS
    if (cell == null || cell.isEmpty()) {
      return;
    }
    ICell recipient = null;
    if (offset != Offset.NONE) {
      recipient = getAllChildren().get(offset);
    } else {
      recipient = this; //TODO: HEAD
    }
    if (recipient == null) {
      //System.out.println(this.getName() + "yolo");
      setCellAtOffset(offset, cell);
      updateParentage();
      return;
    }
    if (cell.getAllChildren().keySet().size() <= 1) {
      recipient.getDeck().addDeck(cell.getDeck());
      updateParentage();
      return;
    }
    for (Entry<IOffset, ICell> e : cell.getAllChildren().entrySet()) {
      ICell tempRec = recipient.getAllChildren().get(e.getKey());
      if (tempRec == null) {
        recipient.setCellAtOffset(e.getKey(), e.getValue());
        updateParentage();
      } else {
        //System.out.println("yeet" + e.getKey() + e.getValue() + "yeet");
        tempRec.addCell(Offset.NONE, e.getValue());
      }
    }
    updateParentage();
  }

  @Override
  public void updateParentage() {
    String masterName = "";
    //System.out.println("parent: " + parent);
    //System.out.println("this: " + this);
    if (parent == null) {
      masterName = getName();
    } else {
      masterName = parent.getName() + "," + getOffsetFromParent().getOffset();
    }
    name = masterName;

    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE && e.getValue() != null) {
        //System.out.println(this.getName());
        Cell c = (Cell) e.getValue();
        //this.setCellAtOffset(e.getKey(), c); //added by Tyler, didn't seem to do much
        c.setParent(this);
        c.updateParentage(); //fixme monster
      }
    }
  }

  private ICell extractCell(ICell cell) {
    ICell duplicate = new Cell("");
    for (int k = 0; k < Offset.values().length; k++) {
      IOffset off = Offset.values()[k];
      if (off.equals(Offset.NONE)) {
        duplicate.getDeck().addDeck(cell.getDeck());
        continue;
      }
      duplicate.setCellAtOffset(off, cell.getAllChildren().get(off));
    }
    return duplicate;
  }

  @Override
  public void setCellAtOffset(IOffset offset, ICell cell) {
    if (cell == null) {
      removeCellAtOffset(offset);
      return;
    }
    ICell extracted = extractCell(cell);
    children.put(offset, extracted);
    //System.out.println(offset.getOffset() + "yolo" + extracted.getName());
    ((Cell) extracted).setParent(this); //fixme you're a monster
    extracted.updateParentage();
  }

  @Override
  public List<ICell> getAllCells() {
    List<ICell> total = new ArrayList<>();
    getAllCellsHelper(total);
    return total;
  }

  @Override
  public void getAllCellsHelper(List<ICell> tracker) {
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      //System.out.println("equals?" + !tracker.contains(e.getValue()));
      //System.out.println(e.getValue());
      if (!tracker.contains(e.getValue())) {
        tracker.add(e.getValue());
        //System.out.println("recurse");
        e.getValue().getAllCellsHelper(tracker);
      }//TODO: MAKE SURE THIS DOESN'T INFINITE RECURSE
    }
  }

  @Override
  public ICell getPeak(IOffset offset) {
    if (offset.equals(Offset.NONE)) {
      return this;
    }
    ICell temp = this;
    while (temp.getAllChildren().keySet()
        .contains(offset)) { //TODO: VERIFY THIS DOESN'T SKIP EMPTIES
      temp = temp.getAllChildren().get(offset);
    }
    return temp;
  }

  //TODO: MAY EVENTUALLY NEED THE FOLLOWING:
  //  SHUFFLE A CELL AND ITS COMPATRIOTS
  //

  @Override
  public boolean equals(Object other) { //fixme should this check name? not yet for tests for cards
    /*if (!(other instanceof Cell)) {
      return false;
    }*/
    if (other == null) {
      return false;
    }
    Cell c = (Cell) other;
    if (!deck.equals(c.deck)) {
      return false;
    }
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE) {
        if (!e.getValue().equals(c.getAllChildren().get(e.getKey()))) {
          System.out.println(e);
          return false;
        }
      }
    }
    for (Entry<IOffset, ICell> e : c.getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE) {
        if (!e.getValue().equals(getAllChildren().get(e.getKey()))) {
          System.out.println("2 "+e);
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder ret = new StringBuilder(name + ": " + deck.toString() + "\n");
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE) {
        ret.append(e.getValue().toString());
      }
    }
    return ret.toString();
  }

  @Override
  public ICell findHead() {
    ICell newHead = this;
    ICell currentCell = newHead;
    while (currentCell.getParent() != null) {
      currentCell = currentCell.getParent();
    }
    return currentCell;
  }

  @Override
  public ICell findLeaf() {
    ICell newLeaf = this;
    Map<Integer, ICell> leafMap = new HashMap<>();
    findLeafHelper(newLeaf, 0, leafMap);
    return leafMap.get(findMax(leafMap.keySet()));
  }

  @Override
  public ICell copy() {
    return this.copy((ICard c)->true);
    /*IDeck d = getDeck().copy();
    ICell ret = new Cell(name, d);
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE) {
        ret.setCellAtOffset(e.getKey(), e.getValue().copy());
      }
    }
    return ret;*/
  }


  @Override
  public ICell copy(Function<ICard, Boolean> cardFunction) {
    IDeck d = getDeck().copy(cardFunction);
    ICell ret = new Cell(name, d);
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE) {
        ret.setCellAtOffset(e.getKey(), e.getValue().copy());
      }
    }
    return ret;
  }

  @Override
  public ICell extract(Function<ICell, ICard> cardGetter) {
    ICell ret = new Cell(name);
    //ret.getDeck().addDeck(new Deck());
    ret.getDeck().addCard(cardGetter.apply(this));
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE) {
        ret.setCellAtOffset(e.getKey(), e.getValue().extract(cardGetter));
      }
    }
    return ret;
  }

  @Override
  public ICell findNamedCell(String nm) {
    if (name.equals(nm)) {
      return this;
    }
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE) {
        if (e.getValue().getName().toLowerCase().contains(nm)) {
          ICell ret = e.getValue().findNamedCell(nm);
          if (ret != null) {
            return ret;
          }
        }
      }
    }
    return null;
  }

  @Override
  public ICell followNamespace(String nm) {
    String[] names = nm.split(",");
    //System.out.println("namespace: "+ Arrays.toString(names));
    if (names.length == 0 || names[0].equals("")) {
      //System.out.println("it's empty");
      return this;
    }
    ICell next = getAllChildren().get(Offset.valueOf(names[0].toUpperCase()));
    if (next == null) {
      //System.out.println("filling in the rest");
      ICell ret = this;
      for (int i = 1; i < names.length; i++) {
        setCellAtOffset(Offset.valueOf(names[i].toUpperCase()),
            new Cell(getName() + "," + names[i]));
        ret = ret.getAllChildren().get(Offset.valueOf(names[i].toUpperCase()));
      }
      return ret;
    }
    String[] restNames = new String[names.length - 1];
    for (int i = 1; i < names.length; i++) {
      restNames[i - 1] = names[i];
    }
    return next.followNamespace(String.join(",", restNames));
  }

  private Integer findMax(Iterable<Integer> iter) {
    Integer Max = Integer.MIN_VALUE;
    for (Integer k : iter) {
      if (Max.compareTo(k) < 0) {
        Max = k;
      }
    }
    return Max;
  }

  private void findLeafHelper(ICell curr, int steps, Map<Integer, ICell> tracker) {
    if (curr.getAllChildren().size() <= 1) {
      tracker.put(steps, curr);
      return;
    }
    for (int k = 0; k < Offset.values().length; k++) {
      IOffset offset = Offset.values()[k];
      ICell offsetCell = curr.getAllChildren().get(offset);

      if (offsetCell != null && !offset.equals(Offset.NONE)) {
        findLeafHelper(offsetCell, steps + 1, tracker);
      }
    }
  }

  @Override
  public String toStorageString() {
    String ret = getName() + "$";
    ret += getDeck().toStorageString() + "$";
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE) {
        ret += "{" + e.getKey().getOffset() + ":" + e.getValue().toStorageString() + "}";
      }
    }
    return ret;
  }

  public static ICell fromStorageString(String input) { //fixme add to API?
    String nm = input.split("\\$")[0];
    IDeck d = Deck.fromStorageString(input.split("\\$")[1]);
    Cell ret = new Cell(nm, d);
    String brace = getFirstBraces(input);
    while (brace != null) {
      input = input.replaceFirst(Pattern.quote(brace), "");
      brace = brace.substring(1, brace.length() - 1);
      //System.out.println("brace: "+brace);
      String offStr = brace.split(":")[0];
      Offset off = Offset.valueOf(offStr.toUpperCase());
      String nextBrace = brace.replaceFirst(Pattern.quote(offStr), "");
      ret.setCellAtOffset(off, fromStorageString(nextBrace));
      brace = getFirstBraces(input);
    }
    ret.updateParentage();
    return ret;
  }

  private static String getFirstBraces(String input) {
    //System.out.println("brace input: "+input);
    int start = 0;
    while (input.charAt(start) != '{') {
      if (++start >= input.length()) {
        //System.out.println("bad start");
        return null;
      }
    }
    int end = start;
    int counter = 1;
    while (counter > 0) {
      if (++end >= input.length()) {
        //System.out.println("bad end");
        return null;
      }
      char c = input.charAt(end);
      if (c == '{') {
        counter++;
      }
      if (c == '}') {
        counter--;
      }
      //System.out.println("counter: "+counter+" "+c);
    }
    //System.out.println("braces: "+input.substring(start, end+1));
    return input.substring(start, end+1);
  }

}
