package ooga.data.rules;

import java.util.List;
import ooga.cardtable.GameState;
import ooga.cardtable.IGameState;
import ooga.cardtable.IMove;
import ooga.cardtable.IPlayer;
import ooga.data.XMLException;
import ooga.data.factories.Factory;

/**
 * This class implements IMasterRule and controls all of the logic for an IMove. Each IPhase has
 * several MasterRules, each of which govern their own conditional trees and appropriate actions.
 *
 * @author Tyler Jang
 */
public class MasterRule implements IMasterRule {

  private List<IRule> myRules;
  private List<IRule> myAutoRules;
  private List<ICardAction> myCardActions;
  private List<IControlAction> myControlActions;
  private String MASTER_RULE = "This is a Master Rule";

  /**
   * Constructor for MasterRule. Sets all the rules and all of the actions.
   *
   * @param rules          a List of IRules to be validated for a move
   * @param autoRules      a List of IRule conditions to be validated
   * @param cardActions    a List of ICardActions to be activated upon validation of the rules
   * @param controlActions a List of IControlActions to be activated upon validation of the auto
   *                       rules
   */
  public MasterRule(List<IRule> rules, List<IRule> autoRules, List<ICardAction> cardActions,
      List<IControlAction> controlActions) {
    myRules = rules;
    myAutoRules = autoRules;
    myCardActions = cardActions;
    myControlActions = controlActions;
  }

  /**
   * Executes a move, validating it and updating card actions.
   *
   * @param move the IMove to be processed
   * @return the new IGameState
   */
  @Override
  public IGameState executeMove(IMove move) {
    if (checkValidMove(move)) {
      for (ICardAction action : myCardActions) {
        action.execute(move);
      }
    }
    return GameState.WAITING;
  }

  /**
   * Executes the automatic actions associated with the IMasterRule.
   *
   * @param player the player to which points and adjustments should be made
   * @param move   the IMove to be processed
   * @return an IPhaseArrow containing the current phase and the new phase to be updated to
   */
  @Override
  public IPhaseArrow executeAutoActions(IPlayer player, IMove move) {
    if (checkAutoRules(move)) {
      IPhaseArrow lastArrow = null;
      for (IControlAction action : myControlActions) {
        lastArrow = action.execute(player);
      }
      return lastArrow;
    }
    return null;
  }

  /**
   * Validates the auto rules based on a move.
   *
   * @param move the IMove to validate
   * @return whether or not control changes should be processed accordingly
   */
  @Override
  public boolean checkAutoRules(IMove move) {
    boolean flag = true;
    try {
      for (IRule rule : myAutoRules) {
        if (!rule.checkValidMove(move)) {
          return false;
        }
      }
    } catch (NullPointerException e) {
      throw new XMLException(e, Factory.CONTROL_ERROR);
    }
    return flag;
  }

  /**
   * Validates the IMove.
   *
   * @param move the IMove to validate
   * @return whether or not the move is valid for this IRule
   */
  @Override
  public boolean checkValidMove(IMove move) {
    boolean flag = true;
    for (IRule rule : myRules) {
      if (!rule.checkValidMove(move)) {
        return false;
      }
    }
    return flag;
  }

  /**
   * Retrieves the name of the IRule.
   *
   * @return the IRule's name
   */
  @Override
  public String getName() {
    return MASTER_RULE;
  }
}
