package ooga.data.rules;

import java.util.List;
import ooga.cardtable.ICell;

/**
 * An interface for ICells and ICellGroups.
 */
public interface Cellular {

  /**
   * Return all the ICells represented by the name for either a group or an individual cell.
   *
   * @param name the query
   * @return a List of ICells matching the name
   */
  List<ICell> getCellsbyName(String name);

  /**
   * Returns whether or not the name is contained by getCellsByName() for this name.
   *
   * @param name the query
   * @return whether or not the name is relevant
   */
  boolean isInGroup(String name);

}
