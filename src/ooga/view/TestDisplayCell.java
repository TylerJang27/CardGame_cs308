package ooga.view;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ooga.cardtable.*;
import java.util.List;

public class TestDisplayCell extends Application {

    // TODO: function that takes in one cell and converts to one display cell, for rendering/updates
    // TODO: function which calls convertToDisplay(), then adds the imageview object to screen....
    // TODO: problem will be when need to drag a display cell which points to other display cells
    // TODO: ^^ can't assume all children move every time, need to ask back end? Or maybe can assume
    // TODO: how to guarantee selected card during drag is rendered OVER rather than UNDER other cards

    public void start(Stage primaryStage) {
        Pane mainPane = new Pane();
        Scene scene = new Scene(mainPane, 500, 500, false);
        primaryStage.setScene(scene);
        primaryStage.show();

        Card testCard = new Card(); // automatically facedown
        List<ICard> testCards = List.of(testCard);
        Deck testDeck = new Deck("testDeck", testCards);
        Cell testCell = new Cell("testCell");

        DisplayCell testDispCell = new DisplayCell(testCell, "acehearts.png", "twohearts.png", new Point2D(100,200), 100, 80);

        mainPane.getChildren().add(testDispCell.getImageView());
    }

    public static void main(String[] args) {
        launch(args);
    }
}