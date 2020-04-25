package ooga.view.gamescreen;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import ooga.view.menu.Dictionary;

public class Dashboard {

  private static final String SCORE = "score";
  private static final String INSTRUCTIONS = "%s_insns";
  private static final String INSTRUCTIONS_BUTTON = "insns";
  private static final String SAVE = "Save";
  private static final String SAVE_WINDOW_TITLE = "savefilechoosertitle";
  private static final String XML_FILE = "XMLFile";
  private static final String XML_EXTENSION = "*.xml";
  private static final double INITIAL_SCORE = 0.0;
  private static final List<String> DASHBOARD_CLASS = List.of("dashboard");
  private static final String SPACE = " ";

  private Pane myPane;

  private String myScoreLabel;
  private Text myScoreDisplay;
  private Stage myPopUp;

  public Dashboard(Button restartButton, String scoreLabel, String game,
      Consumer<String> saveConsumer) {
    HBox myBox = new HBox();

    myScoreLabel = scoreLabel;
    initializeScoreDisplay();

    Pane messagePane = initializeInstructions(game);
    Scene messageScene = new Scene(messagePane);
    Button instructionsButton = makeMessageButton(messageScene);
    Button saveButton = makeSaveButton(saveConsumer);

    myBox.getStyleClass().addAll(DASHBOARD_CLASS);
    myBox.getChildren().addAll(restartButton, instructionsButton, myScoreDisplay, saveButton);

    myPane = new Pane(myBox);
  }

  public Node getNode() {
    return myPane;
  }

  private Button makeSaveButton(Consumer<String> saveConsumer) {
    Button saveButton = new Button();
    saveButton.textProperty().bind(Dictionary.getInstance().get(SAVE));

    saveButton.setOnMouseClicked(click -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.titleProperty().bind(Dictionary.getInstance().get(SAVE_WINDOW_TITLE));
      fileChooser.getExtensionFilters().add(
          new ExtensionFilter(Dictionary.getInstance().get(XML_FILE).getValue(), XML_EXTENSION));
      File saveFile = fileChooser.showSaveDialog(new Stage());
      saveConsumer.accept(saveFile.getPath());
    });
    return saveButton;
  }

  private Pane initializeInstructions(String game) {
    Text myInstructions = new Text();
    myInstructions.textProperty().bind(
        Dictionary.getInstance().get(String.format(INSTRUCTIONS, game.toLowerCase())));
    Pane messagePane = new Pane();
    messagePane.getChildren().add(myInstructions);
    return messagePane;
  }

  private Button makeMessageButton(Scene messageScene) {
    myPopUp = new Stage();
    myPopUp.setScene(messageScene);
    Button instructionsButton = new Button();
    instructionsButton.textProperty().bind(Dictionary.getInstance().get(INSTRUCTIONS_BUTTON));
    instructionsButton.setOnAction(e -> myPopUp.show());
    return instructionsButton;
  }

  private void initializeScoreDisplay() {
    myScoreDisplay = new Text();
    myScoreDisplay.setText(myScoreLabel + SPACE + INITIAL_SCORE);
    myScoreDisplay.getStyleClass().add(SCORE);
  }

  public void updateScore(double newScore) {
    myScoreDisplay.setText(myScoreLabel + SPACE + newScore);
  }
}
