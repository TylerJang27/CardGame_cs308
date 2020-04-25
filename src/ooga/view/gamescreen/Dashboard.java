package ooga.view.gamescreen;

import java.io.File;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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

    public Dashboard(Button restartButton, String scoreLabel, ResourceBundle messages, String game, Consumer<String> saveConsumer) {
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
        Button saveButton = new Button("Save");
        saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save File");
                fileChooser.getExtensionFilters().add(new ExtensionFilter("XML File","*.xml"));
                File saveFile = fileChooser.showSaveDialog(new Stage());
                saveConsumer.accept(saveFile.getPath());
            }
        });

        myBox.getStyleClass().add("dashboard");
        myBox.getChildren().addAll(restartButton, instructionsButton, myScoreDisplay,saveButton);

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
