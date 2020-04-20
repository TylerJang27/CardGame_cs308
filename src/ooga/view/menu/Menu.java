package ooga.view.menu;

import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ooga.view.View;

public class Menu {
  private static final double DEFAULT_HEIGHT= 500;
  private static final double DEFAULT_WIDTH= 650;
  private static final Insets MARGINS = new Insets(305,20,20,20);
  private static final double SPACING = 10;
  private static final ResourceBundle LANGUAGES = ResourceBundle.getBundle("ooga.resources.languages.supportedlanguages");
  private static final String CHOICES = "ooga.resources.languages.menu";
  private static final ResourceBundle GAMES = ResourceBundle.getBundle("ooga.resources.languages.menu.English");
  private static final String APPLICATION_NAME = "Solitaire Confinement";
  private static final ResourceBundle SKINS = ResourceBundle.getBundle("ooga.resources.skins.supportedskins");

  private BorderPane myBorderPane;
  private Stage myStage;
  private StringProperty myGameProperty;
  private Scene myScene;

  private View.ChangeTheme myThemeLambda;

  public Menu(View.ChangeTheme themeLambda, String defaultTheme){

    myThemeLambda = themeLambda;
    myGameProperty = new SimpleStringProperty();
    Dictionary.getInstance().addReference(CHOICES);

    myBorderPane = new BorderPane();

    setTopBorder();
    setCenter();
    setBottomBorder();

    myScene = new Scene(myBorderPane,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    myScene.getStylesheets().add(getClass().getResource("/ooga/resources/skins/"+defaultTheme.toLowerCase()+"/mainmenu.css").toExternalForm()); //

  }

  public Scene getScene() {
    return myScene;
  }

  private void setBottomBorder() {
    ComboBox<String> languages = new ComboBox<>();
    languages.getItems().addAll(LANGUAGES.getString("supported").split(","));
    languages.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
                          String newValue) {
        Dictionary.getInstance().setLanguage(newValue);
      }
    });

    ComboBox<String> skins = new ComboBox<>();
    skins.getItems().addAll(SKINS.getString("supported").split(","));
    skins.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
                          String newValue) {
        myThemeLambda.setTheme(newValue);
        myScene.getStylesheets().clear();
        myScene.getStylesheets().add(getClass().getResource("/ooga/resources/skins/"+newValue.toLowerCase()+"/mainmenu.css").toExternalForm());
      }
    });

    HBox dashboard = new HBox();
    dashboard.getChildren().addAll(languages, skins);
    dashboard.getStyleClass().add("border");
    dashboard.getStyleClass().add("dashboard");

    myBorderPane.setBottom(dashboard);
  }

  private void setCenter() {
    HBox options = new HBox();
    options.getStyleClass().add("options");
    myBorderPane.setCenter(options);
    options.setPadding(MARGINS);

    options.prefWidthProperty().bind(myBorderPane.widthProperty());
    options.prefHeightProperty().bind(myBorderPane.heightProperty());

    for(String game : GAMES.keySet()){
      addOption(game,options);
    }
  }

  private void setTopBorder() {
    StackPane gameNamePane = new StackPane();
    Text gameName = new Text(APPLICATION_NAME);
    gameName.getStyleClass().add("title");
    gameNamePane.getChildren().add(gameName);
    gameNamePane.getStyleClass().add("titleborder");
    myBorderPane.setTop(gameNamePane);
  }

  public void addChosenHandler(ChangeListener<String> listener) {
    myGameProperty.addListener(listener);
  }

  private void addOption(String key, HBox hbox){
    Button option = new Button();
    option.textProperty().bind(Dictionary.getInstance().get(key));
    option.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(option, Priority.ALWAYS);
    option.prefHeightProperty().bind(hbox.heightProperty());
    option.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        String newGame = key;
        if (myGameProperty.getValue() != null && myGameProperty.getValue().equals(key)) {
          newGame = key.toUpperCase();
        }
        myGameProperty.setValue(newGame);
      }
    });
    hbox.getChildren().add(option);
  }

}
