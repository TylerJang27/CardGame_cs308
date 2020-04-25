package ooga.cardtable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ooga.data.rules.IPhaseMachine;
import ooga.data.saveconfiguration.ISaveConfiguration;
import ooga.data.saveconfiguration.SaveConfiguration;

public class Table implements ITable {

  private IPhaseMachine machine;
  private IGameState lastState;
  private List<IPlayer> players;
  private IPlayer currentPlayer;

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
    currentPlayer.setScore(0.0);
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

  @Override
  public ISaveConfiguration getSaveData(String gameName, String rulePath) {
    String phase = machine.getCurrentPhase().getMyName();
    Double score = currentPlayer.getScore();
    Map<String, String> cellBuilder = new HashMap<>();
    for (Map.Entry<String, ICell> e : getCellData().entrySet()) {
      cellBuilder.put(e.getKey(), e.getValue().toStorageString());
    }
    return new SaveConfiguration(gameName, rulePath, phase, score, cellBuilder);
  }

}
