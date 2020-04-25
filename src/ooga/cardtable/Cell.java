package ooga.cardtable;

import java.util.ArrayList;
import java.util.Collections;
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
  }

  public static ICell fromStorageString(String input) { //fixme add to API?
    String nm = input.split("\\$")[0];
    IDeck d = Deck.fromStorageString(input.split("\\$")[1]);
    Cell ret = new Cell(nm, d);
    String brace = getFirstBraces(input);
    while (brace != null) {
      input = input.replaceFirst(Pattern.quote(brace), "");
      brace = brace.substring(1, brace.length() - 1);
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
    int start = 0;
    while (input.charAt(start) != '{') {
      start++;
      if (start >= input.length()) {
        return null;
      }
    }
    int end = start;
    int counter = 1;
    while (counter > 0) {
      end++;
      if (end >= input.length()) {
        return null;
      }
      char c = input.charAt(end);
      counter += c == '{' ? 1 : 0;
      counter += c == '}' ? -1 : 0;
    }
    return input.substring(start, end + 1);
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
      ICell toAdd = cellDeckBuilder.apply(mainDeck);
      this.children = toAdd.getAllChildren();
      this.deck = toAdd.getDeck();
      updateParentage();
    }
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
        total += e.getValue().getTotalSize(visited);
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
      if (!e.getKey().equals(Offset.NONE) && !e.getValue().isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Map<IOffset, ICell> getAllChildren() {
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
    children.putIfAbsent(offset, new Cell(getName() + "," + offset.getOffset()));
    children.get(offset).addCard(Offset.NONE, card);
  }

  @Override
  public void addCell(IOffset offset, ICell cell) { //fixme 90% this infinite recurses
    if (cell == null || cell.isEmpty()) {
      return;
    }
    ICell recipient = this;
    if (offset != Offset.NONE) {
      recipient = getAllChildren().get(offset);
    }
    if (recipient == null) {
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
      } else {
        tempRec.addCell(Offset.NONE, e.getValue());
      }
    }
    updateParentage();
  }

  @Override
  public void updateParentage() {
    String masterName;
    if (parent == null) {
      masterName = getName();
    } else {
      masterName = parent.getName() + "," + getOffsetFromParent().getOffset();
    }
    name = masterName;
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE && e.getValue() != null) {
        Cell c = (Cell) e.getValue();
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
      if (!tracker.contains(e.getValue())) {
        tracker.add(e.getValue());
        e.getValue().getAllCellsHelper(tracker);
      }
    }
  }

  @Override
  public ICell getPeak(IOffset offset) {
    if (offset.equals(Offset.NONE)) {
      return this;
    }
    ICell temp = this;
    while (temp.getAllChildren().containsKey(offset)) {
      temp = temp.getAllChildren().get(offset);
    }
    return temp;
  }

  @Override
  public boolean equals(Object other) { //fixme should this check name? not yet for tests for cards
    if (other == this) {
      return true;
    }
    if (other == null) {
      return false;
    }
    Cell c = (Cell) other;
    if (!deck.equals(c.deck)) {
      return false;
    }
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE && !e.getValue().equals(c.getAllChildren().get(e.getKey()))) {
        return false;
      }
    }
    for (Entry<IOffset, ICell> e : c.getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE && !e.getValue().equals(getAllChildren().get(e.getKey()))) {
        return false;
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
    ICell currentCell = this;
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
    return leafMap.get(Collections.max(leafMap.keySet()));
  }

  @Override
  public ICell copy() {
    IDeck d = getDeck().copy();
    ICell ret = new Cell(name, d);
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE) {
        ret.setCellAtOffset(e.getKey(), e.getValue().copy());
      }
    }
    return ret;
  }

  @Override
  public ICell extractCards(Function<ICell, ICard> cardGetter) {
    ICell ret = new Cell(name);
    ret.getDeck().addCard(cardGetter.apply(this));
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE) {
        ret.setCellAtOffset(e.getKey(), e.getValue().extractCards(cardGetter));
      }
    }
    return ret;
  }

  @Override
  public ICell extractDecks(Function<ICell, IDeck> deckGetter) {
    ICell ret = new Cell(name);
    ret.getDeck().addDeck(deckGetter.apply(this));
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE) {
        ret.setCellAtOffset(e.getKey(), e.getValue().extractDecks(deckGetter));
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
      if (e.getKey() != Offset.NONE && e.getValue().getName().toLowerCase().contains(nm)) {
        ICell ret = e.getValue().findNamedCell(nm);
        if (ret != null) {
          return ret;
        }
      }
    }
    return null;
  }

  @Override
  public ICell followNamespace(String nm) {
    String[] names = nm.split(",");
    if (names.length == 0 || names[0].equals("")) {
      return this;
    }
    ICell next = getAllChildren().get(Offset.valueOf(names[0].toUpperCase()));
    if (next == null) {
      ICell ret = this;
      for (int i = 1; i < names.length; i++) {
        setCellAtOffset(Offset.valueOf(names[i].toUpperCase()),
            new Cell(getName() + "," + names[i]));
        ret = ret.getAllChildren().get(Offset.valueOf(names[i].toUpperCase()));
      }
      return ret;
    }
    String[] restNames = new String[names.length - 1];
    System.arraycopy(names, 1, restNames, 0, names.length - 1);
    return next.followNamespace(String.join(",", restNames));
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
    StringBuilder ret = new StringBuilder(getName() + "$");
    ret.append(getDeck().toStorageString()).append("$");
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE) {
        ret.append("{").append(e.getKey().getOffset())
            .append(":").append(e.getValue().toStorageString() + "}");
      }
    }
    return ret.toString();
  }
}
