package ooga.data.rules;

import ooga.cardtable.ICell;
import ooga.cardtable.IDeck;

import java.util.Map;

/**
 * Gets information on a group of thematically linked cells. Includes the group name itself and the cells contained within the group.
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

    void initializeAll(IDeck mainDeck); //TODO: ADD TO API CHANGES

}
