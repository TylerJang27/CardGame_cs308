package ooga.cardtable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ooga.data.rules.IPhaseMachine;

public class Table implements ITable {

  private IPhaseMachine machine;
  private IGameState lastState;
  private List<IPlayer> players;
  private IPlayer currentPlayer; //TODO: MAKE WATERTIGHT

  public Table() {
    machine = null;
    lastState = GameState.WAITING;
    players = List.of(new Player());
    currentPlayer = players.get(0);
  }

  public Table(IPhaseMachine mach) {
    this();
    setPhaseMachine(mach);
  }

  @Override
  public IPlayer getCurrentPlayer() {
    return currentPlayer;
  }

  @Override
  public void setPhaseMachine(IPhaseMachine mach) {
    machine = mach;
  }

  @Override
  public void restartGame() {
    machine.restartGame();
  }

  @Override
  public void setCellList(List<ICell> cellList) {
    //machine.setCellList(cellList);
    System.out.println("Deprecated");
  }

  @Override
  public IGameState update(IMove move) {
    machine.setCurrentPlayer(currentPlayer);
    lastState = machine.update(move);
    return lastState;
  }

  @Override
  public IGameState getGameState() {
    return lastState;
  }

  @Override
  public Map<String, ICell> getCellData() {
    Map<String, ICell> cells = machine.getTopLevelCells();
    Map<String, ICell> ret = new HashMap<>();
    for (Entry<String, ICell> e : cells.entrySet()) {
      ret.put(e.getKey(), e.getValue().copy());
    }
    return ret;
  }
}
