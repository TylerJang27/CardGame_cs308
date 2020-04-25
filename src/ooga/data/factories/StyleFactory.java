package ooga.data.factories;

import java.io.File;
import java.util.Map;
import java.util.ResourceBundle;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.XMLValidator;
import ooga.data.style.IStyle;
import ooga.data.style.StyleData;
import org.w3c.dom.Element;

/**
 * This StyleFactory implements Factory and constructs an IStyle using the createStyle() method.
 * Style information is used by the view.
 * <p>
 * Class based mainly on ConfigParser.java from spike_simulation by Rhondu Smithwick and Robert C.
 * Duvall https://coursework.cs.duke.edu/compsci308_2020spring/spike_simulation/blob/master/src/xml/XMLParser.java
 * <p>
 * Code for iterating through ResourceBundle keys based on: https://www.programcreek.com/java-api-examples/?class=java.util.ResourceBundle&method=getKeys
 *
 * @author Tyler Jang
 */
public class StyleFactory implements Factory {

  public static String STYLE_TYPE = IStyle.DATA_TYPE;
  public static String STYLE_XSD = "src/ooga/data/factories/schemas/style_schema.xsd";

  private static String WORD = "word";
  private static String NUMBER = "number";
  private static String STYLE = "style";
  private static final String RESOURCES = "ooga.resources";
  private static final String RESOURCE_PACKAGE = RESOURCES + "." + STYLE + "_";
  private static final ResourceBundle WORD_RESOURCES = ResourceBundle
      .getBundle(RESOURCE_PACKAGE + WORD);
  private static final ResourceBundle NUMBER_RESOURCES = ResourceBundle
      .getBundle(RESOURCE_PACKAGE + NUMBER);

  private StyleFactory() {
  }

  /**
   * Builds and returns an IStyle from a styling XML. Requirements for style XML can be found in
   * doc/XML_Documentation.md.
   *
   * @param dataFile file from which to read configuration
   * @return an IStyle with all of its configuration information stored
   * @throws XMLException if the file is not considered valid due to its root element or file
   *                      ending
   */
  public static IStyle createStyle(File dataFile) {
    return createStyle(dataFile, dataFile.getPath());
  }

  /**
   * Builds and returns an IStyle from a styling XML. Requirements for style XML can be found in
   * doc/XML_Documentation.md.
   *
   * @param dataFile    file from which to read configuration
   * @param destination String for the destination to save the file
   * @return an IStyle with all of its configuration information stored
   * @throws XMLException if the file is not considered valid due to its root element or file
   *                      ending
   */
  public static IStyle createStyle(File dataFile, String destination) {
    if (XMLValidator.validateXMLSchema(STYLE_XSD, dataFile.getPath())) {
      try {
        Element root = XMLHelper.getRootAndCheck(dataFile, STYLE_TYPE, INVALID_ERROR);

        Map<String, String> stringSettings = XMLHelper.readStringSettings(root, WORD_RESOURCES);
        Map<String, Integer> numberSettings = XMLHelper.readNumberSettings(root, NUMBER_RESOURCES);
        return new StyleData(destination, stringSettings, numberSettings);
      } catch (Exception e) {
        throw new XMLException(e, Factory.MISSING_ERROR + "," + STYLE_TYPE);
      }
    } else {
      throw new XMLException((Factory.INVALID_ERROR));
    }
  }
}
