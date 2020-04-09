package ooga.data;

import ooga.cardtable.ICell;
import ooga.data.rules.ICellGroup;
import ooga.data.rules.IPhase;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import java.util.Map;
import java.util.ResourceBundle;

public class PhaseFactory implements Factory {
    private static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
    private static final String PHASES = "phases";
    private static final ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_PACKAGE+PHASES);




    private static DocumentBuilder documentBuilder;

    public PhaseFactory() { documentBuilder = XMLHelper.getDocumentBuilder();}

    public static Map<String, IPhase> getPhases(Element root, Map<String, ICellGroup> cellGroupMap, Map<String, ICell> cellMap) {

    }

}
