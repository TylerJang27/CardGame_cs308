package ooga.data;

import ooga.cardtable.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

public class InitializeFactory implements Factory {
    public static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
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

    //TODO: SHORTEN METHOD
    public static Function<IDeck, ICell> getInitialization(Node settings, IOffset offset) {
        List<Function<IDeck, ICard>> functionList = new ArrayList<>();
        NodeList cards = ((Element)settings).getElementsByTagName(resources.getString(CARD));

        for (int k = 0; k < cards.getLength(); k ++) {
            String regex = cards.item(k).getNodeValue();
            String[] regexSplit = regex.split(initializeResources.getString(DELIMITER));

            if (regexSplit[0].equals(initializeResources.getString(RANDOM))) {
                functionList.add((IDeck source) -> {
                    ICard c = source.getRandomCard();
                    setFlip(c, regexSplit[1]);
                    return c;
                });
            } else if (regexSplit[0].equals(initializeResources.getString(ALL))) {
                //NOTE: ORDER OF CELL GROUP PARSING MAY MATTER FOR THIS TO WORK
                //ALSO WHILE THIS TAKES IN OFFSET, IT IS BEST IF IT IS NONE
                return (IDeck source) -> {
                    ICell c = new Cell("");
                    while (source.size() > 0) {
                        c.addCard(offset, source.getRandomCard());
                    }
                    return c;
                }
            } else {
                functionList.add((IDeck source) -> {
                    ICard c =  source.getCardByName(regexSplit[0]);
                    setFlip(c, regexSplit[1]);
                    return c;
                });
            }
        }
        return (IDeck source) -> {
            ICell c = new Cell("");
            for (Function<IDeck, ICard> f: functionList) {
                c.addCard(offset, f.apply(source));
            }
            return c;
        };
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
