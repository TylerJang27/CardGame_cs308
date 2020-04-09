package ooga.data;

import ooga.cardtable.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Class for parsing XML nodes and files to generate IDeck instances.
 * Has the option to read in a Deck from a separate file or directly from a rules file.
 *
 * @author Tyler Jang
 */
public class DeckFactory implements Factory {
    public static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
    private static final String DECK = "deck";
    private static final ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_PACKAGE+DECK);

    private static final String DECK_PATH = "DeckPath";
    private static final String DECK_NAME = "DeckName";
    private static final String CARD = "Card";
    private static final String SHUFFLE = "Shuffle";
    private static final String YES = "yes";
    private static final String DEFAULT_SHUFFLE = YES; //TODO: IMPLEMENT
    private static final String NAME = "Name";
    private static final String VALUE = "Value";
    private static final String COLOR = "Color";
    private static final String SUIT = "Suit";

    private static DocumentBuilder documentBuilder;

    public DeckFactory() { documentBuilder = XMLHelper.getDocumentBuilder();}

    /**
     * Builds and returns a deck using a document root
     *
     * @param root  the root of the document which holds a deck element
     * @return      a fully constructed IDeck instance
     * @throws      XMLException if deck is empty or missing
     */
    public static IDeck getDeck(Element root) {
        try {
            Node deck = root.getElementsByTagName(DECK).item(0);
            if (deck.hasChildNodes()) {
                NodeList nodeList = deck.getChildNodes();
                String pathToDeck = resources.getString(DECK_PATH);
                String deckPath = XMLHelper.getTextValue((Element)deck, pathToDeck);
                if (deckPath != "") {
                    return findStoredDeck(deckPath);
                } else {
                    //return buildDeck(nodeList);
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
     * Builds a deck based on a file path
     *
     * @param filePath the path to the XML where the deck is store
     * @return a fully built IDeck instance
     * @throws XMLException if deck is missing
     */
    private static IDeck findStoredDeck(String filePath) {
        try {
            File f = new File(filePath);
            Element root = XMLHelper.getRootAndCheck(f, DECK, INVALID_ERROR);
            Node deck = root.getElementsByTagName(DECK).item(0);
            //return buildDeck(deck.getChildNodes());
            return buildDeck(deck);
        } catch (Exception e) {
            throw new XMLException(e, MISSING_ERROR + "," + DECK);
        }
    }

    /**
     * Builds a deck based on a node
     *
     * @param node
     * @return
     */
    //private static IDeck buildDeck(NodeList nodeList) {
    private static IDeck buildDeck(Node node) {
        Element deck = (Element)node;
        String shuffle = XMLHelper.getTextValue(deck, resources.getString(SHUFFLE));
        String deckName = XMLHelper.getTextValue(deck, resources.getString(DECK_NAME));
        NodeList nodeList = deck.getElementsByTagName(resources.getString(CARD));

        List<ICard> cardList = new ArrayList<>();
        for (int k = 0; k < nodeList.getLength(); k ++) {
            Node n = nodeList.item(k);
            cardList.add(buildCard(n));
        }
        IDeck myDeck = new Deck(deckName, cardList);
        if (shuffle.equals(YES)) {
            myDeck.shuffle();
        }
        return myDeck;
    }

    private static ICard buildCard(Node node) {
        String cardName = Factory.getVal(node, NAME, resources);
        Integer val = Integer.parseInt(Factory.getVal(node, VALUE, resources));
        IColor color = new Color(Factory.getVal(node, COLOR, resources));
        ISuit suit = new Suit(Factory.getVal(node, SUIT, resources), color);
        IValue value = new Value(val + suit.getName(), val);
        return new Card(suit, value);
    }
}
