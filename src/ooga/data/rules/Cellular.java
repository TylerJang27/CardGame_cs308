package ooga.data.rules;

import ooga.cardtable.ICell;

import java.util.List;
//TODO: DOCUMENT, ADD TO API CHANGES
public interface Cellular {

    List<ICell> getCellsbyName(String name);

    boolean isInGroup(String name);

}
