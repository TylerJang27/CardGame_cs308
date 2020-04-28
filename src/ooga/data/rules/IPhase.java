package ooga.data.rules;

import java.util.List;
import java.util.Map;
import ooga.cardtable.ICell;
import ooga.cardtable.IMove;
import ooga.cardtable.IPlayer;

/**
 * Gets Phase rules for each cell. Gives Table cells with rules implemented Gives Table state
 * machine to process turns/winning/etc.
 *
 * @author Maverick Chung, Tyler Jang
 */
public interface IPhase {

  /**
   * Retrieves whether the current phase is automatic, in which it proceeds to the next phase on its
   * own.
   *
   * @return whether phase is automatic
   */
  boolean isAutomatic();

  /**
   * Executes the given move, checking its logic against available IMasterRules and processing its
   * changes.
   *
   * @param move   the IMove to validate
   * @param player the player to add points to
   * @return an IPhaseArrow representing the necessary phase update
   */
  IPhaseArrow executeMove(IMove move, IPlayer player);

  /**
   * Retrieves the list of available rules for an IPhase.
   *
   * @return the list of rules
   */
  List<IMasterRule> getRuleList();

  /**
   * Executes the automatic and control actions for the IPhase.
   *
   * @param player the IPlayer to update
   * @param move   the previous IMove
   * @return the IPhaseArrow referring to the appropriate phase change.
   */
  IPhaseArrow executeAutomaticActions(IPlayer player, IMove move);

  /**
   * Retrieves the ICell Map stored by this IPhase.
   *
   * @return the Map of String ICell names to ICells
   */
  Map<String, ICell> getMyCellMap();

  /**
   * Retrieves the ICellGroup Map stored by this IPhase.
   *
   * @return the Map of String ICellGroup names to ICellGroups
   */
  Map<String, ICellGroup> getMyCellGroupMap();

  //Map<IMasterRule, List<ICardAction>> getConditionalActions();

  //List<ICardAction> getAutoActions();

  //IGameState executeAutomaticActions();

  /**
   * Retrieves the name of the IPhase.
   *
   * @return the IPhase's name
   */
  String getMyName();

  //String getNextPhaseName(IMove move);

  //void setCellList(List<ICell> cells);

  //void addRule(IMasterRule rule, List<ICardAction> actions, String nextPhase);

  //void setAutoActions(List<ICardAction> actions);

  /**
   * Returns whether or not the ICell in question is a valid donor based on the rules of the
   * IPhase.
   *
   * @param cell the ICell to validate
   * @return whether or not cell is a valid donor
   */
  boolean isValidDonor(ICell cell);
}
