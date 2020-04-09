package ooga.data.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ooga.cardtable.GameState;
import ooga.cardtable.ICell;
import ooga.cardtable.IGameState;
import ooga.cardtable.IMove;
import ooga.data.PhaseMachineFactory;

public class PhaseMachine implements IPhaseMachine {

  private Map<String, IPhase> phases;
  private IPhase startPhase;
  private IPhase currentPhase;
  private List<ICell> cells;
  private List<IPhaseHistoryCell> history;

  public PhaseMachine() {
    history = new ArrayList<>();
    phases = new HashMap<>();
  }

  public PhaseMachine(List<IPhase> ph, String startName) {
    this();
    for (IPhase p: ph) {
      addPhase(p);
    }
    startPhase = phases.get(startName); //FIXME add error checking
    currentPhase = startPhase;
  }

  public PhaseMachine(Map<String, IPhase> ph, String startName) {
    this(new ArrayList<>(ph.values()), startName);
  }

  @Override
  public Map<String, IPhase> getPhases() {
    return new HashMap<>(phases);
  }

  @Override
  public void addPhase(IPhase phase) {
    phases.put(phase.getName(), phase);
    phase.setCellList(cells);
  }

  @Override
  public List<String> getPhaseNames() {
    return new ArrayList<>(phases.keySet());
  }

  @Override
  public String getStartingPhaseName() {
    return startPhase.getName();
  }

  @Override
  public Map<String, ICell> getTopLevelCells() {
    Map<String, ICell> ret = new HashMap<>();
    for (ICell c : cells) {
      ret.put(c.getName(), c);
    }
    return ret;
  }

  @Override
  public void setCellList(List<ICell> cellList) {
    cells = new ArrayList<>(cellList);
    for (IPhase ph: phases.values()) {
      ph.setCellList(cells);
    }
  }

  @Override
  public List<String> getTopLevelCellNames() {
    List<String> ret = new ArrayList<>();
    for (ICell c : cells) {
      ret.add(c.getName());
    }
    return ret;
  }

  @Override
  public IGameState update(IMove move) {
    String next = getCurrentPhase().getNextPhaseName(move);
    if (next == null) {
      return GameState.INVALID; //FIXME
    }
    IPhase nextPhase = phases.get(next);
    IGameState state = nextPhase.executeAutomaticActions();
    while (state != GameState.WAITING) { //FIXME
      nextPhase = phases.get(nextPhase.getNextPhaseName(null));
      state = nextPhase.executeAutomaticActions();
    }
    currentPhase = nextPhase;
    return state;
  }

  @Override
  public void moveToNextPhase(IPhaseArrow arrow) { //FIXME this might want to be private
    //consider removing
  }

  @Override
  public IPhase getCurrentPhase() {
    return currentPhase;
  }

  @Override
  public List<IPhaseHistoryCell> getHistory() {
    System.out.println("To be implemented later");
    return null; //FIXME
  }

  @Override
  public boolean isValidDonor(ICell cell) {
    return currentPhase.isValidDonor(cell);
  }
}
