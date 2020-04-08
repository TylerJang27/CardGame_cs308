package ooga.data;

import ooga.data.rules.IPhaseMachine;
import ooga.data.style.IStyle;

import javax.xml.parsers.DocumentBuilder;
import java.util.ResourceBundle;

public class PhaseMachineFactory {
    public static String RULES_TYPE = IPhaseMachine.DATA_TYPE;
    //TODO: INCORPORATE ERROR MESSAGES
    public static String INVALID_ERROR = "INVALID_FILE";
    public static String MISSING_ERROR = "MISSING_ATTRIBUTE";

    private static final String RULES = "rules";
    private static final String RESOURCES = "ooga.resources";
    private static final String RESOURCE_PACKAGE = RESOURCES + "." + RULES + "_";
    private static final ResourceBundle rulesResources = ResourceBundle.getBundle(RESOURCE_PACKAGE+RULES);
    //TODO: IMPLEMENT DEFAULTS

    //TODO: REMOVE HARD CODING?

    private static DocumentBuilder documentBuilder;

    public PhaseMachineFactory() {
        documentBuilder = XMLHelper.getDocumentBuilder();
    }

}
