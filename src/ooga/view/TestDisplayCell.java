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
        List<ICard> testCards1 = List.of(testCard1);
        Deck testDeck1 = new Deck("testDeck", testCards1);
        Cell testCell1 = new Cell("testCell", testDeck1);

        Card testCard2 = new Card(); // automatically facedown, unknown cord
        List<ICard> testCards2 = List.of(testCard2);
        Deck testDeck2 = new Deck("testDeck", testCards2);
        Cell testCell2 = new Cell("testCell", testDeck2);

        Map<String, String> cardNameToFileName = Map.of("Unknown Card", "acehearts.png", "faceDown", "twohearts.png");
        Point2D location = new Point2D(100,200);
        double height = 100;
        double width = 80;
        double offset = 10;

        DisplayCell testDispCell1 = new DisplayCell(testCell1, cardNameToFileName, location, height, width, offset);
        DisplayCell testDispCell2 = new DisplayCell(testCell2, cardNameToFileName, location, height, width, offset);

        mainPane.getChildren().addAll(testDispCell1.getGroup().getChildren());
        mainPane.getChildren().addAll(testDispCell2.getGroup().getChildren());

    }

    public static void main(String[] args) {
        launch(args);
    }
}