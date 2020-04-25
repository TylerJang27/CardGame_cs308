package ooga.data;

import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 * Class for validating XML files against an XML Schema. This is used for increased validation
 * before rules, styling, and layouts are parsed.
 * <p>
 * Explanation and setup taken from: https://www.w3schools.com/xml/schema_intro.asp XMLValidator
 * taken from: https://www.journaldev.com/895/how-to-validate-xml-against-xsd-in-java
 *
 * @author Pankaj, Tyler Jang
 */
public class XMLValidator {

  private XMLValidator() {
  }

  /**
   * Validates an XML File against an XSD Schema. Returns whether or not the file is valid.
   *
   * @param xsdPath the path to the XSD file
   * @param xmlPath the path to the XML file
   * @return whether or not the XML file is valid
   */
  public static boolean validateXMLSchema(String xsdPath, String xmlPath) {
    try {
      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schema = factory.newSchema(new File(xsdPath));
      Validator validator = schema.newValidator();
      validator.validate(new StreamSource(new File(xmlPath)));
    } catch (IOException | SAXException e) {
      System.out.println("Exception: " + e.getMessage());
      return false;
    }
    return true;
  }

}
