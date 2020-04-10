package ooga.view.menu;

import java.awt.Color;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import ooga.view.Dictionary;

public class RowMenu implements Menu {
  private static final double DEFAULT_SIZE= 500;
  private static final Insets MARGINS = new Insets(20,20,20,20);
  private static final double SPACING = 10;
  private static final String CHOICES = "ooga.resources.languages.menu";
  private static final ResourceBundle GAMES = ResourceBundle.getBundle("ooga.resources.languages.menu.English");

  private BorderPane myPane;
  private Stage myStage;

  public RowMenu(){
    Dictionary.getInstance().addReference(CHOICES);

    HBox options = new HBox(SPACING);
    myPane = new BorderPane();
    myPane.setCenter(options);
    options.setPadding(MARGINS);

    myPane.setStyle("-fx-background-color: #CCFF99"); //this is just here so i can debug
    options.prefWidthProperty().bind(myPane.widthProperty());
    options.prefHeightProperty().bind(myPane.heightProperty());

    for(String game : GAMES.keySet()){
      addOption(game,options);
    }

    Pane dashboard = new Pane();
    ComboBox<String> languages = new ComboBox<>();
    languages.getItems().addAll("English","Español");
    languages.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
          String newValue) {
        Dictionary.getInstance().setLanguage(newValue);
      }
    });
    dashboard.getChildren().add(languages);
    myPane.setTop(dashboard);

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
    //TODO
  }

  private void addOption(String key, HBox hbox){
    Button option = new Button();
    option.textProperty().bind(Dictionary.getInstance().get(key));
    option.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(option, Priority.ALWAYS);
    option.prefHeightProperty().bind(hbox.heightProperty());
    hbox.getChildren().add(option);
  }
}
