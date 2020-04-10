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
      addCell(Offset.NONE, cellDeckBuilder.apply(mainDeck));
      //call merge/addcell with cellDeckBuilder.apply(mainDeck);
      //starting deck is empty
      //cellDeckBuilder.apply(mainDeck);
    }
    cellDeckBuilder = null;
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
    int total = 0;
    for (Entry<IOffset, ICell> e: getAllChildren().entrySet()) {
      total += e.getValue().getDeck().size();
      total += getTotalSize(); //TODO: MAKE SURE THIS DOESN'T INFINITE RECURSE
    }
    return total;
  }

  @Override
  public ICell getParent() {
    return parent;
  }

  @Override
  public IOffset getOffsetFromParent() {
    for (Entry<IOffset, ICell> e: parent.getAllChildren().entrySet()) {
      if (e.getKey()!= Offset.NONE && e.getValue()==this) {
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
    if (getDeck().size() > 0 ) {
      return false;
    }
    for (Entry<IOffset, ICell> e: getAllChildren().entrySet()) {
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
    ICell recipient = getAllChildren().get(offset);
    if (cell.getAllChildren().keySet().size() <= 1) {
      recipient.getDeck().addDeck(cell.getDeck());
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

  private void updateParentage() {
    String masterName = "";
    if (parent == null) {
      masterName = getName();
    } else {
      masterName = parent.getName()+","+getOffsetFromParent().getOffset();
    }
    name = masterName;
    for (Entry<IOffset, ICell> e: getAllChildren().entrySet()) {
      if (e.getKey()!=Offset.NONE) {
        Cell c = (Cell)e.getValue();
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
    ((Cell)cell).setParent(this); //fixme you're a monster
    updateParentage();
  }

  @Override
  public List<ICell> getAllCells() {
    List<ICell> total = new ArrayList<>();
    for (Entry<IOffset, ICell> e: getAllChildren().entrySet()) {
      if (!total.contains(e)) {
        total.add(e.getValue());
        total.addAll(e.getValue().getAllCells());
      }//TODO: MAKE SURE THIS DOESN'T INFINITE RECURSE
    }
    return total;
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
  public boolean equals(Object other) {
    if (! (other instanceof Cell)) {
      return false;
    }
    Cell c = (Cell) other;
    if (!deck.equals(c.deck)){
      return false;
    }
    for (Entry<IOffset, ICell> e: getAllChildren().entrySet()) {
      if (e.getKey()!=Offset.NONE){
        if (!e.getValue().getAllChildren().get(e.getKey()).equals(c.getAllChildren().get(e.getKey())))
        {
          return false;
        }
      }
    }
    return true;
  }
}
