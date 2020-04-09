package ooga.data;

import ooga.data.rules.ISettings;
import ooga.data.rules.Settings;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import java.util.Map;
import java.util.ResourceBundle;

//TODO: ADD DOCUMENTATION
public class SettingsFactory implements Factory {
    private static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
    private static final String SETTINGS = "settings";
    private static final ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_PACKAGE+SETTINGS);

    private static final String PLAYERS = "Players";
    private static final String LAYOUT = "Layout";
    private static final String DEFAULT_PLAYERS = "1";

    private static DocumentBuilder documentBuilder;

    public SettingsFactory() { documentBuilder = XMLHelper.getDocumentBuilder();}

    public static ISettings getSettings(Element root) {
        Map<String, String> settings = XMLHelper.readStringSettings(root, resources);

        int numPlayers = Integer.parseInt(settings.getOrDefault(resources.getString(PLAYERS), DEFAULT_PLAYERS));
        String layout = settings.getOrDefault(resources.getString(LAYOUT), "");
        return new Settings(numPlayers, layout);
    }
}
