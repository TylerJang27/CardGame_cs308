package ooga.data;

import ooga.data.rules.ILayout;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import java.io.File;

public class LayoutFactory {

    private static String LAYOUT_TYPE = ILayout.DATA_TYPE;
    private static String INVALID_ERROR = "INVALID_FILE";

    private static DocumentBuilder documentBuilder;

    public LayoutFactory() {
        documentBuilder = XMLHelper.getDocumentBuilder();
    }

    public static ILayout getLayout(File dataFile) {
        Element root = XMLHelper.getRootAndCheck(dataFile, LAYOUT_TYPE, INVALID_ERROR);


    }

}
