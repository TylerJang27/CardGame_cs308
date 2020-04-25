package ooga.data.saveconfiguration;

import java.util.Map;
import ooga.cardtable.ICell;

/**
 * This interface governs saved configurations of games, including the game name, the current score,
 * the current phase, and the current cell states.
 *
 * @author Tyler Jang
 */
public interface ISaveConfiguration {

  String DATA_TYPE = "save";

  /**
   * Retrieves the name of the game that was stored.
   *
   * @return a String representing the game name
   */
  String getGameName();

  /**
   * Retrieves the file path of the rules.
   *
   * @return a String representing the path to the rules file
   */
  String getRulePath();

  /**
   * Retrieves the name of the current phase at save.
   *
   * @return a String representing the current phase
   */
  String getCurrentPhase();

  /**
   * Retrieves the current phase at save.
   *
   * @return a Double representing the score
   */
  Double getScore();

  /**
   * Retrieves the cell map present at save.
   *
   * @return a Map of String ICell names to ICells
   */
  Map<String, ICell> getCellMap();

  /**
   * Writes the SaveConfiguration information to the given filepath.
   *
   * @param filepath the String representing where the file should be saved
   */
  void writeConfiguration(String filepath);
}
