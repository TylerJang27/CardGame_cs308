package ooga.data.style;

import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.factories.Factory;
import ooga.data.factories.StyleFactory;
import ooga.data.style.IStyle;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Class for writing styling information into an XML file. Saves user settings.
 *
 * @author Andrew Krier, Tyler Jang
 */
public class XMLWriter {

    private static String WORD = "word";
    private static String NUMBER = "number";
    private static String STYLE = "style";
    private static final String RESOURCES = "ooga.resources";
    private static final String RESOURCE_PACKAGE = RESOURCES + "." + STYLE + "_";
    private static final ResourceBundle WORD_RESOURCES = ResourceBundle.getBundle(RESOURCE_PACKAGE + WORD);
    private static final ResourceBundle NUMBER_RESOURCES = ResourceBundle.getBundle(RESOURCE_PACKAGE + NUMBER);

    private static final String DATA = "data";
    private static final String TYPE = "type";
    private static final String DATA_TYPE = StyleFactory.STYLE_TYPE;

    private static final String LANGUAGE = "Language";
    private static final String CARDS = "Cards";
    private static final String TABLE = "Table";

    private static final String DARK = "Dark";
    private static final String DIFFICULTY = "Difficulty";
    private static final String SOUND = "Sound";

    /**
     * Writes information from IStyle implementation to filepath
     *
     * @param filepath the destination for the XML file
     * @param style    the IStyle from which to build the XML file
     */
    public static void writeStyle(String filepath, IStyle style) {
        try {
            DocumentBuilder documentBuilder = XMLHelper.getDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement(DATA);
            document.appendChild(root);

            Attr attribute = document.createAttribute(TYPE);
            attribute.setValue(DATA_TYPE);
            root.setAttributeNode(attribute);

            addStyle(document, root, style);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(filepath));

            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            throw new XMLException(e, Factory.UNKNOWN_ERROR);
        }
    }

    /**
     * Extracts information from style to add to the document
     *
     * @param document the document to which data should be added
     * @param root     the root Element of the document
     * @param style    the IStyle implementation from which to parse data
     */
    private static void addStyle(Document document, Element root, IStyle style) {
        Map<String, String> vals = Map.of(
                WORD_RESOURCES.getString(LANGUAGE), style.getLanguage(),
                WORD_RESOURCES.getString(CARDS), style.getCardSkinPath(),
                WORD_RESOURCES.getString(TABLE), style.getTheme(),
                NUMBER_RESOURCES.getString(DARK), "" + (style.getDarkMode() ? 1 : 0),
                NUMBER_RESOURCES.getString(DIFFICULTY), ("" + style.getDifficulty()),
                NUMBER_RESOURCES.getString(SOUND), "" + (style.getSound() ? 1 : 0));
        for (Map.Entry<String, String> entry : vals.entrySet()) {
            Element e = document.createElement(entry.getKey());
            e.appendChild(document.createTextNode(entry.getValue()));
            root.appendChild(e);
        }
    }
}
