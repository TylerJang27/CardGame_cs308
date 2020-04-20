package ooga.data.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ooga.cardtable.*;

import ooga.data.rules.excluded.IPhaseHistoryCell;

public class PhaseMachine implements IPhaseMachine {

  private Map<String, IPhase> phases;
  private IPhase startPhase;
  private IPhase currentPhase;
  private List<ICell> cells;
  private List<IPhaseHistoryCell> history;
  private ISettings mySettings;
  private IMove lastMove;
  private IDeck fullDeck;

  public PhaseMachine(Map<String, IPhase> ph, String startName, ISettings settings, IDeck deck) {
    fullDeck = deck;
    lastMove = null;
    history = new ArrayList<>();
    phases = ph;
    startPhase = phases.get(startName);
    mySettings = settings;
    restartGame();
  }

  @Override
  public void restartGame() {
    currentPhase = startPhase;
    cells = new ArrayList<>();
    IDeck deckCopy = fullDeck.copy();
    for (Map.Entry<String, ICell> e: getTopLevelCells().entrySet()) {
      cells.add(e.getValue());
    }
    for (Map.Entry<String, ICellGroup> e: currentPhase.getMyCellGroupMap().entrySet()) {
      e.getValue().initializeAll(deckCopy);
    }
    cycleAutomatic();
  }

  private void cycleAutomatic() {
    //System.out.println("my phase is " + currentPhase.getMyName() + " and my automaticity is " + currentPhase.isAutomatic());
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
    updateCellParents();
    //System.out.println("findname: "+nm);
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
    //System.out.println("a named cell was asked for and not found, and that (might be) terrible.");
    return ret;
  }

  @Override
  public IGameState update(IMove move) {
    //String next = getCurrentPhase().getNextPhaseName(move);
    move = replaceMoveCells(move);
    //System.out.println("phasemachine got move");
    //System.out.println(move.getDonor().getName());
    //System.out.println(move.getMover().getName());
    //System.out.println(move.getRecipient().getName());
    IPhaseArrow arrow = currentPhase.executeMove(move);
    lastMove = move;
    //System.out.println("I'm a dummy if this doesn't print");
    if (arrow != null) {
      moveToNextPhase(arrow);
      //System.out.println("welcome to Phase: " + currentPhase.getMyName());
      updateCellParents();
      return GameState.WAITING;
    }
    //System.out.println("welcome to Phase: " + currentPhase.getMyName());
    updateCellParents();  
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

  private void updateCellParents() {
    for (ICell c: cells) {
      c.updateParentage();
      //System.out.println("Phase cell: "+c);
    }
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
    //System.out.println("To be implemented later");
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
