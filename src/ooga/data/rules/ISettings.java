package ooga.data.rules;

/**
 * Stores information about overall settings for a game.
 *
 * @author Tyler Jang, Andrew Krier
 */
public interface ISettings {

  /**
   * Retrieves the number of players for a given game
   *
   * @return the number of players
   */
  int getPlayers();

  /**
   * Retrieves the filepath to the ILayout XML of cells
   *
   * @return String representing a filepath
   */
  String getLayout();
}
