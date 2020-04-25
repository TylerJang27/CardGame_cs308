package ooga.data.factories;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import ooga.cardtable.Card;
import ooga.cardtable.Color;
import ooga.cardtable.Deck;
import ooga.cardtable.ICard;
import ooga.cardtable.IColor;
import ooga.cardtable.IDeck;
import ooga.cardtable.ISuit;
import ooga.cardtable.IValue;
import ooga.cardtable.Suit;
import ooga.cardtable.Value;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This DeckFactory implements Factory and constructs an IDeck using the createDeck() method. Has
 * the option to read in a Deck from a separate file or directly from a rules file. This IDeck is
 * used to store information about all the cards, including their name, color, suit, and value.
 *
 * @author Tyler Jang
 */
public class DeckFactory implements Factory {

  private static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
  private static final String DECK = "deck";
  private static final ResourceBundle RESOURCES = ResourceBundle.getBundle(RESOURCE_PACKAGE + DECK);

  private static final String DECK_PATH = "DeckPath";
  private static final String DECK_NAME = "DeckName";
  private static final String CARD = "Card";
  private static final String SHUFFLE = "Shuffle";
  private static final String YES = "y";
  private static final String NAME = "Name";
  private static final String VALUE = "Value";
  private static final String COLOR = "Color";
  private static final String SUIT = "Suit";
  private static final String FIXED = "Fixed";

  private DeckFactory() {
  }

  /**
   * Builds and returns an IDeck built from a rules XML. Requirements for rules XML can be found in
   * doc/XML_Documentation.md.
   *
   * @param root the root of the filr from which an IDeck is built
   * @return a fully constructed IDeck instance
   * @throws XMLException if deck is empty or missing
   */
  public static IDeck createDeck(Element root) {
    try {
      Node deck = root.getElementsByTagName(DECK).item(0);
      if (deck.hasChildNodes()) {
        String pathToDeck = RESOURCES.getString(DECK_PATH);
        String deckPath = XMLHelper.getTextValue((Element) deck, pathToDeck);
        if (!deckPath.equals("")) {
          return findStoredDeck(deckPath);
        } else {
          return buildDeck(deck);
        }
      } else {
        throw new XMLException(MISSING_ERROR + "," + DECK);
      }
    } catch (Exception e) {
      throw new XMLException(e, MISSING_ERROR + "," + DECK);
    }
  }

  /**
   * Builds a deck based on a file path.
   *
   * @param filePath the path to the XML where the deck is stored
   * @return a fully built IDeck instance
   * @throws XMLException if deck is missing or the file cannot be found
   */
  private static IDeck findStoredDeck(String filePath) {
    try {
      File f = new File(filePath);
      Element root = XMLHelper.getRootAndCheck(f, DECK, INVALID_ERROR);
      Node deck = root.getElementsByTagName(DECK).item(0);
      return buildDeck(deck);
    } catch (Exception e) {
      throw new XMLException(e, MISSING_ERROR + "," + DECK);
    }
  }

  /**
   * Builds and returns a full deck based on a node. Shuffles the deck unless otherwise specified.
   *
   * @param node the starting node from which to build the deck
   * @return a fully built IDeck implementation
   */
  private static IDeck buildDeck(Node node) {
    Element deck = (Element) node;
    String shuffle = XMLHelper.getTextValue(deck, RESOURCES.getString(SHUFFLE));
    String deckName = XMLHelper.getTextValue(deck, RESOURCES.getString(DECK_NAME));
    NodeList nodeList = deck.getElementsByTagName(RESOURCES.getString(CARD));

    List<ICard> cardList = new ArrayList<>();
    for (int k = 0; k < nodeList.getLength(); k++) {
      Node n = nodeList.item(k);
      cardList.add(buildCard(n));
    }
    IDeck myDeck = new Deck(deckName, cardList);
    if (shuffle.equalsIgnoreCase(YES)) {
      myDeck.shuffle();
    }
    return myDeck;
  }

  /**
   * Builds an returns an ICard implementation based on a card node.
   *
   * @param node the starting node from which to build the card
   * @return a fully built ICard implementation
   */
  private static ICard buildCard(Node node) {
    String cardName = Factory.getVal(node, NAME, RESOURCES);
    Integer val = Integer.parseInt(Factory.getVal(node, VALUE, RESOURCES));
    IColor color = new Color(Factory.getVal(node, COLOR, RESOURCES));
    ISuit suit = new Suit(Factory.getVal(node, SUIT, RESOURCES), color);
    IValue value = new Value(val + suit.getName(), val);
    String fixed = XMLHelper.getTextValue((Element) node, RESOURCES.getString(FIXED));
    boolean isFixed = false;
    if (fixed != null && !fixed.isEmpty() && fixed.equalsIgnoreCase(YES)) {
      isFixed = true;
    }
    ICard c = new Card(cardName, suit, value);
    c.setFixed(isFixed);
    return c;
  }
}
