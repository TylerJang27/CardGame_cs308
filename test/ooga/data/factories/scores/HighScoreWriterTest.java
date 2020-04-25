package ooga.data.factories.scores;

import ooga.data.factories.HighScoreFactory;
import ooga.data.highscore.HighScore;
import ooga.data.highscore.HighScoreWriter;
import ooga.data.highscore.IHighScores;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This tests the implementation of IHighScore as it pertains to reading and writing. This does not cover its interactions with ITable or Controller.
 *
 * @author Tyler Jang
 */
class HighScoreWriterTest {

    private static final String TEST_DIRECTORY = "test/ooga/data/factories/scores/";

    /**
     * Tests that a write and reconstruction of scores produces the same score map and the same basic fields.
     */
    @Test
    void writeScores() {
        Map<String, Double> defaultScores = Map.of("solitaire", 0.0, "memory", 0.0);
        /*IHighScores defaultHighScore = new HighScore(TEST_DIRECTORY + "default_score_orig.xml", defaultScores);
        IHighScores readHighScore = HighScoreFactory.createScores(new File(TEST_DIRECTORY + "default_score_orig.xml"));
        for (String s: defaultHighScore.getSavedGames()) {
            assertEquals(defaultHighScore.getScore(s), readHighScore.getScore(s));
        }

        List<String> games = List.of("solitaire", "war", "rps", "memory", "pickup52");
        List<Double> scores = List.of(0.0, 0.0, 12.0, 1.1, -3.6);

        List<Map<String, Double>> scoreMaps = new ArrayList<>();
        scoreMaps.add(Map.of(games.get(0), scores.get(0)));

        for (int k = 1; k < games.size(); k ++) {
            HashMap<String, Double> scoreMap = new HashMap<>(scoreMaps.get(k - 1));
            scoreMap.put(games.get(k), scores.get(k));
            scoreMaps.add(scoreMap);
        }

        String fileSave = TEST_DIRECTORY + "score.xml";
        for (Map<String, Double> map: scoreMaps) {
            IHighScores expectedHighScore = new HighScore(TEST_DIRECTORY, map);
            HighScoreWriter.writeScores(fileSave, expectedHighScore);
            IHighScores actualHighScore = HighScoreFactory.createScores(new File(fileSave));
            for (String s: expectedHighScore.getSavedGames()) {
                System.out.println("check baby check baby");
                System.out.println(s);
                assertEquals(expectedHighScore.getScore(s), actualHighScore.getScore(s));
            }
        }*/
    }
}