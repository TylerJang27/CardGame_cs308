package ooga.data.factories;

import ooga.cardtable.*;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

public class InitializeFactory implements Factory {
    private static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
    private static final String DECK = "deck";
    private static final String INITIALIZE = "initialize";
    private static final ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_PACKAGE+DECK);
    private static final ResourceBundle initializeResources = ResourceBundle.getBundle(RESOURCE_PACKAGE+INITIALIZE);

    private static final String CARD = "Card";
    private static final String RANDOM = "Random";
    private static final String ALL = "All";
    private static final String DELIMITER = "Delimiter";
    private static final String UP = "Up";
    private static final String DOWN = "Down";

    private static DocumentBuilder documentBuilder;

    public InitializeFactory() { documentBuilder = XMLHelper.getDocumentBuilder();}

    public static Function<IDeck, ICell> getInitialization(Node settings, IOffset offset, double rotation) { //TODO: IMPLEMENT ROTATION
        List<Function<IDeck, ICard>> functionList = new ArrayList<>();
        try {
            NodeList cards = ((Element) settings).getElementsByTagName(resources.getString(CARD));

            for (int k = 0; k < cards.getLength(); k++) {
                String regex = cards.item(k).getTextContent();
                String[] regexSplit = regex.split(initializeResources.getString(DELIMITER));

                if (regexSplit[0].equals(initializeResources.getString(RANDOM))) {
                    retrieveRandomCard(functionList, regexSplit[1]);
                } else if (regexSplit[0].equals(initializeResources.getString(ALL))) {
                    return retrieveDeck();
                } else {
                    retrieveNameCard(functionList, regexSplit);
                }
            }
        } catch (Exception e) {
            throw new XMLException(e, Factory.MISSING_ERROR + "," + resources.getString(INITIALIZE));
        }
        return getDeckBuilderFunction(offset, functionList);
    }

    private static Function<IDeck, ICell> getDeckBuilderFunction(IOffset offset, List<Function<IDeck, ICard>> functionList) {
        return (IDeck source) -> {
            ICell c = new Cell("");
            ICell root = c;
            for (Function<IDeck, ICard> f: functionList) {
                c.addCard(offset, f.apply(source));
                c=c.getHeldCells().get(offset);
            }
            return root;
        };
    }

    private static void retrieveNameCard(List<Function<IDeck, ICard>> functionList, String[] regexSplit) {
        functionList.add((IDeck source) -> {
            ICard c = source.getCardByName(regexSplit[0]);
            setFlip(c, regexSplit[1]);
            return c;
        });
    }

    private static Function<IDeck, ICell> retrieveDeck() {
        return (IDeck source) -> {
            ICell c = new Cell("", source);
            return c;
        };
    }

    private static void retrieveRandomCard(List<Function<IDeck, ICard>> functionList, String direction) {
        functionList.add((IDeck source) -> {
            ICard c = source.getRandomCard();
            setFlip(c, direction);
            return c;
        });
    }

    private static void setFlip(ICard c, String direction) {
        boolean up = c.isFaceUp();
        if (up && direction.equals(initializeResources.getString(DOWN))) {
            c.flip();
        } else if (!up && direction.equals(initializeResources.getString(UP))) {
            c.flip();
        }
    }

}
