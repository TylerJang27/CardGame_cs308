package ooga.data;

import ooga.cardtable.IDeck;
import ooga.data.rules.IPhaseMachine;
import ooga.data.rules.ISettings;
import ooga.data.rules.PhaseMachine;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 *
 * @author Tyler Jang, Andrew Krier
 */
public class PhaseMachineFactory {
    public static String RULES_TYPE = IPhaseMachine.DATA_TYPE;
    //TODO: INCORPORATE ERROR MESSAGES
    public static String INVALID_ERROR = "INVALID_FILE";
    public static String MISSING_ERROR = "MISSING_ATTRIBUTE";

    private static final String RULES = "rules_tags";
    private static final String RESOURCES = "ooga.resources";
    public static final String RESOURCE_PACKAGE = RESOURCES + "." + RULES + "_";
    //private static final ResourceBundle rulesResources = ResourceBundle.getBundle(RESOURCE_PACKAGE+RULES);
    //TODO: IMPLEMENT DEFAULTS

    //TODO: REMOVE HARD CODING?

    private static DocumentBuilder documentBuilder;

    public PhaseMachineFactory() {
        documentBuilder = XMLHelper.getDocumentBuilder();
    }

    public static IPhaseMachine getPhaseMachine(File dataFile) {
        Element root = XMLHelper.getRootAndCheck(dataFile, RULES_TYPE, INVALID_ERROR);

        ISettings settings = SettingsFactory.getSettings(root);
        IDeck deck = DeckFactory.getDeck(root);



        //Cells
        //Phases
        //Build
        return new PhaseMachine(new ArrayList<>());
    }

}
