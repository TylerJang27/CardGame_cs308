package ooga.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ooga.data.rules.ILayout;

public class TestDisplayTable extends Application {

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

