package ooga.view.gamescreen;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import ooga.cardtable.ICell;
import ooga.data.rules.Layout;
import ooga.view.View;

import java.util.Map;
import java.util.ResourceBundle;

public class GameScreen {

    private DisplayTable myDisplayTable;
    private Dashboard myDashboard;
    private Header myHeader;
    private BorderPane myBorderPane;

    private Scene myScene;

    public GameScreen(View.TriggerMove moveLambda, Layout layout, double screenWidth, String theme, Button backButton, Button restartButton, String game, String scoreLabel, String language) {

        // Current default is standard, can change
        String skinType = "classic";
        ResourceBundle cardskins = ResourceBundle.getBundle("ooga.resources.decks.supportedthemes");
        for (String the : cardskins.getString("standard").split(",")) {
            if (theme.toLowerCase().equals(the.toLowerCase())) {
                skinType = theme;
                break;
            }
        }

        myDisplayTable = new DisplayTable(moveLambda, (Layout) layout, 650, skinType);
        myDashboard = new Dashboard(backButton, restartButton, scoreLabel, ResourceBundle.getBundle("ooga.resources.languages.messages."+language), game);
        myHeader = new Header();

        myBorderPane = new BorderPane();
        myBorderPane.setTop(myHeader.getPane());
        myBorderPane.setCenter(myDisplayTable.getPane());
        myBorderPane.setBottom(myDashboard.getPane());

        myScene = new Scene(myBorderPane,650,500);
        myScene.getStylesheets().add(getClass().getResource("/ooga/resources/skins/"+theme.toLowerCase()+"/gametable.css").toExternalForm()); //

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

    public Scene getScene() {
        return myScene;
    }

    public DisplayTable getDisplayTable() {
        return myDisplayTable;
    }

}
