package ooga.data.factories;

import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.rules.ISettings;
import ooga.data.rules.Settings;
import org.w3c.dom.Element;

import java.util.Map;
import java.util.ResourceBundle;

/**
 * This SettingsFactory implements Factory and constructs an ISettings using the getSettings() method.
 * This ISettings is used to store information about rules and configuration for a given game, including the filepath to its layout file.
 *
 * @author Tyler Jang
 */
public class SettingsFactory implements Factory {
    private static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
    private static final String SETTINGS = "settings";
    private static final ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_PACKAGE + SETTINGS);

    private static final String PLAYERS = "Players";
    private static final String LAYOUT = "Layout";
    private static final String DEFAULT_PLAYERS = "1";

    /**
     * Builds and returns an ISettings built from a rules XML. Requirements for rules XML can be found in ____.
     *
     * @param root the root of the file from which an ISettings is built
     * @return an ISettings implementation storing information about setup and game play
     */
    public static ISettings getSettings(Element root) {
        try {
            Map<String, String> settings = XMLHelper.readStringSettings(root, resources);

            int numPlayers = Integer.parseInt(settings.getOrDefault(resources.getString(PLAYERS), DEFAULT_PLAYERS));
            String layout = settings.getOrDefault(resources.getString(LAYOUT), "");
            return new Settings(numPlayers, layout);
        } catch (Exception e) {
            throw new XMLException(e, Factory.MISSING_ERROR + "," + SETTINGS);
        }
    }
}
