package ooga.cardtable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck implements IDeck {

  private String myName;
  private List<ICard> cards;

  public Deck() {
    cards = new ArrayList<>();
  }

  public Deck(String name, List<ICard> d) {
    myName = name;
    cards = d;
  }

  @Override
  public void shuffle() {
    Collections.shuffle(cards);
  }

  @Override
  public int size() {
    return cards.size();
  }

  @Override
  public boolean isEmpty() {
    return cards.isEmpty();
  }

  @Override
  public ICard getNextCard() {
    return getCardAtIndex(0);
  }

  @Override
  public ICard getRandomCard() {
    if (isEmpty()) {
      return null;
    }
    Random rand = new Random();
    return getCardAtIndex(rand.nextInt(size()));
  }

  @Override
  public ICard getBottomCard() {
    return getCardAtIndex(size() - 1);
  }

  @Override
  public ICard getCardAtIndex(int index) { //removes the card from the deck
    if (isEmpty()) {
      return null;
    }
    return cards.remove(index);
  }

  @Override
  public ICard peek() {
    return peekCardAtIndex(0);
  }

  @Override
  public ICard peekBottom() {
    return peekCardAtIndex(size() - 1);
  }

  @Override
  public ICard peekCardAtIndex(int index) {
    if (isEmpty()) {
      return null;
    }
    return cards.get(index);
  }

  @Override
  public void addCard(ICard card) { //fixme make package private?
    if (card != null) {
      cards.add(card);
    }
  }

  @Override
  public void addDeck(IDeck deck) { //fixme consider making an iterable?
    for (int i = 0; i < deck.size(); i++) {
      addCard(deck.peekCardAtIndex(i));
    }
  }

  @Override
  public String getName() {
    return myName;
  }

  @Override
  //TODO: HULLOO
  public ICard getCardByName(
      String name) { //TODO: MAKE SURE THIS WORKS WITH THE OFFSET AS I WANT IT TO
    for (ICard c : cards) {
      if (c.getName().equals(name)) {
        return c;
      }
    }
    return getNextCard();
  }

  @Override
  public IDeck copy() {
    List<ICard> c = new ArrayList<>();
    for (ICard card: cards) {
      c.add(card.copy());
    }
    return new Deck(myName, c);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Deck)) {
      return false;
    }
    Deck d = (Deck) other;
    return cards.equals(d.cards);
  }

  @Override
  public String toString() {
    return cards.toString();
  }
}
