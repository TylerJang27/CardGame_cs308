package ooga.cardtable;

public interface IDeck {

  void shuffle();

  int size();

  ICard getNextCard();

  ICard getRandomCard();

  ICard getBottomCard();

  ICard getCardAtIndex(int index);

  void addCard(ICard card);
}
