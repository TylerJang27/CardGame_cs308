package ooga.view;

import java.util.List;
import java.util.Map;
import ooga.cardtable.ICell;
import ooga.cardtable.IMove;
import ooga.data.style.ILayout;

public interface ExternalAPI {

  int createGame(String gameName);

  /**
   * report an error where the key is the key to a error message in a properties file and the
   * formats are the error-specific information
   *
   * @param key
   * @param formats
   */
  void reportError(String key, List<String> formats); //TODO: ADD TO API CHANGES

  /**
   * setCellData() is called regularly by the Controller to pass the correct state of the board to
   * the front end from the back end. This is done by sending a list of cell objects which represent
   * groups of cards and their associated state (i.e. face up/down, staggered/even, card type)
   *
   * @param cellData
   */
  void setCellData(int gameID, Map<String, ICell> cellData);

  /**
   * setCellData() is called regularly by the Controller to pass the correct state of the board to
   * the front end from the back end. This is done by sending a list of cell objects which represent
   * groups of cards and their associated state (i.e. face up/down, staggered/even, card type)
   *
   * @param cellData
   */
  void setUpdatesToCellData(int gameID, Map<String, ICell> cellData);

  /**
   * Sets score of players to be displayed
   *
   * @param playerScores maps playerID to total score
   */
  void setScores(int gameID, Map<Integer, Double> playerScores);

  /**
   * If triggered by player move, please call setCellData() first so that the most recent
   * arrangement of cards can be displayed prior to the game over screen. This function ends the
   * game, progressing to a "high score" or some other end game screen.
   *
   * @param playerOutcomes maps a player integer to their status at the end of a game
   */
  void endGame(Map<Integer, Boolean> playerOutcomes, Map<Integer, Double> playerScores,
      Map<Integer, Integer> highScores);

  /**
   * Similar to endGame, but rather than ending game removes a player from the game, with a message
   * indicating the updated status of that player. For example, in UNO a player may win while other
   * players continue on. This function allows the controller to signal that a player, player i, has
   * won the game, but the frontend will not return to the start menu or display high scores, but
   * rather continue operating as it had.
   *
   * @param playerOutcomes maps a player's ID number to their Boolean win/lose (true/false) status.
   *                       If a player's status is unchanged, do not include these player's ID
   *                       numbers in playerOutcomes. Only include the player's who have either won
   *                       or lost before the game is ended.
   * @param playerScores   maps a player's ID number to their Double score. If scoring is not
   *                       enabled for the current game, the contents of playerScores will be
   *                       ignored.
   */
  void playerStatusUpdate(Map<Integer, Boolean> playerOutcomes, Map<Integer, Integer> playerScores);

  /**
   * Returns a boolean indicating whether a user has made a change since the Controller's last call
   * to getUserInput().
   */
  boolean isUserInput();

  /**
   * getUserInput() is called regularly by the Controller to obtain the new move made by any
   * player.
   * <p>
   * Sets isUserInput() to false, pending a new move from any player.
   *
   * @return a map from the clicked on object to the released on object of the user's action
   */
  IMove getUserInput();

  /**
   * Sets the locations of all cell types and the framework for creating new cell locations if
   * applicable.
   */
  void setLayout(int gameID, ILayout layout);

}
