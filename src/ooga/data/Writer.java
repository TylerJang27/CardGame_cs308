package ooga.data;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This interface is used to provide structure and helper methods for different XML Writers.
 *
 * @author Tyler Jang
 */
public interface Writer {

  String DATA = "data";
  String TYPE = "type";

  /**
   * Writes the document to the output filepath.
   *
   * @param document the Document to write
   * @param filepath the filepath to save the file
   * @throws TransformerException if a transformation is not possible
   */
  static void writeOutput(Document document, String filepath) throws TransformerException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource domSource = new DOMSource(document);
    StreamResult streamResult = new StreamResult(new File(filepath));

    transformer.transform(domSource, streamResult);
  }

  /**
   * Builds and prepares a Document of the given dataType
   *
   * @param dataType the data type for the XML file
   * @return the root Element of the document
   */
  static Element buildDocumentWithRoot(String dataType) {
    DocumentBuilder documentBuilder = XMLHelper.getDocumentBuilder();
    Document document = documentBuilder.newDocument();
    Element root = document.createElement(DATA);
    document.appendChild(root);

    Attr attribute = document.createAttribute(TYPE);
    attribute.setValue(dataType);
    root.setAttributeNode(attribute);

    return root;
  }
}
