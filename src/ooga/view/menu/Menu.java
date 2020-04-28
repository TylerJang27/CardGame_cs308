package ooga.view.menu;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import ooga.view.View;
import ooga.view.View.ChangeValue;

public class Menu {

  private static final Insets MARGINS = new Insets(20, 20, 20, 20);

  private static final String CHOICES = "ooga.resources.languages.games";
  private static final String COMMA_REGEX = ",";
  private static final String SUPPORTED = "supported";
  private static final String HIGH_SCORES = "High_Scores";
  private static final String LOAD_GAME = "Load_Game";
  private static final String LOAD_HEADER = "Load_Header";
  private static final String XML_FILE = "XMLFile";
  private static final String XML_EXTENSION = "*.xml";
  private static final List<String> DASHBOARD_CSS = List.of("dashboard", "border");
  private static final List<String> OPTIONS_CSS = List.of("options");
  private static final List<String> ROOT_CSS = List.of("menu");
  private static final String GAMES_FOLDER = "ooga.resources.languages.games.%s";
  private static final double GAMES_SPACING = 20.0;
  public static final String TITLE = "title";
  public static final String TITLEBORDER = "titleborder";

  private BorderPane myBorderPane;
  private StringProperty myGameProperty;

  private String myGame;

  public Menu(String appName, ResourceBundle supportedLangs, ResourceBundle supportedSkins,
      View.ChangeValue themeLambda, View.ChangeValue languageLambda, String defaultTheme,
      String defaultLanguage, double screenHeight, double screenWidth,
      EventHandler<MouseEvent> highScoresHandler, Consumer<String> gameLoad) {

    myGameProperty = new SimpleStringProperty();
    Dictionary.getInstance().addReference(CHOICES);
    Dictionary.getInstance().setLanguage(defaultLanguage);

    myBorderPane = new BorderPane();
    myBorderPane.getStyleClass().addAll(ROOT_CSS);

    setTopBorder(appName);
    setCenter(defaultLanguage);
    setBottomBorder(supportedLangs, supportedSkins, defaultTheme, defaultLanguage, themeLambda,
        languageLambda, highScoresHandler, gameLoad);
  }

  public Pane getScene() {
    return myBorderPane;
  }

  public String getGame() {
    return myGame;
  }

  public void addChosenHandler(ChangeListener<String> listener) {
    myGameProperty.addListener(listener);
  }

  private void setBottomBorder(ResourceBundle supportedLangs, ResourceBundle supportedSkins,
      String defaultTheme, String defaultLanguage, View.ChangeValue themeLambda,
      View.ChangeValue languageLambda, EventHandler<MouseEvent> highScoreHandler,
      Consumer<String> gameLoad) {
    ComboBox<String> languages = getLanguagesComboBox(supportedLangs, defaultLanguage,
        languageLambda);

    ComboBox<String> skins = makeSkinsComboBox(supportedSkins, defaultTheme, themeLambda);
    Button highScoresButton = makeHighScoresButton(highScoreHandler);
    Button loadButton = makeLoadGameButton(gameLoad);

    HBox dashboard = new HBox();
    dashboard.getChildren().addAll(languages, skins, highScoresButton, loadButton);
    dashboard.getStyleClass().addAll(DASHBOARD_CSS);

    myBorderPane.setBottom(dashboard);
    //System.out.println(dashboard.getStyleClass());
  }

  private Button makeLoadGameButton(Consumer<String> gameLoad) {
    Button loadButton = new Button(LOAD_GAME);
    loadButton.setOnMouseClicked(event -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle(LOAD_HEADER);
      fileChooser.getExtensionFilters().add(new ExtensionFilter(
          Dictionary.getInstance().get(XML_FILE).getValue(), XML_EXTENSION));
      File file = fileChooser.showOpenDialog(new Stage());
      gameLoad.accept(file.getPath());
    });
    return loadButton;
  }

  private Button makeHighScoresButton(EventHandler<MouseEvent> highScoreHandler) {
    Button highScoresButton = new Button();
    highScoresButton.textProperty().bind(Dictionary.getInstance().get(HIGH_SCORES));
    highScoresButton.setOnMouseClicked(highScoreHandler);
    return highScoresButton;
  }

  private ComboBox<String> makeSkinsComboBox(ResourceBundle supportedSkins, String defaultTheme,
      ChangeValue themeLambda) {
    ComboBox<String> skins = new ComboBox<>();
    skins.getItems().addAll(supportedSkins.getString(SUPPORTED).split(COMMA_REGEX));
    skins.setValue(defaultTheme);
    skins.valueProperty().addListener(
        (observable, oldValue, newValue) -> themeLambda.setValue(newValue));
    return skins;
  }

  private ComboBox<String> getLanguagesComboBox(ResourceBundle supportedLangs,
      String defaultLanguage, ChangeValue languageLambda) {
    ComboBox<String> languages = new ComboBox<>();
    languages.getItems().addAll(supportedLangs.getString(SUPPORTED).split(COMMA_REGEX));
    languages.setValue(defaultLanguage);
    languages.valueProperty().addListener((observable, oldValue, newValue) -> {
      languageLambda.setValue(newValue);
      Dictionary.getInstance().setLanguage(newValue);
    });
    return languages;
  }

  private void setCenter(String defaultLanguage) {
    FlowPane options = new FlowPane();
    options.setAlignment(Pos.BASELINE_CENTER);
    options.setHgap(GAMES_SPACING);
    options.setVgap(GAMES_SPACING);
    //System.out.println(options.getRowValignment());
    options.getStyleClass().addAll(OPTIONS_CSS);
    myBorderPane.setCenter(options);
    options.setPadding(MARGINS);

    options.prefWidthProperty().bind(myBorderPane.widthProperty());
    options.prefHeightProperty().bind(myBorderPane.heightProperty());

    ResourceBundle games = ResourceBundle.getBundle(String.format(GAMES_FOLDER, defaultLanguage));
    for (String game : games.keySet()) {
      addOption(game, options);
    }
  }

  private void setTopBorder(String appName) {
    StackPane gameNamePane = new StackPane();
    Text gameName = new Text(appName);
    gameName.getStyleClass().add(TITLE);
    gameNamePane.getChildren().add(gameName);
    gameNamePane.getStyleClass().add(TITLEBORDER);
    myBorderPane.setTop(gameNamePane);
  }

  private void addOption(String key, FlowPane hbox) {
    Button option = new Button();
    option.textProperty().bind(Dictionary.getInstance().get(key));
    option.setMaxWidth(Double.MAX_VALUE);
    option.setOnMouseClicked(event -> {
      String newGame = key;
      if (myGameProperty.getValue() != null && myGameProperty.getValue().equals(key)) {
        newGame = key.toUpperCase();
      }
      myGame = newGame;
      myGameProperty.setValue(newGame);
    });
    hbox.getChildren().add(option);
  }

}
