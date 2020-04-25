package ooga.data.factories;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.XMLValidator;
import ooga.data.style.Coordinate;
import ooga.data.style.ICoordinate;
import ooga.data.style.ILayout;
import ooga.data.style.Layout;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This LayoutFactory implements Factory and constructs an ILayout using the createLayout() method.
 * This ILayout is used to store information about where cells should be located and how they should
 * be drawn for a particular game.
 *
 * @author Andrew Krier, Tyler Jang
 */
public class LayoutFactory implements Factory {

  private static String LAYOUT_TYPE = ILayout.DATA_TYPE;
  private static String INVALID_ERROR = "INVALID_FILE";

  private static final String LAYOUT_XSD = "src/ooga/data/factories/schemas/layout_schema.xsd";
  private static final String KEY_CARD = "Card";
  private static final String KEY_CARDS = "Cards";
  private static final String KEY_CELL = "Cell";
  private static final String KEY_CELLS = "Cells";
  private static final String KEY_NAME = "Name";
  private static final String KEY_X = "X";
  private static final String KEY_Y = "Y";
  private static final String KEY_START = "Start";
  private static final String KEY_EXTENSION = "Extension";

  private static final String COMMA = ",";

  private static final String RESOURCES = "ooga.resources";
  private static final String RESOURCE_PACKAGE = RESOURCES + "." + "layout_word";
  private static final String RESOURCE_COORD_PACKAGE = RESOURCES + "." + "layout_coord";
  private static final String RESOURCE_MAP_PACKAGE = RESOURCES + "." + "layout_deck";
  private static final ResourceBundle layoutResources = ResourceBundle.getBundle(RESOURCE_PACKAGE);
  private static final ResourceBundle coordResources = ResourceBundle
      .getBundle(RESOURCE_COORD_PACKAGE);
  private static final ResourceBundle mapResources = ResourceBundle.getBundle(RESOURCE_MAP_PACKAGE);

  private LayoutFactory() {
  }

  /**
   * Builds and returns an ILayout from a layout XML file. Requirements for layout XML can be found
   * in doc/XML_Documentation.md.
   *
   * @param dataFile the file from which to build an ILayout implementation
   * @return an ILayout implementation built from the layout XML
   */
  public static ILayout createLayout(File dataFile) {
    if (XMLValidator.validateXMLSchema(LAYOUT_XSD, dataFile.getPath())) {
      try {
        Element root = XMLHelper.getRootAndCheck(dataFile, LAYOUT_TYPE, INVALID_ERROR);

        if (!root.hasChildNodes()) {
          throw new XMLException(Factory.MISSING_ERROR + COMMA + LAYOUT_TYPE); // Very bad
        }

        Map<String, Integer> numberSettings = XMLHelper.readNumberSettings(root, layoutResources);
        Map<String, ICoordinate> coordMap = coordinateMap(root);
        Map<String, String> cardMap = cardMap(root);

        return new Layout(coordMap, numberSettings, cardMap);
      } catch (Exception e) {
        throw new XMLException(e, Factory.MISSING_ERROR + COMMA + LAYOUT_TYPE);
      }
    } else {
      throw new XMLException(Factory.INVALID_ERROR);
    }
  }

  private static Map<String, ICoordinate> coordinateMap(Element root) {
    Node cells = root.getElementsByTagName(coordResources.getString(KEY_CELLS)).item(0);

    NodeList cellList = ((Element) cells).getElementsByTagName(coordResources.getString(KEY_CELL));

    Map<String, ICoordinate> coordMap = new HashMap<>();

    for (int k = 0; k < cellList.getLength(); k++) {
      Element n = (Element) cellList.item(k);
      String cellName = XMLHelper.getAttribute(n, coordResources.getString(KEY_NAME));
      NodeList coordinate = n.getChildNodes();
      Node x = XMLHelper.getNodeByName(coordinate, coordResources.getString(KEY_X));
      Node y = XMLHelper.getNodeByName(coordinate, coordResources.getString(KEY_Y));

      ICoordinate coord = new Coordinate(Double.parseDouble(x.getTextContent()),
          Double.parseDouble(y.getTextContent()));

      coordMap.put(cellName, coord);
    }
    return coordMap;
  }

  private static Map<String, String> cardMap(Element root) {
    Node beginning = root.getElementsByTagName(mapResources.getString(KEY_START)).item(0);
    Node ending = root.getElementsByTagName(mapResources.getString(KEY_EXTENSION)).item(0);
    String intro = beginning.getTextContent();
    String outro = ending.getTextContent();

    Node deck = root.getElementsByTagName(mapResources.getString(KEY_CARDS)).item(0);
    NodeList cardList = ((Element) deck).getElementsByTagName(mapResources.getString(KEY_CARD));

    Map<String, String> cardMap = new HashMap<>();

    for (int i = 0; i < cardList.getLength(); i++) {
      String name = cardList.item(i).getTextContent();
      String path = intro + name + outro;
      cardMap.put(name, path);
    }

    return cardMap;
  }
}
