package ooga.data.factories;

import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.XMLValidator;
import ooga.data.style.ILayout;
import ooga.data.style.Layout;
import ooga.data.style.Coordinate;
import ooga.data.style.ICoordinate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This LayoutFactory implements Factory and constructs an ILayout using the createLayout() method.
 * This ILayout is used to store information about where cells should be located and how they should be drawn for a particular game.
 *
 * @author Andrew Krier, Tyler Jang
 */
public class LayoutFactory implements Factory {

    private static String LAYOUT_TYPE = ILayout.DATA_TYPE;
    private static String INVALID_ERROR = "INVALID_FILE";

    private static final String LAYOUT_XSD = "src/ooga/data/factories/layout_schema.xsd";

    private static final String RESOURCES = "ooga.resources";
    private static final String RESOURCE_PACKAGE = RESOURCES + "." + "layout_word";
    private static final String RESOURCE_COORD_PACKAGE = RESOURCES + "." + "layout_coord";
    private static final String RESOURCE_MAP_PACKAGE = RESOURCES + "." + "layout_deck";
    private static final ResourceBundle layoutResources = ResourceBundle.getBundle(RESOURCE_PACKAGE);
    private static final ResourceBundle coordResources = ResourceBundle.getBundle(RESOURCE_COORD_PACKAGE);
    private static final ResourceBundle mapResources = ResourceBundle.getBundle(RESOURCE_MAP_PACKAGE);

    //TODO: @ANDREW REFACTOR and add Error Handling

    /**
     * Builds and returns an ILayout from a layout XML file. Requirements for layout XML can be found in ___.
     *
     * @param dataFile the file from which to build an ILayout implementation
     * @return an ILayout implementation built from the layout XML
     */
    public static ILayout createLayout(File dataFile) {
        if (XMLValidator.validateXMLSchema(LAYOUT_XSD, dataFile.getPath())) {
            try {
                Element root = XMLHelper.getRootAndCheck(dataFile, LAYOUT_TYPE, INVALID_ERROR);

                if (!root.hasChildNodes()) {
                    throw new XMLException(Factory.MISSING_ERROR + "," + LAYOUT_TYPE); // Very bad
                }

                Map<String, Integer> numberSettings = XMLHelper.readNumberSettings(root, layoutResources);
                //System.out.println(numberSettings.toString());

                // FIXME!!!!
                Node cells = root.getElementsByTagName(coordResources.getString("Cells")).item(0);

                NodeList cellList = ((Element) cells).getElementsByTagName(coordResources.getString("Cell"));

                Map<String, ICoordinate> coordMap = new HashMap<>();
                for (int k = 0; k < cellList.getLength(); k++) {
                    Element n = (Element) cellList.item(k); // FIXME
                    String cellName = XMLHelper.getAttribute(n, coordResources.getString("Name"));
                    NodeList coordinate = n.getChildNodes();
                    Node x = XMLHelper.getNodeByName(coordinate, coordResources.getString("X"));
                    Node y = XMLHelper.getNodeByName(coordinate, coordResources.getString("Y"));

                    ICoordinate coord = new Coordinate(Double.parseDouble(x.getTextContent()), Double.parseDouble(y.getTextContent()));

                    coordMap.put(cellName, coord);
                }

                Node beginning = root.getElementsByTagName(mapResources.getString("Start")).item(0);
                Node ending = root.getElementsByTagName(mapResources.getString("Extension")).item(0);

                String intro = beginning.getTextContent();
                String outro = ending.getTextContent();

                Node deck = root.getElementsByTagName(mapResources.getString("Cards")).item(0);

                NodeList cardList = ((Element) deck).getElementsByTagName(mapResources.getString("Card"));

                Map<String, String> cardMap = new HashMap<>();

                for (int i = 0; i < cardList.getLength(); i++) {
                    String name = cardList.item(i).getTextContent();
                    String path = intro + name + outro;
                    cardMap.put(name, path);
                }

                return new Layout(coordMap, numberSettings, cardMap);
            } catch (Exception e) {
                throw new XMLException(e, Factory.MISSING_ERROR + "," + LAYOUT_TYPE);
            }
        } else {
            throw new XMLException(Factory.INVALID_ERROR);
        }
    }
}
