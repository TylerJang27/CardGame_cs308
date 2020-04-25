package ooga.data;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Static class for parsing XML files, with each sub-class implementation returning an instance of a
 * Data class object. Throws XMLExceptions in the event of a problem reading the XML file. These
 * Exceptions should be caught, with their messages directed to the user. This class and its
 * sub-classes require that Main have a ResourceBundle named myResources from which messages can be
 * drawn. To use this class, create a sub-class and implement methods such as getSimulation(),
 * getGrid(), or getStyle() using the given methods in this abstract class.
 * <p>
 * Class based mainly on ConfigParser.java from spike_simulation by Rhondu Smithwick and Robert C.
 * Duvall https://coursework.cs.duke.edu/compsci308_2020spring/spike_simulation/blob/master/src/xml/XMLParser.java
 *
 * @author Tyler Jang
 */
public class XMLHelper {

  public static final String XML_END = ".xml";
  public static String TYPE = "type";

  private XMLHelper() {
  }

  /**
   * Gets the root element of an XML file. This will reset the reading process, such that the XML
   * hierarchy can be built.
   *
   * @param xmlFile the File from which to read
   * @return the Root Element
   */
  public static Element getRootElement(DocumentBuilder db, File xmlFile) {
    try {
      db.reset();
      Document xmlDocument = db.parse(xmlFile);
      return xmlDocument.getDocumentElement();
    } catch (SAXException | IOException e) {
      throw new XMLException(e);
    }
  }

  /**
   * Checks whether or not a file is an XML file based on its name. If the file contains ".xml", as
   * defined in this class's constants, the file will be considered valid for the time being.
   *
   * @param dataFile the File to be analyzed
   * @return a boolean representing whether the file seems to be an XML file
   */
  public static boolean isXML(File dataFile) {
    return -1 != dataFile.getName().indexOf(XML_END);
  }

  /**
   * Checks if file is valid based on its root element. If this attribute does not match type, this
   * file will be considered invalid.
   *
   * @param root the root Element
   * @param type the type of XML file (e.g. Simulation)
   * @return a boolean if file is valid based on its root element
   */
  public static boolean isValidFile(Element root, String type) {
    return getAttribute(root, TYPE).equals(type);
  }

  /**
   * Gets attribute of document based off of name, using the hierarchy of the XML file.
   *
   * @param e             the Element from which to retrieve the attribute
   * @param attributeName the attribute's label to retrieve
   * @return the retrieved Attribute
   */
  public static String getAttribute(Element e, String attributeName) {
    return e.getAttribute(attributeName);
  }

  /**
   * Retrieves the text value in the XML file for a given tagName. Mandatory can be defined to
   * determine whether or not an exception is thrown if a tag is not found.
   *
   * @param e               the Element from which to retrieve the attribute
   * @param tagName         the tag to search for in the XML file
   * @param missingRunnable a runnable to execute if the element is not present (e.g. throw an
   *                        exception, do nothing)
   * @return the text for that tag
   */
  public static String getTextValue(Element e, String tagName, Runnable missingRunnable) {
    NodeList nodeList = e.getElementsByTagName(tagName);
    if (nodeList != null && nodeList.getLength() > 0) {
      return nodeList.item(0).getTextContent();
    } else {
      missingRunnable.run();
    }
    return "";
  }

  /**
   * Retrieves the text value in the XML file for a given tagName. Mandatory can be defined to
   * determine whether or not an exception is thrown if a tag is not found.
   *
   * @param e       the Element from which to retrieve the attribute
   * @param tagName the tag to search for in the XML file
   * @return the text for that tag
   */
  public static String getTextValue(Element e, String tagName) {
    return getTextValue(e, tagName, () -> {
    });
  }

  /**
   * Retrieves the node within a list by name. Returns null if not found
   *
   * @param n        the NodeList to search through
   * @param nodeName the name of the node to be searched for
   * @return the Node matching the name or null
   */
  public static Node getNodeByName(NodeList n, String nodeName) {
    for (int k = 0; k < n.getLength(); k++) {
      if (n.item(k).getNodeName().equals(nodeName)) {
        return n.item(k);
      }
    }
    return null;
  }

  /**
   * Retrieves the root element of a file and checks to make sure it fits the correct type
   *
   * @param dataFile the File from which to read
   * @param type     the type of XML data to be read
   * @param error    the error message to be thrown
   * @return the Root Element
   */
  public static Element getRootAndCheck(File dataFile, String type, String error) {
    DocumentBuilder documentBuilder = getDocumentBuilder();
    if (!isXML(dataFile)) {
      throw new XMLException(error, type);
    }

    Element root = getRootElement(documentBuilder, dataFile);

    if (!isValidFile(root, type)) {
      throw new XMLException(error, type);
    }
    return root;
  }

  /**
   * Required boilerplate code needed to make a documentBuilder.
   *
   * @return a DocumentBuilder for the entire class
   */
  public static DocumentBuilder getDocumentBuilder() {
    try {
      return DocumentBuilderFactory.newInstance().newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      throw new XMLException(e);
    }
  }

  /**
   * Generates a Map of Strings of style settings.
   *
   * @param root document root
   * @return a Map of Strings of style settings
   */
  public static Map<String, String> readStringSettings(Element root, ResourceBundle resource) {
    Map<String, String> styles = new HashMap<>();
    Enumeration<String> keys = resource.getKeys();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      String value = resource.getString(key);

      styles.put(value, XMLHelper.getTextValue(root, value));
    }
    return styles;
  }

  /**
   * Generates a Map of Integers of style settings.
   *
   * @param root document root
   * @return a Map of Integers of style settings
   */
  public static Map<String, Integer> readNumberSettings(Element root, ResourceBundle resource) {
    Map<String, Integer> styles = new HashMap<>();
    for (Map.Entry<String, String> e : readStringSettings(root, resource).entrySet()) {
      try {
        styles.put(e.getKey(), Integer.parseInt(e.getValue()));
      } catch (Exception ee) {
      }
    }
    return styles;
  }
}
