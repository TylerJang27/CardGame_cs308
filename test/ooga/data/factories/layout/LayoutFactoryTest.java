package ooga.data.factories.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ooga.data.factories.LayoutFactory;
import ooga.data.style.Coordinate;
import ooga.data.style.ICoordinate;
import ooga.data.style.ILayout;
import ooga.data.style.Layout;
import org.junit.jupiter.api.Test;

class LayoutFactoryTest {

  private static final String TEST_DIRECTORY = "test/ooga/data/factories/layout/";

  @Test
  void createLayout() {
    Map<String, ICoordinate> cellCoords = new HashMap<>();
    Map<String, String> cardImages = new HashMap();
    cellCoords.put("playerl", new Coordinate(20, 65));
    cellCoords.put("center", new Coordinate(50, 33));
    cellCoords.put("playerr", new Coordinate(80, 65));
    cellCoords.put("cpu", new Coordinate(50, 15));
    cellCoords.put("playerc", new Coordinate(50, 65));

    List<String> cardList = Arrays.asList(
        "rock",
        "rock",
        "rock",
        "rock",
        "rock",
        "paper",
        "paper",
        "paper",
        "paper",
        "paper",
        "scissors",
        "scissors",
        "scissors",
        "scissors",
        "scissors",
        "D0",
        "D0",
        "D0",
        "D0",
        "faceDown");

    for (String s : cardList) {
      cardImages.put(s, "rps/" + s + ".png");
    }

    Map<String, Integer> numberSettings = new HashMap<>() {{
      put("players", 1);
      put("screen_height", 100);
      put("card_width", 10);
      put("card_height", 14);
      put("face_down_offset", 14);
      put("face_up_offset", 14);
    }};

    ILayout expected1 = new Layout(cellCoords, numberSettings, cardImages);
    ILayout actual1 = LayoutFactory.createLayout(new File(TEST_DIRECTORY + "layout1.xml"));

    assertEquals(actual1.getCardHeightRatio(), expected1.getCardHeightRatio());
    assertEquals(actual1.getCardImagePaths(), expected1.getCardImagePaths());
    assertEquals(actual1.getCardRatio(), expected1.getCardRatio());
    assertEquals(actual1.getCardImagePaths(), expected1.getCardImagePaths());
    assertEquals(actual1.getCardWidthRatio(), expected1.getCardWidthRatio());
    assertEquals(actual1.getCellLayout().get("cpu").getX(),
        expected1.getCellLayout().get("cpu").getX());
    assertEquals(actual1.getCellLayout().get("playerr").getX(),
        expected1.getCellLayout().get("playerr").getX());
    assertEquals(actual1.getCellLayout().get("center").getX(),
        expected1.getCellLayout().get("center").getX());
    assertEquals(actual1.getCellLayout().get("playerl").getX(),
        expected1.getCellLayout().get("playerl").getX());
    assertEquals(actual1.getCellLayout().get("playerc").getX(),
        expected1.getCellLayout().get("playerc").getX());
    assertEquals(actual1.getCellLayout().get("cpu").getY(),
        expected1.getCellLayout().get("cpu").getY());
    assertEquals(actual1.getCellLayout().get("playerr").getY(),
        expected1.getCellLayout().get("playerr").getY());
    assertEquals(actual1.getCellLayout().get("center").getY(),
        expected1.getCellLayout().get("center").getY());
    assertEquals(actual1.getCellLayout().get("playerl").getY(),
        expected1.getCellLayout().get("playerl").getY());
    assertEquals(actual1.getCellLayout().get("playerc").getY(),
        expected1.getCellLayout().get("playerc").getY());
    assertEquals(actual1.getDownOffsetRatio(), expected1.getDownOffsetRatio());
    assertEquals(actual1.getNumPlayers(), expected1.getNumPlayers());
    assertEquals(actual1.getScreenHeight(), expected1.getScreenHeight());
    assertEquals(actual1.getScreenRatio(), expected1.getScreenRatio());
    assertEquals(actual1.getScreenWidth(), expected1.getScreenWidth());
    assertEquals(actual1.getUpOffsetRatio(), expected1.getUpOffsetRatio());
  }
}