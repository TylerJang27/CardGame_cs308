package ooga.cardtable;

public interface IDeck {

  void shuffle();

  int size();

  ICard getNextCard();

  ICard getRandomCard();

  ICard getBottomCard();

  ICard getCardAtIndex(int index);

  ICard peek();

  ICard peekBottom();

  ICard peekCardAtIndex(int index);

  void addCard(ICard card);

  String getName(); //TODO: ADD TO API CHANGES

  ICard getCardByName(String name); //TODO: ADD TO API CHANGES
}
