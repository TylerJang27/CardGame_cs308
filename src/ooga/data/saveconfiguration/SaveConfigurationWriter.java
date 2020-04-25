package ooga.data.saveconfiguration;

import java.util.Map;
import java.util.ResourceBundle;
import javax.xml.transform.TransformerException;
import ooga.cardtable.ICell;
import ooga.data.Writer;
import ooga.data.XMLException;
import ooga.data.factories.Factory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Class for writing save configuration information into an XML file. Saves the name of the game,
 * the path to the rules, the current phase, the score, and Strings used to rebuild cells.
 *
 * @author Tyler Jang
 */
public class SaveConfigurationWriter implements Writer {

  private static final String DATA_TYPE = ISaveConfiguration.DATA_TYPE;

  private static final String RESOURCE_PACKAGE = "ooga.resources.";
  private static final String SAVE = "save";
  private static final ResourceBundle RESOURCES = ResourceBundle.getBundle(RESOURCE_PACKAGE + SAVE);

  private static final String PHASE = "Phase";
  private static final String GAME = "Game";
  private static final String SCORE = "Score";
  private static final String FILE = "File";
  private static final String CELL = "Cell";
  private static final String NAME = "Name";
  private static final String CELLS = "Cells";

  private SaveConfigurationWriter() {
  }

  /**
   * Writes information from ISaveConfiguration implementation to filepath
   *
   * @param filepath the destination for the XML file
   * @param scores   the ISaveConfiguration from which to build the XML file
   */
  public static void writeSave(String filepath, ISaveConfiguration scores) {
    try {
      Element root = Writer.buildDocumentWithRoot(DATA_TYPE);
      Document document = root.getOwnerDocument();

      addSaves(document, root, scores);

      Writer.writeOutput(document, filepath);
    } catch (TransformerException e) {
      throw new XMLException(e, Factory.UNKNOWN_ERROR);
    }
  }

  /**
   * Extracts information from save configuration to add to the document
   *
   * @param document the document to which data should be added
   * @param root     the root Element of the document
   * @param save     the ISaveConfiguration implementation from which to parse data
   */
  private static void addSaves(Document document, Element root, ISaveConfiguration save) {
    appendNodeAndText(document, root, PHASE, save.getCurrentPhase());
    appendNodeAndText(document, root, GAME, save.getGameName());
    appendNodeAndText(document, root, FILE, save.getRulePath());
    appendNodeAndText(document, root, SCORE, "" + save.getScore());

    root.appendChild(buildCells(document, save));
  }

  /**
   * Builds the Element and children responsible for storing cell information
   *
   * @param document the document to which data should be added
   * @param save     the ISaveConfiguration implementation from which to parse data
   * @return the Element storing cell information
   */
  private static Element buildCells(Document document, ISaveConfiguration save) {
    Element cells = document.createElement(RESOURCES.getString(CELLS));
    for (Map.Entry<String, ICell> e : save.getCellMap().entrySet()) {
      Element cell = document.createElement(RESOURCES.getString(CELL));
      Attr a = document.createAttribute(RESOURCES.getString(NAME));
      a.setValue(e.getKey());
      cell.setAttributeNode(a);
      cell.setTextContent(e.getValue().toStorageString());
      cells.appendChild(cell);
    }
    return cells;
  }

  /**
   * A helper method, adding a simple text node with a text value to root
   *
   * @param document  the document to which data should be added
   * @param root      the root Element of the document
   * @param tag       the tag to translate from a resources file and name the node
   * @param textValue the value to set for the Element
   */
  private static void appendNodeAndText(Document document, Element root, String tag,
      String textValue) {
    Element e = document.createElement(RESOURCES.getString(tag));
    e.appendChild(document.createTextNode(textValue));
    root.appendChild(e);
  }

}
