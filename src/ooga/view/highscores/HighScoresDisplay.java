package ooga.view.highscores;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import ooga.view.KeyPreservingStringProperty;
import ooga.view.menu.Dictionary;

public class HighScoresDisplay {
  private static final String INDEX = "Index";
  private static final String SCORES = "Scores";

  private Node myNode;

  public HighScoresDisplay(HighScoresManager manager){
    TableView<Double> view = new TableView<>();
    view.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    TableColumn<Double, String> indexCol = new TableColumn<>();
    indexCol.textProperty().bind(Dictionary.getInstance().get(INDEX));
    indexCol.setCellFactory(col -> {
      TableCell<Double, String> cell = new TableCell<>();
      cell.textProperty().bind(Bindings.createStringBinding(() -> {
        if (cell.isEmpty()) {
          return null ;
        } else {
          return Integer.toString(cell.getIndex());
        }
      }, cell.emptyProperty(), cell.indexProperty()));
      return cell ;
    });
    view.getColumns().add(indexCol);


    TableColumn<Double, String> col = new TableColumn<>();
    col.textProperty().bind(Dictionary.getInstance().get(SCORES));

    col.setCellValueFactory(
        param -> new SimpleStringProperty(Double.toString(param.getValue())));
    view.getColumns().add(col);

    ComboBox<KeyPreservingStringProperty> gameChoices = new ComboBox<>();
    gameChoices.setItems(manager.getGames());
    gameChoices.setConverter(new StringConverter<>() {
      @Override
      public String toString(KeyPreservingStringProperty object) {
        if (object != null) {
          return object.valueProperty().getValue();
        }
        return null;
      }

      @Override
      public KeyPreservingStringProperty fromString(String string) {
        return null;
      }
    });
    gameChoices.valueProperty().addListener((observable, oldValue, newValue) -> {
      String newKey = newValue.getKey();
      view.setItems(manager.getGameHighScores(newKey));
    });
    gameChoices.getSelectionModel().selectFirst();

    BorderPane root = new BorderPane();
    root.setCenter(view);
    root.setTop(gameChoices);

    myNode = root;
  }
  public Node getNode(){
    return myNode;
  }
}
