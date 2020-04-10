package ooga.data.style;

public class Coordinate implements ICoordinate{

    private double myX;
    private double myY;

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
     * Gives the Y coordinate relative to the screen height
     *
     * @return the y coordinate
     */
    @Override
    public double getY() {
        return myY;
    }
}
