package ooga.cardtable;

public interface IDeck {

  void shuffle();

  ICard getNextCard();

  ICard getRandomCard();

  ICard getBottomCard();

  ICard getCardAtIndex(int index);
}
