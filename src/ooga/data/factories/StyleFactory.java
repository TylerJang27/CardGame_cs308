package ooga.data.factories;

import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.style.IStyle;
import ooga.data.style.StyleData;
import org.w3c.dom.Element;

import java.io.File;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This StyleFactory implements Factory and constructs an IStyle using the createStyle() method.
 * Style information is used by the view.
 * <p>
 * Class based mainly on ConfigParser.java from spike_simulation by Rhondu Smithwick and Robert C.
 * Duvall https://coursework.cs.duke.edu/compsci308_2020spring/spike_simulation/blob/master/src/xml/XMLParser.java
 * <p>
 * Code for iterating through ResourceBundle keys based on:
 * https://www.programcreek.com/java-api-examples/?class=java.util.ResourceBundle&method=getKeys
 *
 * @author Tyler Jang
 */
public class StyleFactory implements Factory {

    public static String STYLE_TYPE = IStyle.DATA_TYPE;

    private static String WORD = "word";
    private static String NUMBER = "number";
    private static String STYLE = "style";
    private static final String RESOURCES = "ooga.resources";
    private static final String RESOURCE_PACKAGE = RESOURCES + "." + STYLE + "_";
    private static final ResourceBundle wordResources = ResourceBundle.getBundle(RESOURCE_PACKAGE + WORD);
    private static final ResourceBundle numberResources = ResourceBundle.getBundle(RESOURCE_PACKAGE + NUMBER);

    /**
     * Builds and returns an IStyle from a styling XML. Requirements for style XML can be found in ___.
     *
     * @param dataFile file from which to read configuration
     * @return an IStyle with all of its configuration information stored
     * @throws XMLException if the file is not considered valid due to its root element or file ending
     */
    public static IStyle createStyle(File dataFile) {
        try {
            Element root = XMLHelper.getRootAndCheck(dataFile, STYLE_TYPE, INVALID_ERROR);

            Map<String, String> stringSettings = XMLHelper.readStringSettings(root, wordResources);
            Map<String, Integer> numberSettings = XMLHelper.readNumberSettings(root, numberResources);
            return new StyleData(dataFile.getPath(), stringSettings, numberSettings);
        } catch (Exception e) {
            throw new XMLException(e, Factory.MISSING_ERROR + "," + STYLE_TYPE);
        }
    }
}
