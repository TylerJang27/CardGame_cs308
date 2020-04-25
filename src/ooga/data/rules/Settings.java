package ooga.data.rules;

/**
 * Class that implements ISettings and stores information about a game's settings.
 *
 * @author Tyler Jang, Andrew Krier
 */
public class Settings implements ISettings {

  int numPlayers;
  String layoutPath;

  /**
   * The constructor for Settings, storing information on the players and the layout
   *
   * @param players the number of players
   * @param layout  the path to the ILayout file
   */
  public Settings(int players, String layout) {
    numPlayers = players;
    layoutPath = layout;
  }

  /**
   * The default constructor for Settings, storing a default of 1 player and no file path
   */
  public Settings() {
    this(1, "");
  }

  /**
   * Retrieves the number of players for a given game
   *
   * @return the number of players
   */
  @Override
  public int getPlayers() {
    return numPlayers;
  }

  /**
   * Retrieves the filepath to the ILayout XML of cells
   *
   * @return String representing a filepath
   */
  @Override
  public String getLayout() {
    return layoutPath;
  }
}
