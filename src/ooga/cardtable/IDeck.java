package ooga.cardtable;

public interface IDeck {

  void shuffle();

  void reverse();

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

  String getName();

  ICard getCardByName(String name);

  IDeck copy();

  boolean isFixed();

  String toStorageString();
}
