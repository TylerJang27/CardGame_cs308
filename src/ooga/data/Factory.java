package ooga.data;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ResourceBundle;

//TODO: ADD DOCUMENTATION
public interface Factory {

    String INVALID_ERROR = "INVALID_FILE";
    String MISSING_ERROR = "MISSING_ATTRIBUTE";

    static String getVal(Node n, String tagRef, ResourceBundle resources) {
        return XMLHelper.getTextValue((Element)n, resources.getString(tagRef), () -> {throw new XMLException(MISSING_ERROR + "," + tagRef);});
    }
}
