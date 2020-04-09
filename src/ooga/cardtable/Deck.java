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
  public ICard getNextCard() {
    return getCardAtIndex(0);
  }

  @Override
  public ICard getRandomCard() {
    Random rand = new Random();
    return getCardAtIndex(rand.nextInt(size()));
  }

  //TODO: MAVERICK FIX THIS SIZE?
  @Override
  public ICard getBottomCard() {
    return getCardAtIndex(size());
  }

  @Override
  public ICard getCardAtIndex(int index) { //removes the card from the deck
    return cards.remove(index);
  }

  @Override
  public ICard peek() {
    return peekCardAtIndex(0);
  }

  @Override
  public ICard peekBottom() {
    return peekCardAtIndex(size());
  }

  @Override
  public ICard peekCardAtIndex(int index) {
    return cards.get(index);
  }

  @Override
  public void addCard(ICard card) { //fixme make package private?
    cards.add(card);
  }

  @Override
  public String getName() { return myName; }

  @Override
  public ICard getCardByName(String name) { //TODO: MAKE SURE THIS WORKS WITH THE OFFSET AS I WANT IT TO
    for (ICard c: cards) {
      if (c.getName().equals(name)) {
        return c;
      }
    }
    return getNextCard();
  }
}
