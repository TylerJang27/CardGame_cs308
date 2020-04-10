package ooga.data.factories;

import ooga.cardtable.*;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.rules.CellGroup;
import ooga.data.rules.ICellGroup;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.util.*;
import java.util.function.Function;

//TODO: ADD DOCUMENTATION
public class CellGroupFactory implements Factory{
    private static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
    private static final String CELL_GROUP = "cell_group";
    private static final ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_PACKAGE+CELL_GROUP);

    private static final String GROUP = "Group";
    private static final String CELL_GROUPS = "CellGroups";
    private static final String CATEGORY = "Category";
    private static final String CELL = "Cell";
    private static final String NAME = "Name";
    private static final String FAN = "Fan";
    private static final String ROTATION = "Rotation";
    private static final String INIT_CARD = "InitCard";
    private static final String CARD = "Card";

    private static DocumentBuilder documentBuilder;

    public CellGroupFactory() { documentBuilder = XMLHelper.getDocumentBuilder();}

    public static Map<String, ICellGroup> getCellGroups(Element root) {
        try {
            Node groupHeader = root.getElementsByTagName(resources.getString(CELL_GROUPS)).item(0);
            NodeList groups = ((Element)groupHeader).getElementsByTagName(resources.getString(GROUP));
            Map<String, ICell> allCells = new HashMap<>();
            Map<String, ICellGroup> allCellGroups = new HashMap<>();
            for (int k = 0; k < groups.getLength(); k ++) {
                ICellGroup newGroup = buildGroup((Element)groups.item(k), allCells);//
                allCellGroups.put(newGroup.getName(), newGroup);
            }
            return allCellGroups;
        } catch (Exception e) {
            throw new XMLException(e, MISSING_ERROR + "," + CELL_GROUP);
        }
    }

    private static ICellGroup buildGroup(Element group, Map<String, ICell> cellMap) {
        String groupName = XMLHelper.getAttribute(group, resources.getString(CATEGORY));
        NodeList cells = group.getElementsByTagName(resources.getString(CELL));
        Map<String, ICell> newCells = new HashMap<>();
        for (int k = 0; k < cells.getLength(); k ++) {
            ICell newCell = buildCell((Element)cells.item(k), cellMap);
            newCells.put(newCell.getName(), newCell);
        }
        return new CellGroup(groupName, newCells);
    }

    private static ICell buildCell(Element cell, Map<String, ICell> cellMap) {
        String cellName = XMLHelper.getAttribute(cell, resources.getString(NAME));
        if (cellMap.keySet().contains(cellName)) {
            return cellMap.get(cellName);
        } else {
            //TODO: ADD MORE DEFENSIVE CODING AND CHECKS HERE FOR SAFETY
            String offsetName = Factory.getVal(cell, FAN, resources).strip();
            IOffset offset = Offset.valueOf(offsetName.toUpperCase());
            Double rotation = Double.parseDouble(Factory.getVal(cell, ROTATION, resources)); //TODO: IMPLEMENT ROTATION

            Node initializeSettings = XMLHelper.getNodeByName(cell.getChildNodes(), resources.getString(INIT_CARD));
            Function<IDeck, ICell> initializer = InitializeFactory.getInitialization(initializeSettings, offset, rotation);

            ICell builtCell = new Cell(cellName);
            builtCell.setDraw(initializer);
            cellMap.put(cellName, builtCell);
            return builtCell;
        }
    }
}
