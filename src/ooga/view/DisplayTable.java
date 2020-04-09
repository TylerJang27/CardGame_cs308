package ooga.view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ooga.data.rules.ILayout;

public class DisplayTable extends Application {

    // TODO: function that takes in one cell and converts to one display cell, for rendering/updates
    // TODO: function which calls convertToDisplay(), then adds the imageview object to screen....
    // TODO: problem will be when need to drag a display cell which points to other display cells
    // TODO: ^^ can't assume all children move every time, need to ask back end? Or maybe can assume

    private String myGameName;
    private ILayout myLayout;
    private Color myTableColor;

    private final int DEFAULT_SCENE_WIDTH = 800;
    private final int DEFAULT_SCENE_HEIGHT = 600;

    public DisplayTable(ILayout inLayout) {
        //ILayout inLayout
        //myLayout = inLayout;
        myGameName =  "Practice Game";
        String tableColor = "0x0000FF";
        myTableColor = Color.web(tableColor);
    }

    @Override public void start(Stage stage) {
        stage.setTitle(myGameName);
        Group root = new Group();
        Scene scene = new Scene(root, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);
        scene.setFill(myTableColor);

    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}

