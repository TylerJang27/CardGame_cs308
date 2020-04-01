package ooga.cardtable;

import java.util.List;
import java.util.Map;

public interface ITable {

  public void setPhaseMachine(IPhaseMachine machine);

  public void setCellList(List<ICell> cellList);

  /**
   * Updates the table based on the move. Returns true if move was valid, false otherwise
   * @param move the move the was made
   * @return validity of the move
   */
  public boolean update(IMove move);

  public Map<String, ICell> getCellData();

}
