package ooga.view.highscores;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ooga.view.KeyPreservingStringProperty;

public class HighScoresManager {

  private Map<String, ObservableList<Double>> myHighScores;
  private ObservableList<KeyPreservingStringProperty> myGames;

  public HighScoresManager() {
    myHighScores = new HashMap<>();
    myGames = FXCollections.observableArrayList();
  }

  public void addScore(String gameType, Collection<Double> highScores) {
    myHighScores.putIfAbsent(gameType.toLowerCase(), FXCollections.observableArrayList());
    myHighScores.get(gameType.toLowerCase()).clear();
    myHighScores.get(gameType.toLowerCase()).addAll(highScores);
    myHighScores.get(gameType.toLowerCase()).sort(Collections.reverseOrder());

    KeyPreservingStringProperty newKey = new KeyPreservingStringProperty(gameType);
    if (!myGames.contains(newKey)) {
      myGames.add(new KeyPreservingStringProperty(gameType));
    }
  }

  public ObservableList<Double> getGameHighScores(String gameID) {
    return myHighScores.getOrDefault(gameID, FXCollections.observableArrayList());
  }

  public ObservableList<KeyPreservingStringProperty> getGames() {
    return myGames;
  }
}