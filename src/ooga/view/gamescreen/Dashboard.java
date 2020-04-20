package ooga.view.gamescreen;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class Dashboard {

    private Pane myPane;
    private HBox myBox;

    public Dashboard(Button backButton) {
        myPane = new Pane();
        myBox = new HBox();
        myBox.getChildren().add(backButton);
        myPane.getChildren().add(myBox);
    }

    public Pane getPane() {
        return myPane;
    }
}
