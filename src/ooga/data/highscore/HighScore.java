package ooga.data.highscore;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * This class stores HighScore information to later write it to XML files.
 *
 * @author Tyler Jang Mariusz Derezinski-Choo
 */
public class HighScore implements IHighScores {

  private Map<String, PriorityQueue<Double>> myScoreMap;
  private String saveFilePath;

  /**
   * The Constructor for HighScore, setting the high score map.
   *
   * @param xmlFile the location to which the file should be saved
   * @param scores  a Map of String game names to Double high scores
   */
  public HighScore(String xmlFile, Map<String, List<Double>> scores) {
    myScoreMap = new HashMap<>();
    saveFilePath = xmlFile;
    for (String key : scores.keySet()) {
      List<Double> gameScores = scores.get(key);
      Collections.sort(gameScores);
      PriorityQueue<Double> gameQueue = new PriorityQueue<>();
      gameQueue.addAll(gameScores);
      myScoreMap.put(key, gameQueue);
    }
  }

  /**
   * Gets the List of all saved games.
   *
   * @return a Set of Strings representing saved games
   */
  @Override
  public Set<String> getSavedGames() {
    return myScoreMap.keySet();
  }

  /**
   * Saves the scores to an XML file.
   */
  @Override
  public void saveScores() {
    HighScoreWriter.writeScores(saveFilePath, this);
  }

  @Override
  public Collection<Double> getScore(String name) {
    return myScoreMap.getOrDefault(name, new PriorityQueue<>());
  }

  /**
   * Replaces a score if already present, or adds a new score for a given game.
   *
   * @param name  the name of the game being set
   * @param score the high score for the game being set
   */
  @Override
  public void setScore(String name, double score) {
    myScoreMap.putIfAbsent(name.toLowerCase(), new PriorityQueue<>());
    PriorityQueue<Double> scores = myScoreMap.get(name.toLowerCase());
    scores.offer(score);
    while (scores.size() > 50) {
      scores.poll();
    }
    saveScores();
  }
}
