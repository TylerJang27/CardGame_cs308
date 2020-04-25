package ooga.data.style;

/**
 * This interface stores the cell's location relative to the screen width.
 *
 * @author Andrew Krier
 */
public interface ICoordinate {

  /**
   * Gives the X coordinate relative to the screen width
   *
   * @return the x coordinate
   */
  double getX();

  /**
   * Gives the Y coordinate relative to the screen width
   *
   * @return the y coordinate
   */
  double getY();
}
