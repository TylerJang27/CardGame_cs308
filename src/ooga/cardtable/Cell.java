package ooga.cardtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

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

  @Override
  public List<ICell> getCellsbyName(String name) {
    List<ICell> cellList = new ArrayList<>();
    if (isInGroup(name)) {
      cellList.add(this);
    }
    return cellList;
  }

  @Override
  public boolean isInGroup(String name) {
    return this.name.equals(name);
    //TODO: DOUBLE CHECK HAPPY WITH THIS IMPLEMENTATION
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
      addCell(Offset.NONE, toAdd);
    }
    cellDeckBuilder = null;
  }

  @Override
  public IDeck getDeck() {
    for(ICell i : children.values()){
      //System.out.println("card" + i.getDeck().peek());
    }
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
    for (Entry<IOffset, ICell> e: getAllChildren().entrySet()) {
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

  @Override
  public IOffset getOffsetFromParent() {
    for (Entry<IOffset, ICell> e : parent.getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE && e.getValue() == this) {
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
    return children.remove(offset);
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
      recipient = this;
    }
    if (recipient == null) {
      System.out.println(this.getName() + "yolo");
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
        System.out.println("yeet" + e.getKey() + e.getValue() + "yeet");
        tempRec.addCell(Offset.NONE, e.getValue());
      }
    }
    updateParentage();
  }

  @Override
  public void updateParentage() {
    String masterName = "";
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
        this.setCellAtOffset(e.getKey(), c); //added by Tyler, didn't seem to do much
        c.setParent(this);
        c.updateParentage(); //fixme monster
      }
    }
  }

  private void setParent(ICell cell) {
    parent = cell;
  }

  @Override
  public void setCellAtOffset(IOffset offset, ICell cell) {
    if (cell == null) {
      children.remove(offset);
      return;
    }
    children.put(offset, cell);
    System.out.println(offset.getOffset() + "yolo" + cell.getName());
    ((Cell) cell).setParent(this); //fixme you're a monster
    cell.updateParentage();
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
      System.out.println("equals?" + !tracker.contains(e.getValue()));
      System.out.println(e.getValue());
      if (!tracker.contains(e.getValue())) {
        tracker.add(e.getValue());
        System.out.println("recurse");
        e.getValue().getAllCellsHelper(tracker);
      }//TODO: MAKE SURE THIS DOESN'T INFINITE RECURSE
    }
  }

  @Override
  public ICell getPeak(IOffset offset) {
    ICell temp = this;
    while (temp.getDeck().size() > 0) { //TODO: VERIFY THIS DOESN'T SKIP EMPTIES
      temp = getAllChildren().get(offset);
    }
    return temp;
  }

  //TODO: MAY EVENTUALLY NEED THE FOLLOWING:
  //  SHUFFLE A CELL AND ITS COMPATRIOTS
  //

  @Override
  public boolean equals(Object other) { //fixme should this check name? not yet for tests for cards
    if (!(other instanceof Cell)) {
      return false;
    }
    Cell c = (Cell) other;
    if (!deck.equals(c.deck)) {
      return false;
    }
    for (Entry<IOffset, ICell> e : getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE) {
        if (!e.getValue().equals(c.getAllChildren().get(e.getKey()))) {
          return false;
        }
      }
    }
    for (Entry<IOffset, ICell> e : c.getAllChildren().entrySet()) {
      if (e.getKey() != Offset.NONE) {
        if (!e.getValue().equals(getAllChildren().get(e.getKey()))) {
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
    while(currentCell.getParent() != null) {
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

  private Integer findMax(Iterable<Integer> iter) {
    Integer Max = Integer.MIN_VALUE;
    for (Integer k: iter) {
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
    for (int k = 0; k < Offset.values().length; k ++) {
      IOffset offset = Offset.values()[k];
      ICell offsetCell = curr.getAllChildren().get(offset);

      if (offsetCell != null && !offset.getOffset().equals(Offset.NONE.getOffset())) {
        findLeafHelper(offsetCell, steps + 1, tracker);
      }
    }
  }

}
