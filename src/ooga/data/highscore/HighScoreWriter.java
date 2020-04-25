package ooga.data.highscore;

import javax.xml.transform.TransformerException;
import ooga.data.Writer;
import ooga.data.XMLException;
import ooga.data.factories.Factory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Class for writing high score information into an XML file. Saves highest scores.
 *
 * @author Tyler Jang
 */
public class HighScoreWriter implements Writer {

  private static final String DATA_TYPE = IHighScores.DATA_TYPE;
  private static final String SCORE = IHighScores.SCORE;

  private HighScoreWriter() {
  }

  /**
   * Writes information from IHighScore implementation to filepath
   *
   * @param filepath the destination for the XML file
   * @param scores   the IHighScore from which to build the XML file
   */
  public static void writeScores(String filepath, IHighScores scores) {
    try {
      Element root = Writer.buildDocumentWithRoot(DATA_TYPE);
      Document document = root.getOwnerDocument();

      addScores(document, root, scores);

      Writer.writeOutput(document, filepath);
    } catch (TransformerException e) {
      throw new XMLException(e, Factory.UNKNOWN_ERROR);
    }
  }

  /**
   * Extracts information from high scores to add to the document
   *
   * @param document the document to which data should be added
   * @param root     the root Element of the document
   * @param scores   the IHighScore implementation from which to parse data
   */
  private static void addScores(Document document, Element root, IHighScores scores) {
    for (String s : scores.getSavedGames()) {
      try {
        Element e = document.createElement(s);
        for (Double score : scores.getScore(s)) {
          Element singleScore = document.createElement(SCORE);
          singleScore.appendChild(document.createTextNode("" + score));
          e.appendChild(singleScore);
        }
        root.appendChild(e);
      } catch (Exception e) {
      }
    }
  }
}
