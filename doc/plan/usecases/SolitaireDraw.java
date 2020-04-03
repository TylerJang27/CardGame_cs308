package ooga.apiexamples;

import ooga.cardtable.ICard;
import ooga.cardtable.ICell;
import ooga.cardtable.IOffset;

public class SolitaireDraw {

  private ICell deckCell;
  private ICell drawCell;

  public void draw(int n) {
    for (int i = 0; i < n; i++) {
      ICard card = deckCell.getDeck().getNextCard();
      card.flip();
      drawCell.addCard(new IOffset("none"), card);
    }
  }

}
