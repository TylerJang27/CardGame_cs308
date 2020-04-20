package ooga.data.factories;

import ooga.cardtable.*;
import ooga.data.XMLException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * This InitializeFactory implements Factory and constructs a Function<IDeck, ICell> for each ICell using the createInitialization() method.
 * This Initialization is used to build the initial card setup for each cell.
 *
 * @author Tyler Jang
 */
public class InitializeFactory implements Factory {
    private static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
    private static final String DECK = "deck";
    private static final String INITIALIZE = "initialize";
    private static final ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_PACKAGE + DECK);
    private static final ResourceBundle initializeResources = ResourceBundle.getBundle(RESOURCE_PACKAGE + INITIALIZE);

    private static final String CARD = "Card";
    private static final String RANDOM = "Random";
    private static final String ALL = "All";
    private static final String DELIMITER = "Delimiter";
    private static final String UP = "Up";
    private static final String DOWN = "Down";

    /**
     * Builds and return a Function of IDeck to ICell built from a rules XML. Requirements for rules XML can be found in ___.
     *
     * @param settings  the Node from which the Function is built
     * @param offset    the offset used for all of the cards //TODO: UNLESS OTHERWISE SPECIFIED
     * @param rotation  the rotation used for the cell
     * @return          a Function that, when applied, builds an ICell by pulling cards from the supplied IDeck.
     */
    public static Function<IDeck, ICell> createInitialization(Node settings, IOffset offset, double rotation) {
        List<Function<IDeck, ICard>> functionList = new ArrayList<>();
        try {
            NodeList cards = ((Element) settings).getElementsByTagName(resources.getString(CARD));

            for (int k = 0; k < cards.getLength(); k++) {
                String regex = cards.item(k).getTextContent();
                String[] regexSplit = regex.split(initializeResources.getString(DELIMITER));

                if (regexSplit[0].equals(initializeResources.getString(RANDOM))) {
                    functionList.add(retrieveRandomCard(regexSplit[1]));
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
     * @param offset        the Offset from which to build the cards
     * @param rotation      the double rotation for each card
     * @param functionList  the List of Functions of IDeck to ICard from which to build the full ICell
     * @return
     */
    private static Function<IDeck, ICell> getDeckBuilderFunction(IOffset offset, double rotation, List<Function<IDeck, ICard>> functionList) {
        return (IDeck source) -> {
            ICell c = new Cell("");
            ICell root = c;
            for (Function<IDeck, ICard> f : functionList) {
                ICard card = f.apply(source);
                card.rotate(rotation);
                c.addCard(offset, card);
                c = c.getHeldCells().get(offset);

            }
            return root;
        };
    }

    /**
     * Builds and returns a Function that retrieves a card from a deck by name.
     *
     * @param regexSplit    an array of Strings containing the name of the card and the flip setting
     * @return              a Function of IDeck to ICard to build the initialization for the cell
     */
    private static Function<IDeck, ICard> retrieveNameCard(String[] regexSplit) {
        return (IDeck source) -> {
            ICard c = source.getCardByName(regexSplit[0]);
            setFlip(c, regexSplit[1]);
            return c;
        };
    }

    /**
     * Builds and returns a Function that retrieves an entire deck and sets relevant rotation on its cards.
     *
     * @param rotation  the rotation to set the cards in the deck
     * @return          a Function of IDeck to ICell for the entire deck
     */
    private static Function<IDeck, ICell> retrieveDeck(double rotation) {
        return (IDeck source) -> {
            ICell c = new Cell("", source);
            for (int k = 0; k < source.size(); k ++) {
                source.peekCardAtIndex(k).rotate(rotation);
            }
            return c;
        };
    }

    /**
     * Builds and returns a Function that retrieves a random card from the deck.
     *
     * @param rotation  the rotation to set the cards in the deck
     * @return              a Function of IDeck to ICard to build the initialization for the cell
     */
    private static Function<IDeck, ICard> retrieveRandomCard(String rotation) {
        return (IDeck source) -> {
            ICard c = source.getRandomCard();
            setFlip(c, rotation);
            return c;
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

}
