package ooga.view.gamescreen;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Dashboard {

    private Pane myPane;
    private HBox myBox;

    private double myScore;
    private String myScoreLabel;
    private Text myScoreDisplay;

    public Dashboard(Button backButton, Button restartButton, String scoreLabel) {
        myPane = new Pane();
        myBox = new HBox();
        myScore = 0;
        myScoreLabel = scoreLabel;
        myScoreDisplay = new Text();
        myScoreDisplay.setText(myScoreLabel+" "+myScore);
        myScoreDisplay.getStyleClass().add("score");
        myBox.getChildren().addAll(backButton, restartButton, myScoreDisplay);
        myBox.getStyleClass().add("dashboard");
        myPane.getStyleClass().add("dashboard");
        myPane.getChildren().add(myBox);
    }


    public void updateScore(double newScore) {
        myScoreDisplay.setText(myScoreLabel+" "+newScore);
    }

    public Pane getPane() {
        return myPane;
    }
}
