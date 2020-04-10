package ooga.cardtable;

public interface IDeck {

  void shuffle();

  int size();

  boolean isEmpty();

  ICard getNextCard();

  ICard getRandomCard();

  ICard getBottomCard();

  ICard getCardAtIndex(int index);

  ICard peek();

  ICard peekBottom();

  ICard peekCardAtIndex(int index);

  void addCard(ICard card);

  void addDeck(IDeck deck);

  String getName(); //TODO: ADD TO API CHANGES

  ICard getCardByName(String name); //TODO: ADD TO API CHANGES
}
