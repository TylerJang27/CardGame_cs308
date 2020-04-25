package ooga.data.factories.layout;

import static org.junit.jupiter.api.Assertions.*;

class LayoutFactoryTest {

    @Test
    void createLayout() {
        Map<String, ICoordinates> cellCoords = new HashMap<>();
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