/*import ooga.data.style.Coordinate;
import ooga.data.style.ICoordinate;
import ooga.data.style.Layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A dummy implementation of Layout, used to hard code Layout tests.
 *
 * @author Andrew Krier
 *//*
public class LayoutDummy extends Layout {

    private static final int SCREEN_WIDTH = 100;

    private Map<String, ICoordinate> cellLayout;

    private int numPlayers = 1;

    private String gameName = "Solitaire";

    private double screenRatio = 0.75;
    private double cardWidthRatio = 0.1;
    private double cardHeightRatio = 0.14;
    private double downOffsetRatio = 0.02;
    private double upOffsetRatio = 0.04;

    private static final String PLAYERS = "players";
    private static final String SCREEN_HEIGHT = "screen_height";
    private static final String CARD_WIDTH = "card_width";
    private static final String CARD_HEIGHT = "card_height";
    private static final String FACE_DOWN_OFFSET = "face_down_offset";
    private static final String FACE_UP_OFFSET = "face_up_offset";

    public LayoutDummy() {
        super();
        initializeMap();
    }

    public LayoutDummy(String xmlfile, String game, Map<String, ICoordinate> cellCoords, Map<String, Integer> numberSettings) {
        super(cellCoords, numberSettings, null);

        gameName = game;

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
     *//*
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
     *//*
    @Override
    public double getScreenWidth() {
        return 0;
    }

    /**
     * Gives screen height
     *
     * @return the double screen height
     *//*
    @Override
    public double getScreenHeight() {
        return 0;
    }

    /**
     * Gives the ratio between the card height and width
     * ratio = height / width
     *
     * @return
     *//*
    @Override
    public double getCardRatio() {
        return 0;
    }

    private Map<String, ICoordinate> initializeMap() {

        Map<String, ICoordinate> ret = new HashMap<>();

        int[][] coords = {
                {11, 10},
                {24, 10},
                {50, 10},
                {63, 10},
                {76, 10},
                {89, 10},
                {11, 27},
                {24, 27},
                {37, 27},
                {50, 27},
                {63, 27},
                {76, 27},
                {89, 27},
        };
        ArrayList<String> names = new ArrayList(Arrays.asList("deck", "hand", "club", "diamond", "heart", "spade", "1", "2", "3", "4", "5", "6", "7"));

        for (int i = 0; i < names.size(); i++) {
            ret.put(names.get(i), new Coordinate(coords[i][0], coords[i][1]));
        }

        return ret;
    }
}
