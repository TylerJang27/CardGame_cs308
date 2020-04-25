package ooga.data.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ooga.cardtable.ICell;
import ooga.cardtable.IDeck;

/**
 * This class implements ICellGroup and stores information about what ICells are referred to by an
 * ICellGroup's name. These ICells can collectively be referred to by referring to the group name.
 *
 * @author Tyler Jang
 */
public class CellGroup implements ICellGroup {

  String myName;
  Map<String, ICell> myCells;

  /**
   * Constructor for CellGroup, taking in the group's name and the Map of the relevant ICells.
   *
   * @param name  the name of the group
   * @param cells Map of String ICell names to ICells
   */
  public CellGroup(String name, Map<String, ICell> cells) {
    myName = name;
    myCells = cells;
  }


  /**
   * Retrieves the name of the Cell Group.
   *
   * @return the ICellGroup's name
   */
  @Override
  public String getName() {
    return myName;
  }

  /**
   * Retrieves the cells denoted by this Cell Group.
   *
   * @return a Map of String ICell names to ICells
   */
  @Override
  public Map<String, ICell> getCellMap() {
    return myCells;
  }

  /**
   * Return all the ICells represented by the name for either a group or an individual cell.
   *
   * @param name the query
   * @return a List of ICells matching the name
   */
  @Override
  public List<ICell> getCellsbyName(String name) {
    List<ICell> cellList = new ArrayList<>();
    if (name.equals(myName)) {
      cellList.addAll(myCells.values());
    } else if (isInGroup(name)) {
      cellList.add(myCells.get(name));
    }
    return cellList;
  }

  /**
   * Returns whether or not the name is contained by getCellsByName() for this name.
   *
   * @param name the query
   * @return whether or not the name is relevant
   */
  @Override
  public boolean isInGroup(String name) {
    return myCells.containsKey(name);
  }

  /**
   * Initializes all of the ICells referred to by this group name.
   *
   * @param mainDeck the IDeck with which the ICells should be initialized.
   */
  @Override
  public void initializeAll(IDeck mainDeck) {
    for (Map.Entry<String, ICell> c : myCells.entrySet()) {
      c.getValue().initializeCards(mainDeck);
    }
  }
}
