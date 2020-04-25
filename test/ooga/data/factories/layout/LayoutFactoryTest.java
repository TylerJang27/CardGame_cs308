package ooga.data.factories.layout;

import ooga.data.style.Coordinate;
import ooga.data.style.ICoordinate;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LayoutFactoryTest {

    @Test
    void createLayout() {
        Map<String, ICoordinate> cellCoords = new HashMap<>();
        Map<String, Integer> numberSettings = new HashMap<>();
        Map<String, String> cardImages = new HashMap();
        cellCoords.put("cpu", new Coordinate(50, 15));
        cellCoords.put("center", new Coordinate(50, 33));
        cellCoords.put("player1", new Coordinate(20, 65));
        cellCoords.put("playerc", new Coordinate(50, 65));
        cellCoords.put("playerr", new Coordinate(80, 65));

        List<String> cardList = null;
    }
}