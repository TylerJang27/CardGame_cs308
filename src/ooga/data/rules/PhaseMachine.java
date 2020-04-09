package ooga.data.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ooga.cardtable.GameState;
import ooga.cardtable.ICell;
import ooga.cardtable.IGameState;
import ooga.cardtable.IMove;

public class PhaseMachine implements IPhaseMachine {

  private Map<String, IPhase> phases;
  private IPhase startPhase;
  private IPhase currentPhase;
  private List<ICell> cells;
  private List<IPhaseHistoryCell> history;

  @Override
  public Map<String, IPhase> getPhases() {
    return new HashMap<>(phases);
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
  public List<String> getTopLevelCellNames() {
    List<String> ret = new ArrayList<>();
    for (ICell c : cells) {
      ret.add(c.getName());
    }
    return ret;
  }

  @Override
  public IGameState update(IMove move) {
    IRule rule = getCurrentPhase().identifyMove(move);
    IPhase nextPhase = phases.get(getCurrentPhase().getNextPhaseName(rule)); //togetherify identifyMove and getNextPhaseName
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
    return null; //FIXME
  }
}
