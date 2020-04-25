package ooga.view.gamescreen;

import java.util.List;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class Header {

  private static final List<String> LABEL_CLASSES = List.of("message");
  private static final List<String> PANE_CLASSES = List.of("header");
  private static final double TRANSITION_TIME = 8;
  private static final double INITIAL_OPACITY = 1;
  private static final double FINAL_OPACITY = 0;

  private Pane myPane;
  private Label myLabel;
  private FadeTransition myFader;

  public Header() {
    myLabel = new Label();
    myLabel.getStyleClass().addAll(LABEL_CLASSES);

    myFader = createFade(myLabel);

    myPane = new StackPane(myLabel);
    myPane.getStyleClass().addAll(PANE_CLASSES);
  }

  public void playMessage(String text) {
    myLabel.setText(text);
    myFader.play();
  }

  public Pane getPane() {
    return myPane;
  }

  private FadeTransition createFade(Node node) {
    FadeTransition fade = new FadeTransition(Duration.seconds(TRANSITION_TIME), node);
    fade.setFromValue(INITIAL_OPACITY);
    fade.setToValue(FINAL_OPACITY);

    return fade;
  }
}
