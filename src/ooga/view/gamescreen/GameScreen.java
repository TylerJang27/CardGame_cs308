package ooga.view.gamescreen;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import ooga.cardtable.ICell;
import ooga.controller.Controller.GiveMove;
import ooga.data.style.ILayout;
import ooga.view.View.SaveGame;

public class GameScreen {

  private static final String SKIN_TYPE = "classic";
  private static final String CARD_RESOURCE_BUNDLE = "ooga.resources.decks.supportedthemes";
  private static final ResourceBundle DEFAULT_CARD_BUNDLE = ResourceBundle
      .getBundle(CARD_RESOURCE_BUNDLE);
  private static final String COMMA_REGEX = ",";
  private static final String STANDARD = "standard";
  private static final double SCREEN_WIDTH = 650.0;
  private static final List<String> GAME_CSS = List.of("gametable");


  private DisplayTable myDisplayTable;
  private Dashboard myDashboard;
  private Header myHeader;
  private BorderPane myBorderPane;

  public GameScreen(int gameID, GiveMove moveLambda, ILayout layout, double screenWidth,
      String theme, Button restartButton, String game, String scoreLabel, String language,
      SaveGame saveGame) {
    String skinType = SKIN_TYPE;
    for (String skin : DEFAULT_CARD_BUNDLE.getString(STANDARD).split(COMMA_REGEX)) {
      if (theme.toLowerCase().equals(skin.toLowerCase())) {
        skinType = theme;
        break;
      }
    }
    Consumer<String> dashboardSave = fileName -> saveGame.saveGame(gameID, fileName);

    myDisplayTable = new DisplayTable(gameID, moveLambda, layout, SCREEN_WIDTH, skinType);
    myDashboard = new Dashboard(restartButton, scoreLabel, game, dashboardSave);
    myHeader = new Header();

    myBorderPane = new BorderPane();
    myBorderPane.getStyleClass().addAll(GAME_CSS);
    myBorderPane.setTop(myHeader.getPane());
    myBorderPane.setCenter(myDisplayTable.getPane());
    myBorderPane.setBottom(myDashboard.getNode());
  }

  public void displayMessage(String text) {
    myHeader.playMessage(text);
  }

  public void updateScore(double score) {
    myDashboard.updateScore(score);
  }

  public void initializeTable(Map<String, ICell> cellData) {
    myDisplayTable.updateCells(cellData);
  }

  public void updateTable(Map<String, ICell> cellData) {
    myDisplayTable.updateTheseCells(cellData);
  }

  public Node getNode() {
    return myBorderPane;
  }
}
