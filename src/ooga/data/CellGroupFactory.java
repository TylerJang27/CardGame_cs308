package ooga.data;

import ooga.data.rules.ICellGroup;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import java.util.List;
import java.util.ResourceBundle;

//TODO: ADD DOCUMENTATION
public class CellGroupFactory {
    public static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
    private static final String CELL_GROUP = "cell_group";
    private static final ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_PACKAGE+CELL_GROUP);

    public static String INVALID_ERROR = "INVALID_FILE";
    public static String MISSING_ERROR = "MISSING_ATTRIBUTE";

    private static final String GROUP = "Group";
    private static final String CATEGORY = "Category";
    private static final String CELL = "Cell";
    private static final String NAME = "Name";
    private static final String FAN = "Fan";
    private static final String ROTATION = "Rotation";
    private static final String INIT_CARD = "InitCard";
    private static final String CARD = "Card";

    private static DocumentBuilder documentBuilder;

    public CellGroupFactory() { documentBuilder = XMLHelper.getDocumentBuilder();}

    public static List<ICellGroup> getCellGroups(Element root) {

    }


}
