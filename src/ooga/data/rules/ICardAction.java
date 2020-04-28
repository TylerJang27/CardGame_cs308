package ooga.data.rules;

import ooga.cardtable.IMove;

/**
 * This interface refers to the movement and updates that will occur upon processing of a move.
 *
 * @author Tyler Jang
 */
public interface ICardAction {

  /**
   * Executes the ICardAction, moving the ICells based on move.
   *
   * @param move an IMove to be processed into ICell movements.
   */
  void execute(IMove move);
}
