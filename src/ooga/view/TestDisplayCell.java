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

    public void start(Stage primaryStage) {
        Pane mainPane = new Pane();
        Scene scene = new Scene(mainPane, 500, 500, false);
        primaryStage.setScene(scene);
        primaryStage.show();

        Card testCard1 = new Card(); // automatically facedown, unknown card
        //Card testCard2 = new Card(); // automatically facedown, unknown cord
        List<ICard> testCards = List.of(testCard1);
        Deck testDeck = new Deck("testDeck", testCards);
        Cell testCell = new Cell("testCell", testDeck);

        Map<String, String> cardNameToFileName = Map.of("Unknown Card", "acehearts.png", "faceDown", "twohearts.png");
        Point2D location = new Point2D(100,200);
        double height = 100;
        double width = 80;
        double offset = 10;

        DisplayCell testDispCell = new DisplayCell(testCell, cardNameToFileName, location, height, width, offset);

        mainPane.getChildren().add(testDispCell.getImageView());
        //mainPane.getChildren().addAll(List.of(testDispCell.getImageView()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}