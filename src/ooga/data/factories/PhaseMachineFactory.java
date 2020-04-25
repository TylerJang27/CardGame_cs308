package ooga.data.factories;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import ooga.cardtable.ICell;
import ooga.cardtable.IDeck;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.XMLValidator;
import ooga.data.rules.ICellGroup;
import ooga.data.rules.IPhase;
import ooga.data.rules.IPhaseMachine;
import ooga.data.rules.ISettings;
import ooga.data.rules.PhaseMachine;
import org.w3c.dom.Element;

/**
 * This PhaseMachineFactory implements Factory and constructs an IPhaseMachine using the
 * createPhaseMachine() method. This IPhaseMachine is used to govern the flow of rules, actions, and
 * moves within a card game.
 * <p>
 * This Factory is dependent on CellGroupFactory, DeckFactory, MasterRuleFactory, and PhaseFactory
 * functioning properly.
 *
 * @author Tyler Jang, Andrew Krier
 */
public class PhaseMachineFactory implements Factory {

  public static String RULES_TYPE = IPhaseMachine.DATA_TYPE;
  private static final String RULES = "rules";
  private static final String RESOURCES = "ooga.resources";
  public static final String RESOURCE_PACKAGE = RESOURCES + "." + RULES + "_";

  private static final String RULES_XSD = "src/ooga/data/factories/schemas/rules_schema.xsd";

  public static final String START = "INIT_PHASE";

  private PhaseMachineFactory() {
  }

  /**
   * Builds and returns an IPhaseMachine built from a rules XML. Requirements for rules XML can be
   * found in doc/XML_Documentation.md.
   *
   * @param dataFile the file from which to build an IPhaseMachine implementation
   * @return an IPhaseMachine implementation built and initialized based on the rules XML
   */
  public static IPhaseMachine createPhaseMachine(File dataFile) {
    if (XMLValidator.validateXMLSchema(RULES_XSD, dataFile.getPath())) {
      try {
        Element root = XMLHelper.getRootAndCheck(dataFile, RULES_TYPE, INVALID_ERROR);
        ISettings settings = SettingsFactory.createSettings(root);
        IDeck deck = DeckFactory.createDeck(root);
        Map<String, ICellGroup> cellGroups = CellGroupFactory.createCellGroups(root);
        Map<String, ICell> allBaseCells = getAllCells(cellGroups);
        Map<String, IPhase> phases = PhaseFactory.createPhases(root, cellGroups, allBaseCells);

        return new PhaseMachine(phases, START, settings, deck);
      } catch (Exception e) {
        throw new XMLException(e, Factory.MISSING_ERROR + "," + RULES_TYPE);
      }
    } else {
      throw new XMLException(Factory.INVALID_ERROR);
    }
  }

  /**
   * Returns a Map of String cell names to all cells using a Map of Strings to cell groups.
   *
   * @param cellGroupMap a Map of cell group names to cell groups
   * @return a Map of cell names to cells
   */
  protected static Map<String, ICell> getAllCells(Map<String, ICellGroup> cellGroupMap) {
    Map<String, ICell> allBaseCells = new HashMap<>();
    for (Map.Entry<String, ICellGroup> e : cellGroupMap.entrySet()) {
      allBaseCells.putAll(e.getValue().getCellMap());
    }
    return allBaseCells;
  }

}
