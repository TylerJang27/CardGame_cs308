package ooga.view.gamescreen;

import java.util.function.Consumer;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import ooga.cardtable.ICell;
import ooga.controller.Controller.GiveMove;
import ooga.data.style.Layout;
import ooga.view.View;

import java.util.Map;
import java.util.ResourceBundle;
import ooga.view.View.SaveGame;
import ooga.view.menu.Dictionary;

public class GameScreen {
    private static final String SKIN_TYPE = "classic";
    private static final String CARD_RESOURCE_BUNDLE = "ooga.resources.decks.supportedthemes";
    private static final ResourceBundle DEFAULT_CARD_BUNDLE = ResourceBundle.getBundle(CARD_RESOURCE_BUNDLE);

    private DisplayTable myDisplayTable;
    private Dashboard myDashboard;
    private Header myHeader;
    private BorderPane myBorderPane;

    public GameScreen(int gameID, GiveMove moveLambda, Layout layout, double screenWidth, String theme, Button restartButton, String game, String scoreLabel, String language, SaveGame saveGame) {
        String skinType = SKIN_TYPE;
        for (String the : DEFAULT_CARD_BUNDLE.getString("standard").split(",")) {
            if (theme.toLowerCase().equals(the.toLowerCase())) {
                skinType = theme;
                break;
            }
        }
        Consumer<String> dashboardSave = fileName -> {
            saveGame.saveGame(gameID, fileName);
        };

        myDisplayTable = new DisplayTable(gameID,moveLambda, (Layout) layout, 650, skinType);
        Dictionary.getInstance().addReference("ooga.resources.languages.messages");
        myDashboard = new Dashboard(restartButton, scoreLabel, game, dashboardSave);
        myHeader = new Header();

        myBorderPane = new BorderPane();
        myBorderPane.setTop(myHeader.getPane());
        myBorderPane.setCenter(myDisplayTable.getPane());
        myBorderPane.setBottom(myDashboard.getPane());

        //myScene = new Scene(myBorderPane,650,500);
        //myScene.getStylesheets().add(getClass().getResource("/ooga/resources/skins/"+theme.toLowerCase()+"/gametable.css").toExternalForm()); //

    }

    public void displayMessage(String text){myHeader.playMessage(text);}

    public void updateScore(double score) {
        myDashboard.updateScore(score);
    }

    public void initializeTable(Map<String,ICell> cellData) {
        myDisplayTable.updateCells(cellData);
    }

    public void updateTable(Map<String, ICell> cellData) {
        myDisplayTable.updateTheseCells(cellData);
    }

    public Node getNode(){
        return myBorderPane;
    }

    public DisplayTable getDisplayTable() {
        return myDisplayTable;
    }

}
