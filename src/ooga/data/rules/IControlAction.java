package ooga.data.rules;

import ooga.cardtable.IPlayer;

/**
 * This interface stores information about phase changes and control updates upon successful
 * validation of an IMasterRule.
 *
 * @author Tyler Jang
 */
public interface IControlAction {

  /**
   * Executes the IControlAction, updating player score and returning the IControlAction's
   * IPhaseArrow.
   *
   * @param player the player to which score adjustments should be made
   * @return the IPhaseArrow for the IControlAction
   */
  IPhaseArrow execute(IPlayer player);

}
