package ooga.view.gamescreen;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class Dashboard {

    private Pane myPane;
    private HBox myBox;

    private double myScore;
    private String myScoreLabel;
    private Text myScoreDisplay;
    private ResourceBundle myMessages;
    private Text myInstructions;
    private Stage myPopUp;

    public Dashboard(Button backButton, Button restartButton, String scoreLabel, ResourceBundle messages, String game) {
        myMessages = messages;
        myPane = new Pane();
        myBox = new HBox();

        myScore = 0;
        myScoreLabel = scoreLabel;
        myScoreDisplay = new Text();
        myScoreDisplay.setText(myScoreLabel+" "+myScore);
        myScoreDisplay.getStyleClass().add("score");

        myInstructions = new Text();
        HBox textholder = new HBox();
        textholder.getChildren().add(myInstructions);
        Pane messagePane = new Pane();
        messagePane.getChildren().add(textholder);
        Scene messageScene = new Scene(messagePane);
        myPopUp = new Stage();
        myPopUp.setScene(messageScene);

        Button instructionsButton = new Button(messages.getString("insns"));
        instructionsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                myInstructions.setText(messages.getString(game.toLowerCase()+"_insns"));
                myPopUp.show();
            }
        });
        //messages.getString(game+"_insns");

        myBox.getStyleClass().add("dashboard");
        myBox.getChildren().addAll(backButton, restartButton, instructionsButton, myScoreDisplay);

        myPane.getStyleClass().add("dashboard");
        myPane.getChildren().add(myBox);
    }

    private void showPopUp(String text) {
        myInstructions.setText(text);
        myPopUp.show();
    }

    public void updateScore(double newScore) {
        myScoreDisplay.setText(myScoreLabel+" "+newScore);
    }

    public Pane getPane() {
        return myPane;
    }
}
