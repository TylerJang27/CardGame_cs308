package ooga.view.gamescreen;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Header {

    private Pane myPane;
    private Label myLabel;
    private FadeTransition myFader;

    public Header() {

        myLabel = new Label();
        //myLabel.setStyle("-fx-text-fill: white; -fx-padding: 10px;");
        myLabel.getStyleClass().add("message");

        myFader = createFader(myLabel);

        myPane = new StackPane(myLabel);
        myPane.getStyleClass().add("header");
    }

    public void playMessage(String text) {
        myLabel.setText(text);
        myFader.play();
    }

    public Pane getPane(){
        return myPane;
    }

    private FadeTransition createFader(Node node) {
        FadeTransition fade = new FadeTransition(Duration.seconds(8), node);
        fade.setFromValue(1);
        fade.setToValue(0);

        return fade;
    }
}
