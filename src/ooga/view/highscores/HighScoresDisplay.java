package ooga.view.highscores;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import ooga.view.KeyPreservingStringProperty;

public class HighScoresDisplay {

  private HighScoresManager myManager;
  private Node myNode;

  public HighScoresDisplay(HighScoresManager manager){
    myManager = manager;
    TableView<Double> view = new TableView<>();
    view.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    TableColumn<Double, String> indexCol = new TableColumn<>("Index");
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


    TableColumn<Double, String> col = new TableColumn<>("Scores");
    col.setCellValueFactory(
        new Callback<CellDataFeatures<Double, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(CellDataFeatures<Double, String> param) {
            return new SimpleStringProperty(Double.toString(param.getValue()));
          }
        });
    view.getColumns().add(col);
    ObservableList<Double> scores = manager.getGameHighScores("memory");
    view.setItems(scores);

    ComboBox<KeyPreservingStringProperty> gameChoices = new ComboBox<>();
    gameChoices.setItems(manager.getGames());
    gameChoices.setConverter(new StringConverter<KeyPreservingStringProperty>() {
      @Override
      public String toString(KeyPreservingStringProperty object) {
        if(object != null) {
          return object.valueProperty().getValue();
        }
        return null;
      }
      @Override
      public KeyPreservingStringProperty fromString(String string) {
        System.out.println("here!");
        return null;
      }
    });
    gameChoices.valueProperty().addListener(new ChangeListener<KeyPreservingStringProperty>() {
      @Override
      public void changed(ObservableValue<? extends KeyPreservingStringProperty> observable,
          KeyPreservingStringProperty oldValue, KeyPreservingStringProperty newValue) {
        String newKey = newValue.getKey();
        view.setItems(manager.getGameHighScores(newKey));
      }
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
