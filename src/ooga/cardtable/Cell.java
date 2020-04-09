package ooga.cardtable;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

public class Cell implements ICell {

  private IDeck deck;
  private String name;
  private Function<IDeck, ICell> cellDeckBuilder;
  private Map<IOffset, ICell> children;

  public Cell(String nm) {
    name = nm;
    deck = new Deck();
  }

  public Cell(String nm, IDeck d) {
    this(nm);
    deck = d;
  }

  @Override
  public void setDraw(Function<IDeck, ICell> initializer) {
    cellDeckBuilder = initializer;
  }

  @Override
  public void initializeCards(IDeck mainDeck) {
    if (cellDeckBuilder != null) {
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
    if (cell == null || cell.isEmpty()) {
      return;
    }
    ICell recipient = getAllChildren().get(offset);
    if (cell.getAllChildren().keySet().size() <= 1) {
      recipient.getDeck().addDeck(cell.getDeck());
      return;
    }
    for (Entry<IOffset, ICell> e : cell.getAllChildren().entrySet()) {
      recipient.getAllChildren().get(e.getKey()).addCell(Offset.NONE, e.getValue());
    }
  }
}
