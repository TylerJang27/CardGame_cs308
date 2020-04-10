package ooga.data.factories;

import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.rules.ILayout;
import ooga.data.rules.Layout;
import ooga.data.style.Coordinate;
import ooga.data.style.ICoordinate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class LayoutFactory {

    private static String LAYOUT_TYPE = ILayout.DATA_TYPE;
    private static String INVALID_ERROR = "INVALID_FILE";

    private static final String RESOURCES = "ooga.resources";
    private static final String RESOURCE_PACKAGE = RESOURCES + "." + "layout_word";
    private static final String RESOURCE_COORD_PACKAGE = RESOURCES + "." + "layout_coord";
    private static final ResourceBundle layoutResources = ResourceBundle.getBundle(RESOURCE_PACKAGE);
    private static final ResourceBundle coordResources = ResourceBundle.getBundle(RESOURCE_COORD_PACKAGE);

    private static DocumentBuilder documentBuilder;

    public LayoutFactory() {
        documentBuilder = XMLHelper.getDocumentBuilder();
    }

    public static ILayout getLayout(File dataFile) {
        try {
            Element root = XMLHelper.getRootAndCheck(dataFile, LAYOUT_TYPE, INVALID_ERROR);

            if (!root.hasChildNodes()) {
                throw new XMLException(Factory.MISSING_ERROR + "," + LAYOUT_TYPE); // Very bad
            }

            Map<String, Integer> numberSettings = XMLHelper.readNumberSettings(root, layoutResources);
            //System.out.println(numberSettings.toString());

            // FIXME!!!!
            Node cells = root.getElementsByTagName(coordResources.getString("Cells")).item(0);

            NodeList cellList = cells.getChildNodes();

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

            return new Layout(coordMap, numberSettings);
        } catch (Exception e) {
            throw new XMLException(e, Factory.MISSING_ERROR + "," + LAYOUT_TYPE);
        }
    }
}
