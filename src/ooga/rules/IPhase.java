package ooga.rules;

import java.util.List;
import java.util.Map;
import ooga.cardtable.IMove;

/**
 * Gets Phase rules for each cell Gives Table cells with rules implemented Gives Table state machine
 * to process turns/winning/etc.
 */
public interface IPhase {

  /**
   * Retrieves whether the current phase is automatic, in which it proceeds to the next phase on its own.
   *
   * @return whether phase is automatic
   */
  boolean isAutomatic();

  /**
   * Associates a move with the rule it invokes
   *
   * @param move the user's movement of cards
   * @return the rules associated with the move
   */
  IRule identifyMove(IMove move);

  /**
   * Retrieves the list of available rules for a phase
   *
   * @return the list of rules
   */
  List<IRule> getRuleList();

  /**
   * Retrieves the map of available rules to lists of generic card actions that result
   *
   * @return a map of rules to action lists
   */
  Map<IRule, List<ICardAction>> getConditionalActions();

  /**
   * Retrieves the card actions that progress automatically at the start of a phase
   *
   * @return a list of automatic card actions
   */

  List<ICardAction> getAutoActions();

  /**
   * Executes the automatic actions for the phase and returns the GameState
   *
   * @return the updated GameState for the frontend
   */
  IGameState executeAutomaticActions();

}
