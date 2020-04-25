package ooga.data.style;

import java.util.Map;

/**
 * This class governs styling information to be passed to the frontend and saved based on user
 * customization.
 *
 * @author Tyler Jang, Andrew Krier
 */
public class StyleData implements IStyle {

  private String saveFilePath;

  private String myLanguage;
  private String myCards;
  private String myTable;

  private boolean myDark;
  private int myDifficulty;
  private boolean mySound;

  private static final String LANGUAGE = "language";
  private static final String CARDS = "cards";
  private static final String TABLE = "table";

  private static final String DARK = "dark";
  private static final String DIFFICULTY = "difficulty";
  private static final String SOUND = "sound";

  /**
   * The Constructor for StyleData, setting the appropriate settings and save location.
   *
   * @param xmlFile        the location to which the file should be saved
   * @param wordSettings   a Map of String word setting types to their String values
   * @param numberSettings A Map of String number setting types to their Integer values
   */
  public StyleData(String xmlFile, Map<String, String> wordSettings,
      Map<String, Integer> numberSettings) {
    saveFilePath = xmlFile;
    myLanguage = wordSettings.get(LANGUAGE);
    myCards = wordSettings.get(CARDS);
    myTable = wordSettings.get(TABLE);

    myDark = numberSettings.get(DARK) == 1;
    myDifficulty = numberSettings.get(DIFFICULTY);
    mySound = numberSettings.get(SOUND) == 1;
  }

  /**
   * Gives the active language stored in the style settings
   *
   * @return String representing language
   */
  @Override
  public String getLanguage() {
    return myLanguage;
  }

  /**
   * Sets the active language to be stored in the style settings
   *
   * @param lang String representing language
   */
  @Override
  public void setLanguage(String lang) {
    myLanguage = lang;
    saveSettings();
  }

  /**
   * Gives whether or not dark mode is enabled in the style settings
   *
   * @return boolean representing dark mode
   */
  @Override
  public boolean getDarkMode() {
    return myDark;
  }

  /**
   * Sets whether or not dark mode is enabled in the style settings
   *
   * @param dark boolean representing dark mode
   */
  @Override
  public void setDarkMode(boolean dark) {
    myDark = dark;
    saveSettings();
  }

  /**
   * Gives the difficulty level stored in the style settings
   *
   * @return int representing difficulty
   */
  @Override
  public int getDifficulty() {
    return myDifficulty;
  }

  /**
   * Sets the difficulty level stored in the style settings
   *
   * @param diff int representing difficulty
   */
  @Override
  public void setDifficulty(int diff) {
    myDifficulty = diff;
    saveSettings();
  }

  /**
   * Gives whether or not sound was enabled in the style settings
   *
   * @return boolean representing sound
   */
  @Override
  public boolean getSound() {
    return mySound;
  }

  /**
   * Sets the sound enabled setting in the style settings
   *
   * @param sound boolean representing sound
   */
  @Override
  public void setSound(boolean sound) {
    mySound = sound;
    saveSettings();
  }

  /**
   * Gives the file path to active card skin pack in the style settings
   *
   * @return String filepath to skin pack
   */
  @Override
  public String getCardSkinPath() {
    return myCards;
  }

  /**
   * Sets the file path to active card skin pack in the style settings
   *
   * @param path String filepath to skin pack
   */
  @Override
  public void setCardSkinPath(String path) {
    myCards = path;
    saveSettings();
  }

  /**
   * Gives the file path to active table skin in the style settings
   *
   * @return String filepath to skin
   */
  @Override
  public String getTheme() {
    return myTable;
  }

  /**
   * Sets the file path to active table skin in the style settings
   *
   * @param path String filepath to skin
   */
  @Override
  public void setTheme(String path) {
    myTable = path;
    saveSettings();
  }

  /**
   * Saves the settings to an XML file
   */
  public void saveSettings() {
    StyleWriter.writeStyle(saveFilePath, this);
  }

  /**
   * Returns whether or not an IStyle implementation is equal to this.
   *
   * @param o the object to compare
   * @return whether or not the objects are equal
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof StyleData) {
      StyleData styleData = (StyleData) o;
      return getDifficulty() == styleData.getDifficulty() &&
          getDarkMode() == styleData.getDarkMode() &&
          getSound() == styleData.getSound() &&
          getCardSkinPath().equals(styleData.getCardSkinPath()) &&
          getLanguage().equalsIgnoreCase(styleData.getLanguage()) &&
          getTheme().equals(styleData.getTheme());
    }
    return false;
  }
}
