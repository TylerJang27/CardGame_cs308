package ooga.data;

import java.util.Map;

public interface ILayout {
    /**
     * Gives a map of all the cells in a game
     * with their respective names as their keys
     * @return a map of of cell names to their coordinates, given as decimals relative to the screen size
     */
    Map<String, ICoordinate> getCellLayout();

    /**
     * Gives screen width
     * @return the double screen width
     */
    double getScreenWidth();

    /**
     * Gives screen height
     * @return the double screen height
     */
    double getScreenHeight();

    /**
     * Gives the ratio between the card height and width
     * ratio = height / width
     * @return
     */
    double getCardRatio();
}
