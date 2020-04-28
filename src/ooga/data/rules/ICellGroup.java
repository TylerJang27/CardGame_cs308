package ooga.data.rules;

import java.util.Map;
import ooga.cardtable.ICell;
import ooga.cardtable.IDeck;

/**
 * This interface stores groups of related cells. Gets information on a group of thematically linked
 * cells. Includes the group name itself and the cells contained within the group.
 *
 * @author Tyler Jang
 */
public interface ICellGroup extends Cellular {

  /**
   * Retrieves the name of the Cell Group
   *
   * @return the ICellGroup's name
   */
  String getName();

  /**
   * Retrieves the cells denoted by this Cell Group
   *
   * @return Collection of ICells
   */
  Map<String, ICell> getCellMap();

  /**
   * Initializes all of the ICells referred to by this group name.
   *
   * @param mainDeck the IDeck with which the ICells should be initialized.
   */
  void initializeAll(IDeck mainDeck);

}
