package ooga.cardtable;

import java.util.List;
import java.util.Map;

import ooga.data.rules.IPhaseMachine;

public class Table implements ITable {
  private IPhaseMachine machine;
  private IGameState lastState;

  public Table() {
    machine = null;
    lastState = GameState.WAITING;
  }

  public Table(IPhaseMachine mach) {
    this();
    setPhaseMachine(mach);
  }

  @Override
  public void setPhaseMachine(IPhaseMachine mach) {
    machine = mach;
  }

  @Override
  public void setCellList(List<ICell> cellList) {
    //machine.setCellList(cellList);
    System.out.println("Deprecated");
  }

  @Override
  public IGameState update(IMove move) {
    lastState = machine.update(move);
    return lastState;
  }

  @Override
  public IGameState getGameState() {
    return lastState;
  }

  @Override
  public Map<String, ICell> getCellData() {
    return machine.getTopLevelCells();
  }
}
