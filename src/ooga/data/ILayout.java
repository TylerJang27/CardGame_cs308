package ooga.data;

public interface ILayout {
    /**
     * Gives a map of all the cells in a game
     * with their respective names as their keys
     * @return
     */
    Map<String, ICoordinate> getCellLayout();

    /**
     * Gives screen width
     * @return
     */
    double getScreenWidth();

    /**
     * Gives screen height
     * @return
     */
    double getScreenHeight();
}
