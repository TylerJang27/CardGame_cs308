package ooga.cardtable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

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
  public boolean isFixed() {
    if (cards.size() > 0) {
      //System.out.println(cards.get(0).getName());
      return cards.get(0).isFixed();
    } else {
      return false;
    }
  }

  @Override
  public String toStorageString() {
    String ret = getName() + "[";
    for (ICard c : cards) {
      ret += c.toStorageString() + "*";
    }
    if (ret.length() > ("" + getName()).length() + 1) {
      ret = ret.substring(0, ret.length() - 1);
    }
    return ret + "]";
  }

  public static IDeck fromStorageString(String input) {
    if (input == null) {
      return null;
    }
    if (input.endsWith("[]")) {
      return new Deck();
    }
    String nm = input.split("\\[")[0];
    input = input.replaceFirst(Pattern.quote(nm), "");
    input = input.substring(1, input.length() - 1);
    List<ICard> newCards = new ArrayList<>();
    for (String s : input.split("\\*")) {
      newCards.add(Card.fromStorageString(s));
    }
    return new Deck(nm, newCards);
  }

  @Override
  public void shuffle() {
    Collections.shuffle(cards);
  }

  @Override
  public void reverse() {
    Collections.reverse(cards);
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
    if (cards.get(index).isFixed() && cards.size() > 1) {
      return getRandomCard();
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
      cards.add(0, card);
    }
  }

  @Override
  public void addDeck(IDeck deck) { //fixme consider making an iterable?
    for (int i = deck.size() - 1; i >= 0; i--) {
      //System.out.println(deck.peekCardAtIndex(i) + "is my card");
      addCard(deck.getCardAtIndex(i));
    }
    /*for (int k = 0; k < deck.size(); k ++) {
      addCard(deck.getCardAtIndex(k));
    }*/
  }

  @Override
  public String getName() {
    return myName;
  }

  @Override
  public ICard getCardByName(
      String name) {
    for (ICard c : cards) {
      if (c.getName().equals(name)) {
        //System.out.println("searched name: " + c.getName());
        cards.remove(c);
        return c;
      }
    }
    return getNextCard();
  }

  @Override
  public IDeck copy() {
    List<ICard> c = new ArrayList<>();
    for (ICard card : cards) {
      c.add(card.copy());
    }
    return new Deck(myName, c);
  }

  @Override
  public boolean equals(Object other) {
    return other.getClass() == this.getClass() && cards.equals(((Deck) other).cards);
  }

  @Override
  public String toString() {
    return cards.toString();
  }
}
