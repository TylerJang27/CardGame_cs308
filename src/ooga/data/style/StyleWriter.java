package ooga.data.style;

import java.util.Map;
import java.util.ResourceBundle;
import javax.xml.transform.TransformerException;
import ooga.data.Writer;
import ooga.data.XMLException;
import ooga.data.factories.Factory;
import ooga.data.factories.StyleFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Class for writing styling information into an XML file. Saves user settings.
 *
 * @author Andrew Krier, Tyler Jang
 */
public class StyleWriter implements Writer {

  private static String WORD = "word";
  private static String NUMBER = "number";
  private static String STYLE = "style";
  private static final String RESOURCES = "ooga.resources";
  private static final String RESOURCE_PACKAGE = RESOURCES + "." + STYLE + "_";
  private static final ResourceBundle WORD_RESOURCES = ResourceBundle
      .getBundle(RESOURCE_PACKAGE + WORD);
  private static final ResourceBundle NUMBER_RESOURCES = ResourceBundle
      .getBundle(RESOURCE_PACKAGE + NUMBER);

  private static final String DATA_TYPE = StyleFactory.STYLE_TYPE;

  private static final String LANGUAGE = "Language";
  private static final String CARDS = "Cards";
  private static final String TABLE = "Table";

  private static final String DARK = "Dark";
  private static final String DIFFICULTY = "Difficulty";
  private static final String SOUND = "Sound";

  private StyleWriter() {
  }

  /**
   * Writes information from IStyle implementation to filepath
   *
   * @param filepath the destination for the XML file
   * @param style    the IStyle from which to build the XML file
   */
  public static void writeStyle(String filepath, IStyle style) {
    try {
      Element root = Writer.buildDocumentWithRoot(DATA_TYPE);
      Document document = root.getOwnerDocument();

      addStyle(document, root, style);

      Writer.writeOutput(document, filepath);
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
        WORD_RESOURCES.getString(LANGUAGE), makeCamelCase(style.getLanguage().toLowerCase()),
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

  /**
   * Converts a String into a capital first letter and lowercase remaining letters.
   *
   * @param s the String to convert
   * @return the original String, all lowercase except the first letter
   */
  private static String makeCamelCase(String s) {
    if (s.length() > 1) {
      return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    } else {
      return s.toUpperCase();
    }
  }
}
