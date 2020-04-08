package ooga.data;

public class StyleData implements IStyle {


    private String myLanguage;
    private boolean isDark;

    public StyleData(String styleXML) {
        // TODO: Make this work
    }

    /**
     * Gives the active language stored in the style settings
     *
     * @return String representing language
     */
    @Override
    public String getLanguage() {
        return null;
    }

    /**
     * Sets the active language to be stored in the style settings
     *
     * @param lang String representing language
     */
    @Override
    public void setLanguage(String lang) {

    }

    /**
     * Gives whether or not dark mode is enabled in the style settings
     *
     * @return boolean representing dark mode
     */
    @Override
    public boolean getDarkMode() {
        return false;
    }

    /**
     * Sets whether or not dark mode is enabled in the style settings
     *
     * @param dark boolean representing dark mode
     */
    @Override
    public void setDarkMode(boolean dark) {

    }

    /**
     * Gives the difficulty level stored in the style settings
     *
     * @return int representing difficulty
     */
    @Override
    public int getDifficulty() {
        return 0;
    }

    /**
     * Sets the difficulty level stored in the style settings
     *
     * @param diff int representing difficulty
     */
    @Override
    public void setDifficulty(int diff) {

    }

    /**
     * Gives whether or not sound was enabled in the style settings
     *
     * @return boolean representing sound
     */
    @Override
    public boolean getSound() {
        return false;
    }

    /**
     * Sets the sound enabled setting in the style settings
     *
     * @param sound boolean representing sound
     */
    @Override
    public void setSound(boolean sound) {

    }

    /**
     * Gives the file path to active card skin pack in the style settings
     *
     * @return String filepath to skin pack
     */
    @Override
    public String getCardSkinPath() {
        return null;
    }

    /**
     * Sets the file path to active card skin pack in the style settings
     *
     * @param path String filepath to skin pack
     */
    @Override
    public void setCardSkinPath(String path) {

    }

    /**
     * Gives the file path to active table skin in the style settings
     *
     * @return String filepath to skin
     */
    @Override
    public String getTableSkinPath() {
        return null;
    }

    /**
     * Sets the file path to active table skin in the style settings
     *
     * @param path String filepath to skin
     */
    @Override
    public void setTableSkinPath(String path) {

    }

    private void updateXML() {
        // TODO: Make a new XML file with all these style preferences
    }
}
