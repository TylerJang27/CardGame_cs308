package ooga.data.factories;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.XMLValidator;
import ooga.data.saveconfiguration.ISaveConfiguration;
import ooga.data.saveconfiguration.SaveConfiguration;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This SaveConfigurationFactory implements Factory and constructs an ISaveConfiguration using the
 * createSave() method. This ISaveConfiguration is used to load a state from a previous game.
 *
 * @author Tyler Jang
 */
public class SaveConfigurationFactory implements Factory {

  private static final String DATA_TYPE = ISaveConfiguration.DATA_TYPE;

  private static final String RESOURCE_PACKAGE = "ooga.resources.";
  private static final String SAVE = "save";
  private static final ResourceBundle RESOURCES = ResourceBundle.getBundle(RESOURCE_PACKAGE + SAVE);

  public static String SAVE_XSD = "src/ooga/data/factories/schemas/save_schema.xsd";

  private static final String PHASE = "Phase";
  private static final String GAME = "Game";
  private static final String SCORE = "Score";
  private static final String FILE = "File";
  private static final String NAME = "Name";
  private static final String CELLS = "Cells";

  private SaveConfigurationFactory() {
  }

  /**
   * Builds and returns an ISaveConfiguration from a saved XML.
   *
   * @param dataFile file from which to read configuration
   * @return an ISaveConfiguration with all of its configuration information stored
   * @throws XMLException if the file is not considered valid due to its root element or file
   *                      ending
   */
  public static ISaveConfiguration createSave(File dataFile) {
    if (XMLValidator.validateXMLSchema(SAVE_XSD, dataFile.getPath())) {
      try {
        Element root = XMLHelper.getRootAndCheck(dataFile, DATA_TYPE, INVALID_ERROR);

        String game = XMLHelper.getTextValue(root, RESOURCES.getString(GAME));
        String phase = XMLHelper.getTextValue(root, RESOURCES.getString(PHASE));
        String file = XMLHelper.getTextValue(root, RESOURCES.getString(FILE));
        Double score = Double.parseDouble(XMLHelper.getTextValue(root, RESOURCES.getString(SCORE)));
        Map<String, String> cellBuilder = extractCellBuilder(root);

        return new SaveConfiguration(game, file, phase, score, cellBuilder);
      } catch (Exception e) {
        throw new XMLException(e, Factory.MISSING_ERROR + "," + DATA_TYPE);
      }
    } else {
      throw new XMLException((Factory.INVALID_ERROR));
    }
  }

  private static Map<String, String> extractCellBuilder(Element e) {
    Map<String, String> cellMap = new HashMap<>();
    Node cells = XMLHelper.getNodeByName(e.getChildNodes(), RESOURCES.getString(CELLS));
    NodeList cellNodeList = cells.getChildNodes();
    for (int k = 0; k < cellNodeList.getLength(); k++) {
      Node cell = cellNodeList.item(k);

      if (!cell.getNodeName().equals(Factory.BLANK_TEXT)) {
        String cellName = XMLHelper.getAttribute((Element) cell, RESOURCES.getString(NAME));
        cellMap.put(cellName, cell.getTextContent());
      }

    }
    return cellMap;
  }
}
