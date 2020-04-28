package ooga.data.style;

/**
 * This interface governs styling information to be parsed and passed to the frontend. This
 * information may also be written and saved back into XML files.
 *
 * @author Tyler Jang, Andrew Krier
 */
public interface IStyle {

  String DATA_TYPE = "style";

  /**
   * Gives the active language stored in the style settings.
   *
   * @return String representing language
   */
  String getLanguage();

  /**
   * Sets the active language to be stored in the style settings.
   *
   * @param lang String representing language
   */
  void setLanguage(String lang);

  /**
   * Gives whether or not dark mode is enabled in the style settings.
   *
   * @return boolean representing dark mode
   */
  boolean getDarkMode();

  /**
   * Sets whether or not dark mode is enabled in the style settings.
   *
   * @param dark boolean representing dark mode
   */
  void setDarkMode(boolean dark);

  /**
   * Gives the difficulty level stored in the style settings.
   *
   * @return int representing difficulty
   */
  int getDifficulty();

  /**
   * Sets the difficulty level stored in the style settings.
   *
   * @param diff int representing difficulty
   */
  void setDifficulty(int diff);

  /**
   * Gives whether or not sound was enabled in the style settings.
   *
   * @return boolean representing sound
   */
  boolean getSound();

  /**
   * Sets the sound enabled setting in the style settings.
   *
   * @param sound boolean representing sound
   */
  void setSound(boolean sound);

  /**
   * Gives the file path to active card skin pack in the style settings.
   *
   * @return String filepath to skin pack
   */
  String getCardSkinPath();

  /**
   * Sets the file path to active card skin pack in the style settings.
   *
   * @param path String filepath to skin pack
   */
  void setCardSkinPath(String path);

  /**
   * Gives the file path to active table skin in the style settings.
   *
   * @return String filepath to skin
   */
  String getTheme();

  /**
   * Sets the file path to active table skin in the style settings.
   *
   * @param path String filepath to skin
   */
  void setTheme(String path);

  /**
   * Saves the settings to an XML file.
   */
  void saveSettings();

  /**
   * Returns whether or not an IStyle implementation is equal to this.
   *
   * @param o the object to compare
   * @return whether or not the objects are equal
   */
  @Override
  boolean equals(Object o);
}
