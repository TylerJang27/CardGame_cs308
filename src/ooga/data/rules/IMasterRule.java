package ooga.data.rules;

import ooga.cardtable.IGameState;
import ooga.cardtable.IMove;
import ooga.cardtable.IPlayer;

/**
 * Interface for evaluating rules for a given phase, based on generic and specific rules. This
 * interface extends IRule.
 *
 * @author Tyler Jang
 */
public interface IMasterRule extends IRule {

  //boolean checkValidAcceptor(ICell cell);

  //boolean checkValidDonor(ICell cell);

  /**
   * Executes a move, validating it and updating card actions.
   *
   * @param move the IMove to be processed
   * @return the new IGameState
   */
  IGameState executeMove(IMove move);

  /**
   * Executes the automatic actions associated with the IMasterRule.
   *
   * @param player the player to which points and adjustments should be made
   * @param move   the IMove to be processed
   * @return an IPhaseArrow containing the current phase and the new phase to be updated to
   */
  IPhaseArrow executeAutoActions(IPlayer player, IMove move);

  /**
   * Validates the auto rules based on a move.
   *
   * @param move the IMove to validate
   * @return whether or not control changes should be processed accordingly
   */
  boolean checkAutoRules(IMove move);

  //boolean checkValidTransfer(ICell don, ICell rec);

  //ICellRegex getAcceptorRegex();

  //ICellRegex getDonorRegex();

  //ICellRegex getTransferRegex();
}
