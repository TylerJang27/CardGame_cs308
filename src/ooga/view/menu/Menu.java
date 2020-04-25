package ooga.view.menu;

import java.io.File;
import java.util.ResourceBundle;

import java.util.function.Consumer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import ooga.view.View;

public class Menu {
  private static final Insets MARGINS = new Insets(305,20,20,20);

  private static final String CHOICES = "ooga.resources.languages.games";

  private BorderPane myBorderPane;
  private StringProperty myGameProperty;
  private Scene myScene;

  private String myGame;

  public Menu(String appName, ResourceBundle supportedLangs, ResourceBundle supportedSkins, View.ChangeValue themeLambda, View.ChangeValue languageLambda, String defaultTheme, String defaultLanguage, double screenHeight, double screenWidth,EventHandler<MouseEvent> highScoresHandler, Consumer<String> gameLoad){

    myGameProperty = new SimpleStringProperty();
    Dictionary.getInstance().addReference(CHOICES);
    Dictionary.getInstance().setLanguage(defaultLanguage);

    myBorderPane = new BorderPane();

    setTopBorder(appName);
    setCenter(defaultLanguage);
    setBottomBorder(supportedLangs, supportedSkins, defaultTheme, defaultLanguage, themeLambda, languageLambda,highScoresHandler,gameLoad);
  }

  public Pane getScene() {
    return myBorderPane;
  }

  public String getGame() {
    return myGame;
  }

  private void setBottomBorder(ResourceBundle supportedLangs, ResourceBundle supportedSkins, String defaultTheme, String defaultLanguage, View.ChangeValue themeLambda, View.ChangeValue languageLambda,EventHandler<MouseEvent> highScoreHandler,Consumer<String> gameLoad) {
    ComboBox<String> languages = new ComboBox<>();
    languages.getItems().addAll(supportedLangs.getString("supported").split(","));
    languages.setValue(defaultLanguage);
    languages.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
                          String newValue) {
        languageLambda.setValue(newValue);
        Dictionary.getInstance().setLanguage(newValue);
      }
    });

    ComboBox<String> skins = new ComboBox<>();
    skins.getItems().addAll(supportedSkins.getString("supported").split(","));
    skins.setValue(defaultTheme);
    skins.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
                          String newValue) {
        themeLambda.setValue(newValue);
      }
    });
    Button highScoresButton = new Button("high scores");
    highScoresButton.setOnMouseClicked(highScoreHandler);
    Button loadButton = new Button("Load Game");
    loadButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("load a file");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("XML File","*.xml"));
        File file = fileChooser.showOpenDialog(new Stage());
        gameLoad.accept(file.getPath());
      }
    });

    HBox dashboard = new HBox();
    dashboard.getChildren().addAll(languages, skins,highScoresButton,loadButton);
    dashboard.getStyleClass().add("border");
    dashboard.getStyleClass().add("dashboard");

    myBorderPane.setBottom(dashboard);
  }

  private void setCenter(String defaultLanguage) {
    FlowPane options = new FlowPane();
    options.setAlignment(Pos.CENTER);
    options.setHgap(20);
    options.setVgap(20);
    System.out.println(options.getRowValignment());
    options.getStyleClass().add("options");
    myBorderPane.setCenter(options);
    options.setPadding(MARGINS);

    options.prefWidthProperty().bind(myBorderPane.widthProperty());
    options.prefHeightProperty().bind(myBorderPane.heightProperty());

    ResourceBundle games = ResourceBundle.getBundle("ooga.resources.languages.games."+defaultLanguage);
    for(String game : games.keySet()){
      addOption(game,options);
    }
  }

  private void setTopBorder(String appName) {
    StackPane gameNamePane = new StackPane();
    Text gameName = new Text(appName);
    gameName.getStyleClass().add("title");
    gameNamePane.getChildren().add(gameName);
    gameNamePane.getStyleClass().add("titleborder");
    myBorderPane.setTop(gameNamePane);
  }

  public void addChosenHandler(ChangeListener<String> listener) {
    myGameProperty.addListener(listener);
  }

  private void addOption(String key, FlowPane hbox){
    Button option = new Button();
    option.textProperty().bind(Dictionary.getInstance().get(key));
    option.setMaxWidth(Double.MAX_VALUE);
    //HBox.setHgrow(option, Priority.ALWAYS);
    //option.prefHeightProperty().bind(hbox.heightProperty());
    option.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        String newGame = key;
        if (myGameProperty.getValue() != null && myGameProperty.getValue().equals(key)) {
          newGame = key.toUpperCase();
        }
        myGame = newGame;
        myGameProperty.setValue(newGame);
      }
    });
    hbox.getChildren().add(option);
  }

}
