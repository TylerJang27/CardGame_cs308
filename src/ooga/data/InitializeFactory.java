package ooga.data;

import ooga.cardtable.Deck;
import ooga.cardtable.ICard;
import ooga.cardtable.IDeck;
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
    private static final ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_PACKAGE+DECK);

    private static final String CARD = "Card";

    private static DocumentBuilder documentBuilder;

    public InitializeFactory() { documentBuilder = XMLHelper.getDocumentBuilder();}

    public static Function<IDeck, IDeck> getInitialization(Node settings) {
        List<Function<IDeck, ICard>> functionList = new ArrayList<>();

        NodeList cards = ((Element)settings).getElementsByTagName(resources.getString(CARD));

        for (int k = 0; k < cards.getLength(); k ++) {
            String regex = cards.item(k).getNodeValue();



        }
        return (IDeck source) -> {
            IDeck d = new Deck();
            for (Function<IDeck, ICard> f: functionList) {
                d.addCard(f.apply(source));
            }
            return d;
        };
    }

}
