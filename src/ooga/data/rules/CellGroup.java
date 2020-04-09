package ooga.data.rules;

import ooga.cardtable.ICell;

import java.util.Collection;

//TODO: ADD DOCUMENTATION LATER
public class CellGroup implements ICellGroup{

    String myName;
    Collection<ICell> myCells;

    public CellGroup(String name, Collection<ICell> cells) {
        myName = name;
        myCells = cells;
    }


    /**
     * Retrieves the name of the Cell Group
     *
     * @return the ICellGroup's name
     */
    @Override
    public String getName() {
        return myName;
    }

    /**
     * Retrieves the cells denoted by this Cell Group
     *
     * @return Collection of ICells
     */
    @Override
    public Collection<ICell> getCells() {
        return myCells;
    }
}
