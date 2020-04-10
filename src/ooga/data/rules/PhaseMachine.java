package ooga.data.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ooga.cardtable.GameState;
import ooga.cardtable.ICell;
import ooga.cardtable.IGameState;
import ooga.cardtable.IMove;
import ooga.data.rules.IPhase;
import ooga.data.rules.IPhaseArrow;
import ooga.data.rules.ISettings;
import ooga.data.rules.excluded.IPhaseHistoryCell;
import ooga.data.rules.IPhaseMachine;

public class PhaseMachine implements IPhaseMachine {

  private Map<String, IPhase> phases;
  private IPhase startPhase;
  private IPhase currentPhase;
  private List<ICell> cells;
  private List<IPhaseHistoryCell> history;
  private ISettings mySettings;

  /*public PhaseMachine() {
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
  }*/

  public PhaseMachine(Map<String, IPhase> ph, String startName, ISettings settings) {
    history = new ArrayList<>();
    phases = ph;
    startPhase = phases.get(startName);
    currentPhase = startPhase;
    cells = new ArrayList<>();
    for (Map.Entry<String, ICell> e: getTopLevelCells().entrySet()) {
      cells.add(e.getValue());
    }
    mySettings = settings;
    cycleAutomatic();
  }

  private void cycleAutomatic() {
    if (currentPhase.isAutomatic()) {
      IPhaseArrow arrow = currentPhase.executeAutomaticActions(null); //TODO: REPLACE WITH PLAYER
      moveToNextPhase(arrow);
    }
  }

  @Override
  public Map<String, IPhase> getPhases() {
    return new HashMap<>(phases);
  }

  @Override
  public void addPhase(IPhase phase) {
    phases.put(phase.getMyName(), phase);
    //phase.setCellList(cells);
  }

  @Override
  public List<String> getPhaseNames() {
    return new ArrayList<>(phases.keySet());
  }

  @Override
  public String getStartingPhaseName() {
    return startPhase.getMyName();
  }

  @Override
  public Map<String, ICell> getTopLevelCells() {
    return currentPhase.getMyCellMap();
  }

  /*@Override
  public void setCellList(List<ICell> cellList) {
    cells = new ArrayList<>(cellList);
    for (IPhase ph: phases.values()) {
      ph.setCellList(cells);
    }
  }
   */

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
    //String next = getCurrentPhase().getNextPhaseName(move);
    IPhaseArrow arrow = currentPhase.executeMove(move);
    if (arrow != null) {
      moveToNextPhase(arrow);
      return GameState.WAITING;
    }
    return GameState.INVALID;

    /*if (next == null) {
      return GameState.INVALID; //FIXME
    }
    IPhase nextPhase = phases.get(next);
    IGameState state = nextPhase.executeAutomaticActions();
    while (state != GameState.WAITING) { //FIXME
      nextPhase = phases.get(nextPhase.getNextPhaseName(null));
      state = nextPhase.executeAutomaticActions();
    }
    currentPhase = nextPhase;
    return state;*/
  }

  private void moveToNextPhase(IPhaseArrow arrow) {
    //TODO: UPDATE HISTORY
    currentPhase = phases.get(arrow.getEndPhaseName());
    cycleAutomatic();
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

  @Override
  public ISettings getSettings() {
    return mySettings;
  }
}
