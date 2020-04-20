package ooga.data.factories;
import ooga.cardtable.ICell;
import ooga.cardtable.IDeck;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.rules.*;
import org.w3c.dom.Element;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * This PhaseMachineFactory implements Factory and constructs a PhaseMachine using the getPhaseMachine() method.
 * This PhaseMachine is used to govern the flow of rules, actions, and moves within a card game.
 * <p>
 * This Factory is dependent on CellGroupFactory, DeckFactory, MasterRuleFactory, and PhaseFactory functioning properly.
 *
 * @author Tyler Jang, Andrew Krier
 */
public class PhaseMachineFactory implements Factory {
    public static String RULES_TYPE = IPhaseMachine.DATA_TYPE;
    private static final String RULES = "rules";
    private static final String RESOURCES = "ooga.resources";
    public static final String RESOURCE_PACKAGE = RESOURCES + "." + RULES + "_";

    public static final String START = "INIT_PHASE";

    public static IPhaseMachine getPhaseMachine(File dataFile) {
        try {
            Element root = XMLHelper.getRootAndCheck(dataFile, RULES_TYPE, INVALID_ERROR);
            ISettings settings = SettingsFactory.getSettings(root);
            IDeck deck = DeckFactory.getDeck(root);
            Map<String, ICellGroup> cellGroups = CellGroupFactory.getCellGroups(root);
            Map<String, ICell> allBaseCells = getAllCells(cellGroups);
            Map<String, IPhase> phases = PhaseFactory.getPhases(root, cellGroups, allBaseCells);

            return new PhaseMachine(phases, START, settings, deck);
        } catch (Exception e) {
            throw new XMLException(e, Factory.MISSING_ERROR + "," + RULES_TYPE);
        }
    }

    /**
     * Returns a Map of String cell names to all cells using a Map of Strings to cell groups.
     *
     * @param cellGroupMap a Map of cell group names to cell groups
     * @return a Map of cell names to cells
     */
    private static Map<String, ICell> getAllCells(Map<String, ICellGroup> cellGroupMap) {
        Map<String, ICell> allBaseCells = new HashMap<>();
        for (Map.Entry<String, ICellGroup> e : cellGroupMap.entrySet()) {
            allBaseCells.putAll(e.getValue().getCellMap());
        }
        return allBaseCells;
    }

}
