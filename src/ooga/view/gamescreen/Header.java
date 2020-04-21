package ooga.view.gamescreen;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Header {

    private Pane myPane;
    private HBox myBox;

    public Header(String game) {
        myPane = new Pane();
        myBox = new HBox();
        Text title = new Text(game);
        title.getStyleClass().add("title");
        //myBox.getChildren().add(title);
        myBox.getStyleClass().add("header");
        myPane.getStyleClass().add("header");
        myPane.getChildren().add(myBox);
    }

    public Pane getPane(){
        return myPane;
    }
}
