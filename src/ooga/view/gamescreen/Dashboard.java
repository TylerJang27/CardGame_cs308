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
import ooga.view.menu.Dictionary;

public class Dashboard {
    private static final String SCORE = "score";
    private static final String INSTRUCTIONS = "%s_insns";
    private static final String INSTRUCTIONS_BUTTON = "insns";
    private static final String SAVE = "Save";
    private static final String SAVE_WINDOW_TITLE = "savefilechoosertitle";
    private static final String XML_FILE = "XMLFile";
    private static final String XML_EXTENSION = "*.xml";

    private Pane myPane;
    private HBox myBox;

    private double myScore;
    private String myScoreLabel;
    private Text myScoreDisplay;
    private Text myInstructions;
    private Stage myPopUp;

    public Dashboard(Button restartButton, String scoreLabel, String game, Consumer<String> saveConsumer) {
        myPane = new Pane();
        myBox = new HBox();

        myScore = 0;
        myScoreLabel = scoreLabel;
        myScoreDisplay = new Text();
        myScoreDisplay.setText(myScoreLabel+" "+myScore);
        myScoreDisplay.getStyleClass().add(SCORE);

        myInstructions = new Text();
        System.out.println(String.format(INSTRUCTIONS, game.toLowerCase()));
        myInstructions.textProperty().bind(Dictionary.getInstance().get(INSTRUCTIONS.format(game.toLowerCase())));
        HBox textholder = new HBox();
        textholder.getChildren().add(myInstructions);
        Pane messagePane = new Pane();
        messagePane.getChildren().add(textholder);
        Scene messageScene = new Scene(messagePane);
        myPopUp = new Stage();
        myPopUp.setScene(messageScene);

        Button instructionsButton = new Button();
        instructionsButton.textProperty().bind(Dictionary.getInstance().get(INSTRUCTIONS_BUTTON));
        instructionsButton.setOnAction(e -> myPopUp.show());
        Button saveButton = new Button();
        saveButton.textProperty().bind(Dictionary.getInstance().get(SAVE));

        saveButton.setOnMouseClicked(click -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.titleProperty().bind(Dictionary.getInstance().get(SAVE_WINDOW_TITLE));
                fileChooser.getExtensionFilters().add(new ExtensionFilter(Dictionary.getInstance().get(XML_FILE).getValue(),XML_EXTENSION));
                File saveFile = fileChooser.showSaveDialog(new Stage());
                saveConsumer.accept(saveFile.getPath());
        });

        myBox.getStyleClass().add("dashboard"); //FIXME
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
