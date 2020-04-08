package ooga.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ooga.data.style.IStyle;
import ooga.data.style.StyleData;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;


/**
 * Class for parsing XML files to determine Styling information for the view. Creates an IStyle instance from which styling information can be read.
 * <p>
 * Class based mainly on ConfigParser.java from spike_simulation by Rhondu Smithwick and Robert C.
 * Duvall https://coursework.cs.duke.edu/compsci308_2020spring/spike_simulation/blob/master/src/xml/XMLParser.java
 *
 * @author Tyler Jang
 */
public class StyleParser {

    public static String STYLE_TYPE = IStyle.DATA_TYPE;
    //TODO: INCORPORATE ERROR MESSAGES
    public static String INVALID_ERROR = "INVALID_FILE";
    public static String MISSING_ERROR = "MISSING_ATTRIBUTE";

    private static String WORD = "word";
    private static String NUMBER = "number";

    private static DocumentBuilder DOCUMENT_BUILDER;

    public StyleParser() {
        DOCUMENT_BUILDER = XMLHelper.getDocumentBuilder();
    }

    /**
     * Get data contained in this XML file as an IStyle object. Tests to ensure that the file is valid.
     *
     * @param dataFile file from which to read configuration
     * @return    an IStyle with all of its configuration information stored
     * @throws XMLException if the file is not considered valid due to its root element or file ending
     */
    public static IStyle getStyle(File dataFile) {
        if (!XMLHelper.isXML(dataFile)) {
            throw new XMLException(INVALID_ERROR, STYLE_TYPE);
        }

        Element root = XMLHelper.getRootElement(DOCUMENT_BUILDER, dataFile);

        if (!XMLHelper.isValidFile(root, STYLE_TYPE)) {
            throw new XMLException(INVALID_ERROR, STYLE_TYPE);
        }

        Map<String, String> stringSettings = readStringSettings(root);
        Map<String, Integer> numberSettings = readNumberSettings(root);

        return new StyleData(stringSettings, numberSettings);
    }

    /**
     * Generates a Map of Strings of configuration settings, including optional and mandatory information.
     *
     * @param root document root
     * @return a Map of Strings of configuration settings
     */
    private Map<String, String> readSettings(Element root) {
        //TODO: READ FROM STYLE PROPERTIES
        Map<String, String> simulationSettings = new HashMap<>();
        for (String field : IStyle.MANDATORY_DATA_FIELDS) {
            simulationSettings.put(field, getTextValue(root, field, true));
        }
        for (String field : IStyle.OPTIONAL_DATA_FIELDS) {
            putOptional(root, simulationSettings, field);
        }
        for (String field : SimType.of(simulationSettings.get(IStyle.MANDATORY_DATA_FIELDS.get(0)))
                .getMandatoryFields()) {
            simulationSettings.put(field, getTextValue(root, field, true));
        }
        for (String field : SimType.of(simulationSettings.get(IStyle.MANDATORY_DATA_FIELDS.get(0)))
                .getOptionalFields()) {
            putOptional(root, simulationSettings, field);
        }
        return simulationSettings;
    }

    /**
     * Adds value in field to simulationSettings if its value exists, throws no exception.
     *
     * @param root               document root
     * @param simulationSettings Map of fields and data for Simulation
     * @param field              name of field in XML file
     */
    private void putOptional(Element root, Map<String, String> simulationSettings, String field) {
        String val = getTextValue(root, field, false);
        if (val.length() > 0) {
            simulationSettings.put(field, val);
        }
    }
}
