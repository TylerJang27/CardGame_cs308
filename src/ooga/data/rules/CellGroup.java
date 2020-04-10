package ooga.data.rules;

import ooga.cardtable.ICell;
import ooga.cardtable.IDeck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

//TODO: ADD DOCUMENTATION LATER
public class CellGroup implements ICellGroup{

    String myName;
    Map<String, ICell> myCells;

    public CellGroup(String name, Map<String, ICell> cells) {
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
    public Map<String, ICell> getCellMap() {
        return myCells;
    }

    @Override
    public List<ICell> getCellsbyName(String name) {
        List<ICell> cellList = new ArrayList<>();
        if (name.equals(myName)) {
            cellList.addAll(myCells.values());
        } else if (isInGroup(name)){
            cellList.add(myCells.get(name));
        }
        return cellList;
    }

    @Override
    public boolean isInGroup(String name) {
        return myCells.containsKey(name);
    }

    @Override
    public void initializeAll(IDeck mainDeck) {
        for (Map.Entry<String, ICell> c: myCells.entrySet()) {
            c.getValue().initializeCards(mainDeck);
            //System.out.println(c.getValue().getTotalSize());
        }
    }
}
