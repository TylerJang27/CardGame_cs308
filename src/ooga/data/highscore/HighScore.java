package ooga.data.highscore;

import java.util.Map;
import java.util.Set;

/**
 * This class stores HighScore information to later write it to XML files.
 *
 * @author Tyler Jang
 */
public class HighScore implements IHighScores {

    private Map<String, Double> myScoreMap;
    private String saveFilePath;

    /**
     * The Constructor for HighScore, setting the high score map.
     *
     * @param xmlFile           the location to which the file should be saved
     * @param scores a Map of String game names to Double high scores
     */
    public HighScore(String xmlFile, Map<String, Double> scores) {
        saveFilePath = xmlFile;
        myScoreMap = scores;
    }

    /**
     * Gets the List of all saved games.
     *
     * @return a Set of Strings representing saved games
     */
    @Override
    public Set<String> getSavedGames() {
        return myScoreMap.keySet();
    }

    /**
     * Saves the scores to an XML file.
     */
    @Override
    public void saveScores() {
        HighScoreWriter.writeScores(saveFilePath, this);
    }

    /**
     * Retrieves the score associated with a particular game.
     *
     * @param name the name of the game being queried
     * @return
     */
    @Override
    public double getScore(String name) {
        return myScoreMap.getOrDefault(name.toLowerCase(), Double.MIN_VALUE);
    }

    /**
     * Replaces a score if already present, or adds a new score for a given game.
     *
     * @param name  the name of the game being set
     * @param score the high score for the game being set
     */
    @Override
    public void setScore(String name, double score) {
        myScoreMap.put(name.toLowerCase(), score);
        saveScores();
    }
}
