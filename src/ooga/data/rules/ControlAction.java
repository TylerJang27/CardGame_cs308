package ooga.data.rules;

import ooga.cardtable.IPlayer;

/**
 * This class implements IControlAction and stores information about what control flow changes
 * should occur upon successful validation of an IMasterRule.
 *
 * @author Tyler Jang
 */
public class ControlAction implements IControlAction {

  IPhaseArrow myArrow;
  double myVal;

  /**
   * Constructor for ControlAction, taking in an IPhaseArrow and point value.
   *
   * @param arrow    an IPhaseArrow representing the phase change that should occur.
   * @param pointVal a point value to award a player.
   */
  public ControlAction(IPhaseArrow arrow, double pointVal) {
    myArrow = arrow;
    myVal = pointVal;
  }

  /**
   * Executes the IControlAction, updating player score and returning the IControlAction's
   * IPhaseArrow.
   *
   * @param player the player to which score adjustments should be made
   * @return the IPhaseArrow for the IControlAction
   */
  @Override
  public IPhaseArrow execute(IPlayer player) {
    if (player != null) {
      player.setScore(player.getScore() + myVal);
    }
    return myArrow;
  }
}
