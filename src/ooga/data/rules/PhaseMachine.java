package ooga.data.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ooga.cardtable.GameState;
import ooga.cardtable.ICell;
import ooga.cardtable.IGameState;
import ooga.cardtable.IMove;

import ooga.data.rules.excluded.IPhaseHistoryCell;

public class PhaseMachine implements IPhaseMachine {

  private Map<String, IPhase> phases;
  private IPhase startPhase;
  private IPhase currentPhase;
  private List<ICell> cells;
  private List<IPhaseHistoryCell> history;
  private ISettings mySettings;
  private IMove lastMove;

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
    lastMove = null;
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
      IPhaseArrow arrow = currentPhase.executeAutomaticActions(null, lastMove); //TODO: REPLACE WITH PLAYER
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

  private IMove replaceMoveCells(IMove move) {
    ICell d = findNamedCell(move.getDonor().getName());
    if (d != null) {
      move.setDonor(d);
    }
    ICell r = findNamedCell(move.getRecipient().getName());
    if (r != null) {
      move.setRecipient(r);
    }
    ICell m = findNamedCell(move.getMover().getName());
    if (m != null) {
      move.setMover(m);
    }
    return move;
  }

  private ICell findNamedCell(String nm) {
    System.out.println("findname: "+nm);
    String[] names = nm.split(",");
    String firstName = names[0];
    String[] restNames = new String[names.length-1];
    for (int i = 1; i < names.length; i++) {
      restNames[i-1] = names[i];
    }
    String restName = String.join(",", restNames);
    ICell ret = null;
    for (ICell cell: cells) {
      if (cell.getName().toLowerCase().equals(firstName)) {
        ret = cell.followNamespace(restName);
        if (ret != null) {
          return ret;
        }
      }
    }
    System.out.println("a named cell was asked for and not found, and that (might be) terrible.");
    return ret;
  }

  @Override
  public IGameState update(IMove move) {
    //String next = getCurrentPhase().getNextPhaseName(move);
    move = replaceMoveCells(move);
    System.out.println("phasemachine got move");
    System.out.println(move.getDonor().getName());
    System.out.println(move.getMover().getName());
    System.out.println(move.getRecipient().getName());
    IPhaseArrow arrow = currentPhase.executeMove(move);
    lastMove = move;
    System.out.println("I'm a dummy if this doesn't print");
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
