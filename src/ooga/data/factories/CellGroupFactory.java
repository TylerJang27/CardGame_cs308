package ooga.data.factories;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import ooga.cardtable.Cell;
import ooga.cardtable.ICell;
import ooga.cardtable.IDeck;
import ooga.cardtable.IOffset;
import ooga.cardtable.Offset;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.rules.CellGroup;
import ooga.data.rules.ICellGroup;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This CellGroupFactory implements Factory and constructs ICellGroups using the createCellGroups()
 * method. These ICellGroups contain their own ICells with their own names and ability to hold other
 * cells, decks, and cards.
 * <p>
 * This Factory depends on InitializeFactory working properly.
 *
 * @author Tyler Jang
 */
public class CellGroupFactory implements Factory {

  private static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
  private static final String CELL_GROUP = "cell_group";
  private static final ResourceBundle resources = ResourceBundle
      .getBundle(RESOURCE_PACKAGE + CELL_GROUP);

  private static final String GROUP = "Group";
  private static final String CELL_GROUPS = "CellGroups";
  private static final String CATEGORY = "Category";
  private static final String CELL = "Cell";
  private static final String NAME = "Name";
  private static final String FAN = "Fan";
  private static final String ROTATION = "Rotation";
  private static final String INIT_CARD = "InitCard";

  private CellGroupFactory() {
  }

  /**
   * Builds and returns ICellGroups built from a rules XML. Requirements for rules XML can be found
   * in doc/XML_Documentation.md.
   *
   * @param root the root of the file from which ICellGroups are built
   * @return a Map of String cell group names to ICellGroup implementations
   */
  public static Map<String, ICellGroup> createCellGroups(Element root) {
    try {
      Node groupHeader = root.getElementsByTagName(resources.getString(CELL_GROUPS)).item(0);
      NodeList groups = ((Element) groupHeader).getElementsByTagName(resources.getString(GROUP));
      Map<String, ICell> allCells = new HashMap<>();
      Map<String, ICellGroup> allCellGroups = new HashMap<>();
      for (int k = 0; k < groups.getLength(); k++) {
        ICellGroup newGroup = buildGroup((Element) groups.item(k), allCells);//
        allCellGroups.put(newGroup.getName(), newGroup);
      }
      return allCellGroups;
    } catch (Exception e) {
      throw new XMLException(e, MISSING_ERROR + "," + CELL_GROUP);
    }
  }

  /**
   * Builds and returns a singular ICellGroup implementation with its internal ICells. If an ICell
   * name inside of an ICellGroup already exists, it will not be recreated (names must be unique).
   *
   * @param group   the Element from which to build the ICellGroup
   * @param cellMap a Map of existing String ICell names to ICells
   * @return a fully built ICellGroup
   */
  private static ICellGroup buildGroup(Element group, Map<String, ICell> cellMap) {
    String groupName = XMLHelper.getAttribute(group, resources.getString(CATEGORY));
    NodeList cells = group.getElementsByTagName(resources.getString(CELL));
    Map<String, ICell> newCells = new HashMap<>();
    for (int k = 0; k < cells.getLength(); k++) {
      ICell newCell = buildCell((Element) cells.item(k), cellMap);
      newCells.put(newCell.getName(), newCell);
    }
    return new CellGroup(groupName, newCells);
  }

  /**
   * Builds and returns a singular ICell implementation with its internal initialization settings.
   * Adds the newly built cell to cellMap.
   *
   * @param cell    the Element from which to built the new ICell
   * @param cellMap a Map of existing String ICell names to ICells
   * @return a fully built ICell
   */
  private static ICell buildCell(Element cell, Map<String, ICell> cellMap) {
    String cellName = XMLHelper.getAttribute(cell, resources.getString(NAME));
    if (cellMap.containsKey(cellName)) {
      return cellMap.get(cellName);
    } else {
      IOffset offset = extractCellOffset(cell);
      Double rotation = extractCellRotation(cell);
      Node initializeSettings = XMLHelper
          .getNodeByName(cell.getChildNodes(), resources.getString(INIT_CARD));
      Function<IDeck, ICell> initializer = InitializeFactory
          .createInitialization(initializeSettings, offset, rotation);

      ICell builtCell = new Cell(cellName);
      builtCell.setDraw(initializer);
      cellMap.put(cellName, builtCell);
      return builtCell;
    }
  }

  /**
   * Extracts the rotation for the cell, otherwise 0.
   *
   * @param cell the Element from which the ICell is built
   * @return Double rotation
   */
  private static Double extractCellRotation(Element cell) {
    String doubleName = Factory.getVal(cell, ROTATION, resources);
    try {
      return Double.parseDouble(doubleName);
    } catch (NumberFormatException e) {
      return 0.0;
    }
  }

  /**
   * Extracts the IOffset for the cell, otherwise Offset.NONE.
   *
   * @param cell the Element from which the ICell is built
   * @return IOffset representing the offset with which to initialize the cell
   */
  private static IOffset extractCellOffset(Element cell) {
    String offsetName = Factory.getVal(cell, FAN, resources).strip();
    try {
      return Offset.valueOf(offsetName.toUpperCase());
    } catch (IllegalArgumentException e) {
      return Offset.NONE;
    }
  }
}
