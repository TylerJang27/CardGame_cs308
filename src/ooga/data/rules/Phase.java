package ooga.data.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ooga.cardtable.ICell;
import ooga.cardtable.IGameState;
import ooga.cardtable.IMove;

public class Phase implements IPhase {

  private String name;
  private List<ICell> cellList;
  private List<IRule> rules;
  private List<ICardAction> autoActions;
  private List<String> validDonorNames;
  private Map<IRule, List<ICardAction>> conditionalActions;
  private Map<IRule, String> phaseUpdate;

  public Phase(String nm) {
    name = nm;
    cellList = new ArrayList<>();
    rules = new ArrayList<>();
    autoActions = new ArrayList<>();
    conditionalActions = new HashMap<>();
    phaseUpdate = new HashMap<>();
  }

  public Phase(String nm, List<ICardAction> autos, List<ICell> cells) {
    this(nm);
    setCellList(cells);
    autoActions = new ArrayList<>(autos);
  }

  @Override
  public boolean isAutomatic() {
    return conditionalActions.isEmpty();
  }

  @Override
  public IRule identifyMove(IMove move) {
    for (IRule r : rules) {
      if (r.checkValidMove(move)) {
        return r;
      }
    }
    return null;
  }

  @Override
  public List<IRule> getRuleList() {
    return new ArrayList<>(rules);
  }

  @Override
  public Map<IRule, List<ICardAction>> getConditionalActions() {
    Map<IRule, List<ICardAction>> ret = new HashMap<>();
    for (Entry<IRule, List<ICardAction>> e : conditionalActions.entrySet()) {
      ret.put(e.getKey(), new ArrayList<>(e.getValue()));
    }
    return ret;
  }

  @Override
  public List<ICardAction> getAutoActions() {
    return new ArrayList<>(autoActions);
  }

  @Override
  public IGameState executeAutomaticActions() {
    IGameState ret = null;
    for (ICardAction act : autoActions) {
      ret = act.execute(cellList);
    }
    return ret;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getNextPhaseName(IMove move) {
    IRule rule = identifyMove(move);
    return phaseUpdate.get(rule); //fixme does null case return null? I sure hope so
  }

  @Override
  public void setCellList(List<ICell> cells) {
    cellList = cells;
  }

  @Override
  public void addRule(IRule rule, List<ICardAction> actions, String nextPhase) {
    rules.add(rule);
    conditionalActions.put(rule, new ArrayList<>(actions));
    phaseUpdate.put(rule, nextPhase);
  }

  @Override
  public void setAutoActions(List<ICardAction> actions) {
    autoActions = new ArrayList<>(actions);
  }

  @Override
  public boolean isValidDonor(ICell cell) {
    return validDonorNames.contains(cell.getName().split(",")[0]);
  }
}
