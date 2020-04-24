package ooga.data.factories;

import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.XMLValidator;
import ooga.data.highscore.HighScore;
import ooga.data.highscore.IHighScores;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * This HighScoreFactory implements Factory and constructs an IHighScore using the createHighScore() method.
 * HighScore information is saved locally across games.
 *
 * @author Tyler Jang
 */
public class HighScoreFactory implements Factory {

    public static String DATA_TYPE = IHighScores.DATA_TYPE;
    public static String SCORE_XSD = "src/ooga/data/factories/schemas/score_schema.xsd";

    /**
     * Builds and returns an IHighScore from a scoring XML. Requirements for scoring XML can be found in ___.
     *
     * @param dataFile    file from which to read configuration
     * @return an IHighScore with all of its configuration information stored
     * @throws XMLException if the file is not considered valid due to its root element or file ending
     */
    public static IHighScores createScores(File dataFile) {
        return createScores(dataFile, dataFile.getPath());
    }

    /**
     * Builds and returns an IHighScore from a scoring XML. Requirements for scoring XML can be found in ___.
     *
     * @param dataFile    file from which to read configuration
     * @param destination String for the destination to save the file
     * @return an IHighScore with all of its configuration information stored
     * @throws XMLException if the file is not considered valid due to its root element or file ending
     */
    public static IHighScores createScores(File dataFile, String destination) {
        if (XMLValidator.validateXMLSchema(SCORE_XSD, dataFile.getPath())) {
            try {
                Element root = XMLHelper.getRootAndCheck(dataFile, DATA_TYPE, INVALID_ERROR);
                NodeList scoreNodes = root.getChildNodes();

                Map<String, Double> scoreMap = new HashMap<>();

                for (int k = 0; k < scoreNodes.getLength(); k ++) {
                    Node scoreNode = scoreNodes.item(k);
                    try {
                        scoreMap.put(scoreNode.getNodeName(), Double.parseDouble(scoreNode.getTextContent()));
                        System.out.println(scoreNode.getNodeName() + ", " + Double.parseDouble(scoreNode.getTextContent()));
                    } catch (NumberFormatException | DOMException e) {
                        scoreMap.put(scoreNode.getNodeName(), 0.0);
                    }
                }

                return new HighScore(destination, scoreMap);
            } catch (Exception e) {
                throw new XMLException(e, Factory.MISSING_ERROR + "," + DATA_TYPE);
            }
        } else {
            throw new XMLException((Factory.INVALID_ERROR));
        }
    }
}
