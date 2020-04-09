package ooga.data.rules;

import ooga.data.style.ICoordinate;

import java.util.Map;

public class Layout implements ILayout {

    private static final int SCREEN_WIDTH = 100;

    private Map<String, ICoordinate> cellLayout;

    private int numPlayers;

    private double screenRatio;
    private double cardWidthRatio;
    private double cardHeightRatio;
    private double downOffsetRatio;
    private double upOffsetRatio;

    // TODO: Check if frontend wants card height relative to screen or card width

    private static final String PLAYERS = "players";
    private static final String SCREEN_HEIGHT = "screen_height";
    private static final String CARD_WIDTH = "card_width";
    private static final String CARD_HEIGHT = "card_height";
    private static final String FACE_DOWN_OFFSET = "face_down_offset";
    private static final String FACE_UP_OFFSET = "face_up_offset";

    public Layout(String xmlfile, Map<String, ICoordinate> cellCoords, Map<String, Integer> numberSettings) {
        cellLayout = cellCoords;

        numPlayers = numberSettings.get(PLAYERS);

        screenRatio = numberSettings.get(SCREEN_HEIGHT) / SCREEN_WIDTH;
        cardWidthRatio = numberSettings.get(CARD_WIDTH) / SCREEN_WIDTH;
        cardHeightRatio = numberSettings.get(CARD_HEIGHT) / SCREEN_WIDTH;
        downOffsetRatio = numberSettings.get(FACE_DOWN_OFFSET) / SCREEN_WIDTH;
        upOffsetRatio = numberSettings.get(FACE_UP_OFFSET) / SCREEN_WIDTH;

    }

    /**
     * Gives a map of all the cells in a game
     * with their respective names as their keys
     *
     * @return a map of of cell names to their coordinates, given as decimals relative to the screen size
     */
    @Override
    public Map<String, ICoordinate> getCellLayout() {
        return Map.copyOf(cellLayout);
    }

    // TODO: Add all of these to the External API and give them documentation

    public int getNumPlayers() {
        return numPlayers;
    }

    public double getScreenRatio() {
        return screenRatio;
    }

    public double getCardWidthRatio() {
        return cardWidthRatio;
    }

    public double getCardHeightRatio() {
        return cardHeightRatio;
    }

    public double getDownOffsetRatio() {
        return downOffsetRatio;
    }

    public double getUpOffsetRatio() {
        return upOffsetRatio;
    }

    /**
     * Gives screen width
     *
     * @return the double screen width
     */
    @Override
    public double getScreenWidth() {
        return 0;
    }

    /**
     * Gives screen height
     *
     * @return the double screen height
     */
    @Override
    public double getScreenHeight() {
        return 0;
    }

    /**
     * Gives the ratio between the card height and width
     * ratio = height / width
     *
     * @return
     */
    @Override
    public double getCardRatio() {
        return 0;
    }
}
