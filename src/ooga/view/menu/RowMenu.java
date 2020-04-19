package ooga.view.menu;

import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
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
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ooga.view.Dictionary;

public class RowMenu implements Menu {
  private static final double DEFAULT_SIZE= 500;
  private static final Insets MARGINS = new Insets(20,20,20,20);
  private static final double SPACING = 10;
  private static final ResourceBundle LANGUAGES = ResourceBundle.getBundle("ooga.resources.languages.supportedlanguages");
  private static final String CHOICES = "ooga.resources.languages.menu";
  private static final ResourceBundle GAMES = ResourceBundle.getBundle("ooga.resources.languages.menu.English");
  private static final String APPLICATION_NAME = "Solitaire Confinement";

  private BorderPane myPane;
  private Stage myStage;
  private StringProperty myGameProperty;

  public RowMenu(){

    myGameProperty = new SimpleStringProperty();
    Dictionary.getInstance().addReference(CHOICES);
    myPane = new BorderPane();


    // Make application name at top of screen
    StackPane gameNamePane = new StackPane();
    //Text gameName = new Text(APPLICATION_NAME);
    //gameNamePane.getChildren().add(gameName); //add imageView to stackPane
    Button fakebutton = new Button("Accept");
    fakebutton.getStyleClass().add("button1");
    gameNamePane.getChildren().add(fakebutton);
    myPane.setTop(gameNamePane);

    // Create HBox for game choices (options)
    HBox options = new HBox(SPACING);
    myPane.setCenter(options);
    options.setPadding(MARGINS);

    // Bind size of HBox for game choices (options) to h/w of window
    options.prefWidthProperty().bind(myPane.widthProperty());
    options.prefHeightProperty().bind(myPane.heightProperty());

    // Add buttons to HBox for game choices (options)
    for(String game : GAMES.keySet()){
      addOption(game,options);
    }

    // Add listener to language change drop down
    ComboBox<String> languages = new ComboBox<>();
    languages.getItems().addAll(LANGUAGES.getString("supported").split(","));
    languages.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
          String newValue) {
        Dictionary.getInstance().setLanguage(newValue);
      }
    });

    // Make dashboard for bottom of screen
    Pane dashboard = new Pane();
    dashboard.getChildren().add(languages);
    myPane.setBottom(dashboard);


    // Create scene and set stage using myPane (BorderPane)
    myStage = new Stage();
    Scene scene = new Scene(myPane,DEFAULT_SIZE,DEFAULT_SIZE);
    myStage.setScene(scene);
  }

  @Override
  public void show() {
    myStage.show();
  }

  @Override
  public void addChosenHandler(ChangeListener<String> listener) {
    //System.out.println("test");
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
        myGameProperty.setValue(key);
      }
    });
    hbox.getChildren().add(option);
  }

}
