package ooga.cardtable;

import java.util.List;
import java.util.Map;
import ooga.data.rules.IPhaseMachine;
import ooga.data.saveconfiguration.ISaveConfiguration;

public interface ITable {

  IPlayer getCurrentPlayer();

  void setPhaseMachine(IPhaseMachine machine);

  void restartGame();

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

  ISaveConfiguration getSaveData(String gameName, String rulePath);

}
