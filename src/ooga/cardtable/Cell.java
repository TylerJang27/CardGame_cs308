package ooga.cardtable;

import java.util.Map;

public class Cell implements ICell {
  private Deck deck;
  private String name;

  public Cell(String nm) {
    name = nm;
    deck = new Deck();
  }

  public Cell(String nm, Deck d) {
    this(nm);
    deck = d;
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
  public Map<IOffset, ICell> getAllChildren() {
    return null;
  }

  @Override
  public Map<IOffset, ICell> getHeldCells() {
    return null;
  }

  @Override
  public void addCard(IOffset offset, ICard card) {

  }
}
