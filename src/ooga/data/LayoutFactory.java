package ooga.data;

import ooga.data.rules.ILayout;
import ooga.data.style.ICoordinate;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.util.Map;
import java.util.ResourceBundle;

public class LayoutFactory {

    private static String LAYOUT_TYPE = ILayout.DATA_TYPE;
    private static String INVALID_ERROR = "INVALID_FILE";

    private static final String RESOURCES = "ooga.resources";
    private static final String RESOURCE_PACKAGE = RESOURCES + "." + "layout_word";
    private static final ResourceBundle layoutResources = ResourceBundle.getBundle(RESOURCE_PACKAGE);

    private static DocumentBuilder documentBuilder;

    public LayoutFactory() {
        documentBuilder = XMLHelper.getDocumentBuilder();
    }

    public static ILayout getLayout(File dataFile) {
        Element root = XMLHelper.getRootAndCheck(dataFile, LAYOUT_TYPE, INVALID_ERROR);

        if(!root.hasChildNodes()) return null; // Very bad

        Map<String, String> text = XMLHelper.readStringSettings(root, layoutResources);
        Map<String, Integer> temp = XMLHelper.readNumberSettings(root, layoutResources);
        System.out.println(text.toString());
        System.out.println(temp.toString());

        return null;
    }

    private Map<String, ICoordinate> makeCoordinateMap() {
        return null;
    }

}
