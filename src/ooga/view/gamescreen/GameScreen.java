package ooga.view.gamescreen;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import ooga.cardtable.ICell;
import ooga.data.rules.Layout;
import ooga.view.View;

import java.util.Map;

public class GameScreen {

    private DisplayTable myDisplayTable;
    private Dashboard myDashboard;
    private Header myHeader;
    private BorderPane myBorderPane;

    private Scene myScene;

    public GameScreen(View.TriggerMove moveLambda, Layout layout, double screenWidth, String theme, Button backButton, String game) {

        myDisplayTable = new DisplayTable(moveLambda, (Layout) layout, 650, theme);
        myDashboard = new Dashboard(backButton);
        myHeader = new Header(game);

        myBorderPane = new BorderPane();
        myBorderPane.setTop(myHeader.getPane());
        myBorderPane.setCenter(myDisplayTable.getPane());
        myBorderPane.setBottom(myDashboard.getPane());

        myScene = new Scene(myBorderPane,650,500);
        myScene.getStylesheets().add(getClass().getResource("/ooga/resources/skins/"+theme.toLowerCase()+"/gametable.css").toExternalForm()); //

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
