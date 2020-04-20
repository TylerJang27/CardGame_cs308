package ooga.view.gamescreen;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class Dashboard {

    private Pane myPane;
    private HBox myBox;

    public Dashboard(Button backButton, Button restartButton) {
        myPane = new Pane();
        myBox = new HBox();
        myBox.getChildren().addAll(backButton, restartButton);
        myBox.getStyleClass().add("dashboard");
        myPane.getStyleClass().add("dashboard");
        myPane.getChildren().add(myBox);
    }

    public Pane getPane() {
        return myPane;
    }
}
