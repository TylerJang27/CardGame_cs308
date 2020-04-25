package ooga.data.factories.scores;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ooga.data.factories.HighScoreFactory;
import ooga.data.highscore.HighScore;
import ooga.data.highscore.HighScoreWriter;
import ooga.data.highscore.IHighScores;
import org.junit.jupiter.api.Test;

/**
 * This tests the implementation of IHighScore as it pertains to reading and writing. This does not
 * cover its interactions with ITable or Controller.
 *
 * @author Tyler Jang
 */
class HighScoreWriterTest {

  private static final String TEST_DIRECTORY = "test/ooga/data/factories/scores/";

  /**
   * Tests that a write and reconstruction of scores produces the same score map and the same basic
   * fields.
   */
  @Test
  void writeScores() {
    Map<String, List<Double>> defaultScores = Map
        .of("solitaire", new ArrayList<Double>(Arrays.asList(0.0)), "memory",
            new ArrayList<Double>(Arrays.asList(0.0)));

    IHighScores defaultHighScore = new HighScore(TEST_DIRECTORY + "default_score_orig.xml",
        defaultScores);
    IHighScores readHighScore = HighScoreFactory
        .createScores(new File(TEST_DIRECTORY + "default_score_orig.xml"));
    for (String s : defaultHighScore.getSavedGames()) {
      for (int k = 0; k < defaultHighScore.getScore(s).size(); k++) {
        Double defaultScore = new ArrayList<>(defaultHighScore.getScore(s)).get(k);
        Double readScore = new ArrayList<>(readHighScore.getScore(s)).get(k);
        assertEquals(defaultScore, readScore);
      }
    }

    List<String> games = List.of("solitaire", "war", "rps", "memory", "pickup52");
    List<Double> scores = new ArrayList<>(Arrays.asList(0.0, 0.0, 12.0, 1.1, -3.6));

    List<Map<String, List<Double>>> scoreMaps = new ArrayList<>();
    scoreMaps.add(Map.of(games.get(0), new ArrayList<>(Arrays.asList(scores.get(0)))));

    for (int k = 1; k < games.size(); k++) {
      Map<String, List<Double>> scoreMap = new HashMap<>();
      scoreMap.putAll(scoreMaps.get(k - 1));
      scoreMap.put(games.get(k), new ArrayList<>(Arrays.asList(scores.get(k))));
      scoreMaps.add(scoreMap);
    }

    String fileSave = TEST_DIRECTORY + "score.xml";
    for (Map<String, List<Double>> map : scoreMaps) {
      IHighScores expectedHighScore = new HighScore(TEST_DIRECTORY, map);
      HighScoreWriter.writeScores(fileSave, expectedHighScore);
      IHighScores actualHighScore = HighScoreFactory.createScores(new File(fileSave));
      for (String s : expectedHighScore.getSavedGames()) {
        for (int k = 0; k < expectedHighScore.getScore(s).size(); k++) {
          Double expected = new ArrayList<>(expectedHighScore.getScore(s)).get(k);
          Double actual = new ArrayList<>(actualHighScore.getScore(s)).get(k);
          assertEquals(expected, actual);
        }
      }
    }
  }
}