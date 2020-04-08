package ooga.cardtable;

public interface IDeck {

  void shuffle();

  int size();

  ICard getNextCard();

  ICard getRandomCard();

  ICard getBottomCard();

  ICard getCardAtIndex(int index);

  void addCard(ICard card);

  String getName(); //TODO: ADD TO API CHANGES
}
