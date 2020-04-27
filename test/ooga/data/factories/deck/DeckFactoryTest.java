package ooga.data.factories.deck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import ooga.cardtable.Card;
import ooga.cardtable.Color;
import ooga.cardtable.Deck;
import ooga.cardtable.ICard;
import ooga.cardtable.IDeck;
import ooga.cardtable.ISuit;
import ooga.cardtable.IValue;
import ooga.cardtable.Suit;
import ooga.cardtable.Value;
import ooga.data.XMLHelper;
import ooga.data.factories.DeckFactory;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

/**
 * Testing class for DeckFactory to ensure it produces the correct cards of correct number and
 * correct type.
 *
 * @author Tyler Jang
 */
class DeckFactoryTest {

  private static final String TEST_DIRECTORY = "test/ooga/data/factories/deck/";

  /**
   * Tests the functionality of DeckFactory.createDeck() as well as the equality of decks and
   * inequality based on reversal.
   */
  @Test
  void createDeck() {
    List<ICard> cards = buildDeckCards();
    String deckName = "pack52";
    IDeck expected1 = new Deck(deckName, cards);
    IDeck expected2 = new Deck("", cards);

    Element root = XMLHelper
        .getRootAndCheck(new File(TEST_DIRECTORY + "deck1.xml"), "deck", "invalid file");
    IDeck actualDeck1 = DeckFactory.createDeck(root);

    root = XMLHelper
        .getRootAndCheck(new File(TEST_DIRECTORY + "deck2.xml"), "deck", "invalid file");
    IDeck actualDeck2 = DeckFactory.createDeck(root);

    IDeck expected3 = expected1.copy();
    expected3.reverse();

    assertEquals(expected1, actualDeck1);
    assertEquals(expected2, actualDeck1);
    assertEquals(expected1.getName(), actualDeck1.getName());
    assertEquals(expected1, actualDeck2);
    assertNotEquals(expected3, actualDeck1);
  }

  /**
   * A helper method for use across testing that builds a full deck of 52 cards.
   *
   * @return a List of ICards containing the standard 52 deck.
   */
  private static List<ICard> buildDeckCards() {
    List<String> names = List.of("A", "2", "3", "4", "5", "6", "7", "8", "9", "0", "J", "Q", "K");
    List<String> vals = List
        .of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13");
    List<String> colors = List.of("black", "red", "red", "black");
    List<String> suits = List.of("c", "d", "h", "s");
    List<ICard> cards = new ArrayList<>();
    for (int k = 0; k < suits.size(); k++) {
      for (int j = 0; j < names.size(); j++) {
        IValue val = new Value(vals.get(j) + suits.get(k), Integer.parseInt(vals.get(j)));
        ISuit suit = new Suit(suits.get(k), new Color(colors.get(k)));
        cards.add(new Card(names.get(j) + suits.get(k).toUpperCase(), suit, val));
      }
    }
    return cards;
  }
}