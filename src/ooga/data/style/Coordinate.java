package ooga.data.style;

/**
 * This class holds information on where a cell is, with sizes given relative to the screen width.
 *
 * @author Andrew Krier, Tyler Jang
 */
public class Coordinate implements ICoordinate {

  private double myX;
  private double myY;

  /**
   * The Constructor for Coordinate
   *
   * @param x the x-location of the Cell, relative to the screen width
   * @param y the y-location of the Cell, relative to the screen width
   */
  public Coordinate(double x, double y) {
    myX = x;
    myY = y;
  }

  /**
   * Gives the X coordinate relative to the screen width
   *
   * @return the x coordinate
   */
  @Override
  public double getX() {
    return myX;
  }

  /**
   * Gives the Y coordinate relative to the screen width
   *
   * @return the y coordinate
   */
  @Override
  public double getY() {
    return myY;
  }
}
