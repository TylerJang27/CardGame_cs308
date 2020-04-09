package ooga.cardtable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ooga.data.rules.IPhaseMachine;

public class Table implements ITable {
  private IPhaseMachine machine;
  private List<ICell> cells; //FIXME table doesn't own cells
  private IGameState lastState;

  @Override
  public void setPhaseMachine(IPhaseMachine mach) {
    machine = mach;
  }

  @Override
  public void setCellList(List<ICell> cellList) {
    cells = cellList;
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
