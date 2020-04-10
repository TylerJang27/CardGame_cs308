package ooga.cardtable;

import ooga.data.rules.excluded.IPhaseMachine;

import java.util.List;
import java.util.Map;

public interface ITable {

  void setPhaseMachine(IPhaseMachine machine);

  void setCellList(List<ICell> cellList);

  /**
   * Updates the table based on the move. Returns true if move was valid, false otherwise
   *
   * @param move the move the was made
   * @return game state as a result of the move
   */
  IGameState update(IMove move);

  IGameState getGameState();

  Map<String, ICell> getCellData();

}
