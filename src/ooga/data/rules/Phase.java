package ooga.data.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ooga.cardtable.ICell;
import ooga.cardtable.IGameState;
import ooga.cardtable.IMove;
import ooga.cardtable.IPlayer;

public class Phase implements IPhase {

  private String myName;

  private List<IMasterRule> myRules;
  private List<String> validDonorNames;
  private Map<String, ICellGroup> myCellGroupMap;
  private Map<String, ICell> myCellMap;
  private boolean isAuto;


  public Phase(String name, List<IMasterRule> ruleList, List<String> validDonors, Map<String, ICellGroup> cellGroupMap, Map<String, ICell> cellMap, boolean automatic) {
    myName = name;
    myRules = ruleList;
    validDonorNames = validDonors;
    myCellGroupMap = cellGroupMap;
    myCellMap = cellMap;
    isAuto = automatic;
  }

  @Override
  public boolean isAutomatic() {
    return isAuto;
  }

  @Override
  private IMasterRule identifyMove(IMove move) {
    for (IMasterRule r : myRules) {
      if (r.checkValidMove(move)) {
        return r;
      }
    }
    return null;
  }

  @Override
  public List<IMasterRule> getRuleList() {
    return new ArrayList<>(myRules);
  }

  @Override
  private IPhaseArrow executeAutomaticActions(IPlayer player) {
    for (IMasterRule rule: myRules) {
      rule.executeAutoActions(player);
    }
  }

  @Override
  private String getNextPhaseName(IMove move) {
    IMasterRule rule = identifyMove(move);
    return phaseUpdate.get(rule); //fixme does null case return null? I sure hope so
  }


  /**private List<ICell> cellList;

   private List<ICardAction> autoActions;

   private Map<IMasterRule, List<ICardAction>> conditionalActions;
   private Map<IMasterRule, String> phaseUpdate;
   **/

  /**
   public Phase(String nm) {
   name = nm;
   cellList = new ArrayList<>();
   myRules = new ArrayList<>();
   autoActions = new ArrayList<>();
   conditionalActions = new HashMap<>();
   phaseUpdate = new HashMap<>();
   }

   public Phase(String nm, List<ICardAction> autos, List<ICell> cells) {
   this(nm);
   setCellList(cells);
   autoActions = new ArrayList<>(autos);
   }**/

  /*@Override
  public void setCellList(List<ICell> cells) {
    cellList = cells;
  }*/

  /*@Override
  public void addRule(IMasterRule rule, List<ICardAction> actions, String nextPhase) {
    myRules.add(rule);
    conditionalActions.put(rule, new ArrayList<>(actions));
    phaseUpdate.put(rule, nextPhase);
  }*/

  /*@Override
  public void setAutoActions(List<ICardAction> actions) {
    autoActions = new ArrayList<>(actions);
  }*/
  /*
  @Override
  public Map<IMasterRule, List<ICardAction>> getConditionalActions() {
    Map<IMasterRule, List<ICardAction>> ret = new HashMap<>();
    for (Entry<IMasterRule, List<ICardAction>> e : conditionalActions.entrySet()) {
      ret.put(e.getKey(), new ArrayList<>(e.getValue()));
    }
    return ret;
  }*/

  /*@Override
  public List<ICardAction> getAutoActions() {
    return new ArrayList<>(autoActions);
  }*/



  @Override
  public String getMyName() {
    return myName;
  }

  @Override
  public boolean isValidDonor(ICell cell) {
    return validDonorNames.contains(cell.getName().split(",")[0]);
  }
}
