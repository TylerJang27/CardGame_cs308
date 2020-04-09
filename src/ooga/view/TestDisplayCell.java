package ooga.view;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ooga.cardtable.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDisplayCell extends Application {

    private Pane myMainPane;
    public void start(Stage primaryStage) {
        myMainPane = new Pane();
        Scene scene = new Scene(myMainPane, 500, 500, false);
        primaryStage.setScene(scene);
        primaryStage.show();

        Cell testCell = getDummyCell();

        Map<String, String> cardNameToFileName = Map.of("Unknown Card", "acehearts.png", "faceDown", "twohearts.png");
        Point2D location = new Point2D(100,200);
        double height = 100;
        double width = 80;
        double offset = 20;

        DisplayCell testDispCell = new DisplayCell(testCell, cardNameToFileName, location, height, width, offset);
        drawDisplayCells(testDispCell);

    }

    private Cell getDummyCell() {
        Card testCard = new Card(); // automatically facedown, unknown card
        List<ICard> testCards = List.of(testCard);
        Deck testDeck = new Deck("testDeck", testCards);
        Cell testCell = new Cell("testCell", testDeck);

        Card testAddCard = new Card(); // automatically facedown, unknown cord
        testCell.addCard(Offset.SOUTH,testAddCard);
        Card testAddAnotherCard = new Card();
        testCell.getAllChildren().get(Offset.SOUTH).addCard(Offset.SOUTH,testAddAnotherCard);
        return testCell;
    }

    private void drawDisplayCells(DisplayCell rootDispCell) {
        if (rootDispCell.getGroup().getChildren() == null) {
            return;
        }
        myMainPane.getChildren().addAll(rootDispCell.getGroup().getChildren());
        for (IOffset dir: rootDispCell.getCell().getAllChildren().keySet()) {
            if (dir == Offset.NONE) {
                continue;
            }
            drawDisplayCells(rootDispCell.getAllChildren().get((Offset) dir));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}