package ooga.data.style;

import java.util.Map;

/**
 * This interface stores layout information to be passed to the view for display.
 *
 * @author Andrew Krier, Tyler Jang
 */
public interface ILayout {

  String DATA_TYPE = "layout";

  /**
   * Gives a map of all the cells in a game with their respective names as their keys
   *
   * @return a map of of cell names to their coordinates, given as decimals relative to the screen
   * size
   */
  Map<String, ICoordinate> getCellLayout();

  /**
   * Gives number of players allowed in this game
   *
   * @return
   */
  int getNumPlayers();

  /**
   * Gives the screen height value relative to screen width
   *
   * @return
   */
  double getScreenRatio();

  /**
   * Gives the card width value relative to screen width
   *
   * @return
   */
  double getCardWidthRatio();

  /**
   * Gives the card height value relative to screen width
   *
   * @return
   */
  double getCardHeightRatio();

  /**
   * Gives the offset for face down cards relative to screen width
   *
   * @return
   */
  double getDownOffsetRatio();

  /**
   * Gives the offset for face up cards relative to screen width
   *
   * @return
   */
  double getUpOffsetRatio();

  /**
   * Gives screen width
   *
   * @return the double screen width
   */
  double getScreenWidth();

  /**
   * Gives screen height
   *
   * @return the double screen height
   */
  double getScreenHeight();

  /**
   * Gives the ratio between the card height and width ratio = height / width
   *
   * @return
   */
  double getCardRatio();

  /**
   * Returns the map relating card names to their image paths relative to the display table class
   *
   * @return
   */
  Map<String, String> getCardImagePaths();
}
