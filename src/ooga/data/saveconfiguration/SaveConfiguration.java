package ooga.data.saveconfiguration;

import java.util.HashMap;
import java.util.Map;
import ooga.cardtable.Cell;
import ooga.cardtable.ICell;

/**
 * This class implements ISaveConfiguration and stores information from a game save state.
 *
 * @author Tyler Jang
 */
public class SaveConfiguration implements ISaveConfiguration {

  private String myGameName;
  private String myRulePath;
  private String myPhaseName;
  private Double myScore;
  private Map<String, String> myCellBuilderMap;

  /**
   * The Constructor for SaveConfiguration. Takes in game name information, the path to the rules,
   * the name of the current phase, the score, and a Map to build cell information.
   *
   * @param gameName    the String representing the game name
   * @param rulePath    the String representing the path to the rules file
   * @param phaseName   the String representing the name of the current phase
   * @param score       the Double representing the current score
   * @param cellBuilder the Map of Strings to Strings to build the cells
   */
  public SaveConfiguration(String gameName, String rulePath, String phaseName, Double score,
      Map<String, String> cellBuilder) {
    myGameName = gameName;
    myRulePath = rulePath;
    myPhaseName = phaseName;
    myScore = score;
    myCellBuilderMap = cellBuilder;
  }

  /**
   * Retrieves the name of the game that was stored.
   *
   * @return a String representing the game name
   */
  @Override
  public String getGameName() {
    return myGameName;
  }

  /**
   * Retrieves the file path of the rules.
   *
   * @return a String representing the path to the rules file
   */
  @Override
  public String getRulePath() {
    return myRulePath;
  }

  /**
   * Retrieves the name of the current phase at save.
   *
   * @return a String representing the current phase
   */
  @Override
  public String getCurrentPhase() {
    return myPhaseName;
  }

  /**
   * Retrieves the current phase at save.
   *
   * @return a Double representing the score
   */
  @Override
  public Double getScore() {
    return myScore;
  }

  /**
   * Retrieves the cell map present at save.
   *
   * @return a Map of String ICell names to ICells
   */
  @Override
  public Map<String, ICell> getCellMap() {
    Map<String, ICell> cellMap = new HashMap<>();
    for (Map.Entry<String, String> e : myCellBuilderMap.entrySet()) {
      cellMap.put(e.getKey(), Cell.fromStorageString(e.getValue()));
    }
    return cellMap;
  }

  @Override
  /**
   * Writes the SaveConfiguration information to the given filepath.
   *
   * @param filepath  the String representing where the file should be saved
   */
  public void writeConfiguration(String filepath) {
    SaveConfigurationWriter.writeSave(filepath, this);
  }
}
