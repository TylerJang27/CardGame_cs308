package ooga.data.rules;

import ooga.cardtable.IMove;

/**
 * This interface governs all conditional classes that must be validated in order for moves to be
 * processed.
 *
 * @author Tyler Jang
 */
public interface IRule {

  /**
   * Validates the IMove.
   *
   * @param move the IMove to validate
   * @return whether or not the move is valid for this IRule
   */
  boolean checkValidMove(IMove move);

  /**
   * Retrieves the name of the IRule.
   *
   * @return the IRule's name
   */
  String getName();
}
