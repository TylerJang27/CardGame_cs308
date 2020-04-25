package ooga.data.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ooga.cardtable.ICell;
import ooga.cardtable.IGameState;
import ooga.cardtable.IMove;
import ooga.cardtable.IPlayer;

/**
 * This class implements IPhase and governs an individual state of the FSM that is the
 * IPhaseMachine. Has individual IMasterRules, governing the different possible changes available
 * during the Phase.
 *
 * @author Maverick Chung, Tyler Jang
 */
public class Phase implements IPhase {

  private String myName;

  private List<IMasterRule> myRules;
  private List<String> validDonorNames;
  private Map<String, ICellGroup> myCellGroupMap;
  private Map<String, ICell> myCellMap;
  private boolean isAuto;
  private IGameState myGameState;

  /**
   * The Constructor for Phase, settings its various properties.
   *
   * @param name         the String name of the Phase
   * @param ruleList     the List of IMasterRules to validate
   * @param validDonors  the List of String ICell valid donor names
   * @param cellGroupMap the Map of String ICellGroup names to ICellGroups
   * @param cellMap      the Map of String ICell names to ICells
   * @param automatic    boolean of whether or not the IPhase is automatic
   */
  public Phase(String name, List<IMasterRule> ruleList, List<String> validDonors,
      Map<String, ICellGroup> cellGroupMap, Map<String, ICell> cellMap, boolean automatic) {
    myName = name;
    myRules = ruleList;
    validDonorNames = validDonors;
    myCellGroupMap = cellGroupMap;
    myCellMap = cellMap;
    isAuto = automatic;
  }

  /**
   * Retrieves whether the current phase is automatic, in which it proceeds to the next phase on its
   * own.
   *
   * @return whether phase is automatic
   */
  @Override
  public boolean isAutomatic() {
    return isAuto;
  }

  /**
   * Retrieves what IMasterRule, if any, the IMove pertains to
   *
   * @param move the IMove to process
   * @return the relevant IMasterRule or null
   */
  private IMasterRule identifyMove(IMove move) {
    for (IMasterRule r : myRules) {
      if (r.checkValidMove(move)) {
        return r;
      }
    }
    return null;
  }

  /**
   * Executes the given move, checking its logic against available IMasterRules and processing its
   * changes.
   *
   * @param move   the IMove to validate
   * @param player the player to add points to
   * @return an IPhaseArrow representing the necessary phase update
   */
  @Override
  public IPhaseArrow executeMove(IMove move, IPlayer player) {
    IMasterRule ruleToExecute = identifyMove(move);
    if (ruleToExecute != null && !isAuto) {
      myGameState = ruleToExecute.executeMove(move);
      IPhaseArrow ab = ruleToExecute.executeAutoActions(player, move);
      return ab;
    }
    return null;
  }

  /**
   * Retrieves the list of available rules for an IPhase.
   *
   * @return the list of rules
   */
  @Override
  public List<IMasterRule> getRuleList() {
    return new ArrayList<>(myRules);
  }

  /**
   * Executes the automatic and control actions for the IPhase.
   *
   * @param player the IPlayer to update
   * @param move   the previous IMove
   * @return the IPhaseArrow referring to the appropriate phase change.
   */
  @Override
  public IPhaseArrow executeAutomaticActions(IPlayer player, IMove move) {
    IPhaseArrow lastArrow = new PhaseArrow(myName, "", myName);
    for (IMasterRule rule : myRules) {
      lastArrow = rule.executeAutoActions(player, move);
      if (lastArrow != null) {
        return lastArrow;
      }
    }
    return lastArrow;
  }

  /**
   * Retrieves the ICell Map stored by this IPhase.
   *
   * @return the Map of String ICell names to ICells
   */
  @Override
  public Map<String, ICell> getMyCellMap() {
    return myCellMap;
  }

  /**
   * Retrieves the ICellGroup Map stored by this IPhase.
   *
   * @return the Map of String ICellGroup names to ICellGroups
   */
  @Override
  public Map<String, ICellGroup> getMyCellGroupMap() {
    return myCellGroupMap;
  }

  /**
   * Retrieves the name of the IPhase.
   *
   * @return the IPhase's name
   */
  @Override
  public String getMyName() {
    return myName;
  }

  /**
   * Returns whether or not the ICell in question is a valid donor based on the rules of the
   * IPhase.
   *
   * @param cell the ICell to validate
   * @return whether or not cell is a valid donor
   */
  @Override
  public boolean isValidDonor(ICell cell) {
    return validDonorNames.contains(cell.getName().split(",")[0]);
  }
}
