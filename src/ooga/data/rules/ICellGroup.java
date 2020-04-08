package ooga.data.rules;

import ooga.cardtable.ICell;

import java.util.Collection;

/**
 * Gets information on a group of thematically linked cells. Includes the group name itself and the cells contained within the group.
 *
 * @author Tyler Jang
 */
public interface ICellGroup {

    /**
     * Retrieves the name of the Cell Group
     *
     * @return the ICellGroup's name
     */
    public String getName();

    /**
     * Retrieves the cells denoted by this Cell Group
     *
     * @return Collection of ICells
     */
    public Collection<ICell> getCells();

}
