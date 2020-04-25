package ooga.data.factories;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This interface refers to the different factories that are used for parsing information from XML
 * files into usable fields and class information.
 *
 * @author Tyler Jang
 */
public interface Factory {

  String INVALID_ERROR = "InvalidFile";
  String MISSING_ERROR = "MissingAttribute";
  String CONTROL_ERROR = "ControlError";
  String UNKNOWN_ERROR = "UnknownError";
  String BLANK_TEXT = "#text";

  /**
   * Retrieves the String value of the first tag relative to n with the label matched to tagRef in
   * resources. If an exception occurs, an empty String is returned.
   *
   * @param n         the Node from which to search
   * @param tagRef    the String to translate in the resource bundle
   * @param resources the ResourceBundle to use to translate
   * @return the String text value searched for
   */
  static String getVal(Node n, String tagRef, ResourceBundle resources) {
    try {
      return XMLHelper.getTextValue((Element) n, resources.getString(tagRef), () -> {
        throw new XMLException(MISSING_ERROR + "," + tagRef);
      });
    } catch (ClassCastException | MissingResourceException e) {
      return "";
    }
  }
}
