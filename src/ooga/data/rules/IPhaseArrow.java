package ooga.data.rules;

/**
 * Holds a transition of phases from start to end, with a move.
 *
 * @author Maverick Chung
 */
public interface IPhaseArrow {

  /**
   * Retrieves the starting phase name
   *
   * @return the starting phase name for an arrow
   */
  String getStartPhaseName();

  /**
   * Retrieves the ending phase name
   *
   * @return the ending phase name for an arrow
   */
  String getEndPhaseName();

  /**
   * Retrieves the name of the phase arrow move
   *
   * @return the move name for the arrow
   */
  String getMoveName();

  @Override
  String toString();
}
