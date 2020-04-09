package ooga.view;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ooga.cardtable.*;
import ooga.data.rules.ILayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestDisplayTable extends Application {

    // TODO: problem will be when need to drag a display cell which points to other display cells
    // TODO: ^^ can't assume all children move every time, need to ask back end? Or maybe can assume

    private Pane myCenterPane;
    private ILayout myLayout;

    @Override public void start(Stage primaryStage) {

        DisplayTable gameTable = new DisplayTable();
        myCenterPane = gameTable.getPane();

        Scene scene = new Scene(myCenterPane, 500, 500, false);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}

