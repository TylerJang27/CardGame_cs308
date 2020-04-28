package ooga.data.highscore;

import java.util.Collection;
import java.util.Set;

/**
 * This interfaces governs high score information to be parsed and passed to the frontend and
 * modified.
 *
 * @author Tyler Jang
 */
public interface IHighScores {

  String DATA_TYPE = "score";
  String SCORE = "score";

  /**
   * Gets the List of all saved games.
   *
   * @return a Set of Strings representing saved games
   */
  Set<String> getSavedGames();

  /**
   * Saves the scores to an XML file.
   */
  void saveScores();

  /**
   * Retrieves the score associated with a particular game.
   *
   * @param name the name of the game being queried
   * @return
   */
  Collection<Double> getScore(String name);

  /**
   * Replaces a score if already present, or adds a new score for a given game.
   *
   * @param name  the name of the game being set
   * @param score the high score for the game being set
   */
  void setScore(String name, double score);
}
