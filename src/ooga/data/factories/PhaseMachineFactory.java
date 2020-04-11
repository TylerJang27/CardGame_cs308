package ooga.data.factories;

import ooga.cardtable.ICell;
import ooga.cardtable.IDeck;
import ooga.data.XMLException;
import ooga.data.rules.PhaseMachine;
import ooga.data.XMLHelper;
import ooga.data.rules.*;
import ooga.data.rules.IPhaseMachine;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Tyler Jang, Andrew Krier
 */
public class PhaseMachineFactory implements Factory{
    public static String RULES_TYPE = IPhaseMachine.DATA_TYPE;
    //TODO: INCORPORATE ERROR MESSAGES

    private static final String RULES = "rules";
    private static final String RESOURCES = "ooga.resources";
    public static final String RESOURCE_PACKAGE = RESOURCES + "." + RULES + "_";
    //private static final ResourceBundle rulesResources = ResourceBundle.getBundle(RESOURCE_PACKAGE+RULES);

    public static final String START="INIT_PHASE";

    //TODO: IMPLEMENT DEFAULTS

    //TODO: REMOVE HARD CODING?

    private static DocumentBuilder documentBuilder;

    public PhaseMachineFactory() {
        documentBuilder = XMLHelper.getDocumentBuilder();
    }

    public static IPhaseMachine getPhaseMachine(File dataFile) {
        try {
            Element root = XMLHelper.getRootAndCheck(dataFile, RULES_TYPE, INVALID_ERROR);
            ISettings settings = SettingsFactory.getSettings(root);
            IDeck deck = DeckFactory.getDeck(root);
            System.out.println("B");
            Map<String, ICellGroup> cellGroups = CellGroupFactory.getCellGroups(root);
            for (Map.Entry<String, ICellGroup> e : cellGroups.entrySet()) {
                e.getValue().initializeAll(deck);
            }
            System.out.println("C");
            Map<String, ICell> allBaseCells = getAllCells(cellGroups);
            Map<String, IPhase> phases = PhaseFactory.getPhases(root, cellGroups, allBaseCells);

            return new PhaseMachine(phases, START, settings);
        } catch (Exception e) {
            throw new XMLException(e, Factory.MISSING_ERROR + "," + RULES_TYPE);
        }
    }

    private static Map<String, ICell> getAllCells(Map<String, ICellGroup> cellGroupMap) {
        Map<String, ICell> allBaseCells = new HashMap<>();
        for (Map.Entry<String, ICellGroup> e: cellGroupMap.entrySet()) {
            allBaseCells.putAll(e.getValue().getCellMap());
        }
        return allBaseCells;
    }

}
