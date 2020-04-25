package ooga.data.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import ooga.cardtable.Cell;
import ooga.cardtable.ICard;
import ooga.cardtable.ICell;
import ooga.cardtable.IDeck;
import ooga.cardtable.IOffset;
import ooga.cardtable.Offset;
import ooga.data.XMLException;
import ooga.data.factories.buildingblocks.CardBlock;
import ooga.data.factories.buildingblocks.ICardBlock;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This InitializeFactory implements Factory and constructs a Function<IDeck, ICell> for each ICell
 * using the createInitialization() method. This Initialization is used to build the initial card
 * setup for each cell.
 *
 * @author Tyler Jang
 */
public class InitializeFactory implements Factory {

  private static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
  private static final String DECK = "deck";
  private static final String INITIALIZE = "initialize";
  private static final ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_PACKAGE + DECK);
  private static final ResourceBundle initializeResources = ResourceBundle
      .getBundle(RESOURCE_PACKAGE + INITIALIZE);

  private static final String CARD = "Card";
  private static final String RANDOM = "Random";
  private static final String ALL = "All";
  private static final String DELIMITER = "Delimiter";
  private static final String UP = "Up";
  private static final String DOWN = "Down";

  private static final int OFFSET_REGEX = 3;

  private InitializeFactory() {
  }

  /**
   * Builds and return a Function of IDeck to ICell built from a rules XML. Requirements for rules
   * XML can be found in doc/XML_Documentation.md.
   *
   * @param settings the Node from which the Function is built
   * @param offset   the offset used for all of the cards
   * @param rotation the rotation used for the cell
   * @return a Function that, when applied, builds an ICell by pulling cards from the supplied
   * IDeck.
   */
  public static Function<IDeck, ICell> createInitialization(Node settings, IOffset offset,
      double rotation) {
    List<Function<IDeck, ICardBlock>> functionList = new ArrayList<>();
    try {
      NodeList cards = ((Element) settings).getElementsByTagName(resources.getString(CARD));

      for (int k = 0; k < cards.getLength(); k++) {
        String regex = cards.item(k).getTextContent();
        String[] regexSplit = regex.split(initializeResources.getString(DELIMITER));

        if (regexSplit[0].equals(initializeResources.getString(RANDOM))) {
          functionList.add(retrieveRandomCard(regexSplit));
        } else if (regexSplit[0].equals(initializeResources.getString(ALL))) {
          return retrieveDeck(rotation);
        } else {
          functionList.add(retrieveNameCard(regexSplit));
        }
      }
    } catch (Exception e) {
      throw new XMLException(e, Factory.MISSING_ERROR + "," + resources.getString(INITIALIZE));
    }
    return getDeckBuilderFunction(offset, rotation, functionList);
  }

  /**
   * Condenses functionList into one Function that takes in an IDeck and returns an ICell.
   *
   * @param offset       the Offset from which to build the cards
   * @param rotation     the double rotation for each card
   * @param functionList the List of Functions of IDeck to ICardBlock from which to build the full
   *                     ICell
   * @return
   */
  private static Function<IDeck, ICell> getDeckBuilderFunction(IOffset offset, double rotation,
      List<Function<IDeck, ICardBlock>> functionList) {
    return (IDeck source) -> {
      ICell c = new Cell("");
      ICell root = c;
      for (Function<IDeck, ICardBlock> f : functionList) {
        ICardBlock card = f.apply(source);

        Double rot = rotation;
        if (card.getFutureRotation() != null) {
          rot = card.getFutureRotation();
        }
        card.rotate(rot);

        IOffset off = offset;
        if (card.getOffset() != null) {
          off = card.getOffset();
        }
        c.addCard(off, card.getCard());
        if (!off.equals(Offset.NONE)) {
          c = c.getHeldCells().get(off);
        }
      }
      return root;
    };
  }

  /**
   * Builds and returns a Function that retrieves a card from a deck by name.
   *
   * @param regexSplit an array of Strings containing the name of the card and the flip setting
   * @return a Function of IDeck to ICardBlock to build the initialization for the cell
   */
  private static Function<IDeck, ICardBlock> retrieveNameCard(String[] regexSplit) {
    return (IDeck source) -> {
      ICard c = source.getCardByName(regexSplit[0]);
      setFlip(c, regexSplit[1]);
      return getCardBlock(c, regexSplit);
    };
  }

  /**
   * Builds and returns a Function that retrieves an entire deck and sets relevant rotation on its
   * cards.
   *
   * @param rotation the rotation to set the cards in the deck
   * @return a Function of IDeck to ICell for the entire deck
   */
  private static Function<IDeck, ICell> retrieveDeck(double rotation) {
    return (IDeck source) -> {
      ICell c = new Cell("", source);
      for (int k = 0; k < source.size(); k++) {
        source.peekCardAtIndex(k).rotate(rotation);
      }
      return c;
    };
  }

  /**
   * Builds and returns a Function that retrieves a random card from the deck.
   *
   * @param regexSplit an array of Strings containing the name of the card and the flip setting
   * @return a Function of IDeck to ICardBlock to build the initialization for the cell
   */
  private static Function<IDeck, ICardBlock> retrieveRandomCard(String[] regexSplit) {
    return (IDeck source) -> {
      ICard c = source.getRandomCard();
      setFlip(c, regexSplit[1]);
      return getCardBlock(c, regexSplit);
    };
  }

  /**
   * Sets the ICard's faceup/facedown orientation based on direction.
   *
   * @param c         the ICard to flip
   * @param direction the String representing whether the card should be up or down
   */
  private static void setFlip(ICard c, String direction) {
    boolean up = c.isFaceUp();
    if (up && direction.equalsIgnoreCase(initializeResources.getString(DOWN))) {
      c.flip();
    } else if (!up && direction.equalsIgnoreCase(initializeResources.getString(UP))) {
      c.flip();
    }
  }

  /**
   * Builds a wrapper for each ICard, containing information on offset and rotation.
   *
   * @param card       the ICard to wrap
   * @param regexSplit an array of Strings that may or may not contain information on Offset and
   *                   direction
   * @return an ICardBlock with ICard, offset, and direction information
   */
  private static ICardBlock getCardBlock(ICard card, String[] regexSplit) {
    IOffset off = null;
    Double rotation = null;
    if (regexSplit.length > 2) {
      if (Offset.validOffsets.contains(regexSplit[2].toLowerCase())) {
        off = Offset.valueOf(regexSplit[2].toUpperCase());
      }
      if (regexSplit.length > OFFSET_REGEX) {
        try {
          rotation = Double.parseDouble(regexSplit[OFFSET_REGEX]);
        } catch (NumberFormatException e) {
          rotation = null;
        }
      }
    }
    return new CardBlock(card, off, rotation);
  }

}
