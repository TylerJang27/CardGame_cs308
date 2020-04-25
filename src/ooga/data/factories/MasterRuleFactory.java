package ooga.data.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import ooga.cardtable.ICell;
import ooga.cardtable.IMove;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.rules.ControlAction;
import ooga.data.rules.ICardAction;
import ooga.data.rules.ICellGroup;
import ooga.data.rules.IControlAction;
import ooga.data.rules.IMasterRule;
import ooga.data.rules.IPhaseArrow;
import ooga.data.rules.IRule;
import ooga.data.rules.MasterRule;
import ooga.data.rules.PhaseArrow;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This MasterRuleFactory implements Factory and constructs IMasterRules using the
 * createMasterRules() method. These IMasterRules contain logic to determine whether a move is valid
 * and whether phase changes should occur.
 * <p>
 * This Factory depends on RuleFactory and ActionFactory working properly.
 *
 * @author Tyler Jang
 */
public class MasterRuleFactory implements Factory {

  private static final ResourceBundle RESOURCES = PhaseFactory.RESOURCES;

  private static final String PHASE = PhaseFactory.PHASE;
  private static final String CATEGORY = PhaseFactory.CATEGORY;
  private static final String RULE = PhaseFactory.RULE;
  private static final String VALIDATION = PhaseFactory.VALIDATION;
  private static final String RECEIVER = PhaseFactory.RECEIVER;
  private static final String MOVER = PhaseFactory.MOVER;
  private static final String DONOR = PhaseFactory.DONOR;
  private static final String ACTION = PhaseFactory.ACTION;
  private static final String RECEIVER_DESTINATION = PhaseFactory.RECEIVER_DESTINATION;
  private static final String MOVER_DESTINATION = PhaseFactory.MOVER_DESTINATION;
  private static final String NEXT_PHASE = PhaseFactory.NEXT_PHASE;
  private static final String DONOR_DESTINATION = PhaseFactory.DONOR_DESTINATION;
  private static final String CONDITION = PhaseFactory.CONDITION;

  private static final String R = PhaseFactory.R;
  private static final String M = PhaseFactory.M;
  private static final String D = PhaseFactory.D;
  private static final String C = PhaseFactory.C;

  protected static final String ALL = "All";

  protected static final List<String> TRUE_CHECKS = List.of("", RESOURCES.getString(ALL));

  private MasterRuleFactory() {
  }

  /**
   * Builds and returns IMasterRules built for an IPhase from a rules XML. Requirements for rules
   * XML can be found in doc/XML_Documentation.md.
   *
   * @param rules        the Node from which IMasterRules are built
   * @param cellGroupMap the Map of String ICellGroup names to ICellGroups
   * @param cellMap      the Map of String ICell names to ICells
   * @param phaseName    the String name of the IPhase
   * @return a List of IMasterRules for an IPhase, each with their own internal IRules and actions
   */
  public static List<IMasterRule> createMasterRules(Node rules,
      Map<String, ICellGroup> cellGroupMap, Map<String, ICell> cellMap, String phaseName) {
    List<String> masterRuleNames = new ArrayList<>();
    List<IMasterRule> masterRuleList = new ArrayList<>();
    Map<String, IMasterRule> ruleMap = new HashMap<>();
    Map<IMasterRule, List<ICardAction>> ruleActionMap = new HashMap<>();

    try {
      NodeList ruleNodeList = ((Element) rules).getElementsByTagName(RESOURCES.getString(RULE));

      for (int k = 0; k < ruleNodeList.getLength(); k++) {
        Element ruleNode = (Element) ruleNodeList.item(k);
        String ruleName = XMLHelper.getAttribute(ruleNode, RESOURCES.getString(CATEGORY));

        List<IRule> autoRules = new ArrayList<>();
        List<IRule> allRules = getAllRules(cellGroupMap, ruleName, ruleNode, autoRules);

        List<IControlAction> controlActionList = new ArrayList<>();
        List<ICardAction> cardActionList = getCardandControlActions(phaseName, ruleNode, ruleName,
            controlActionList, cellGroupMap);

        IMasterRule masterRule = new MasterRule(allRules, autoRules, cardActionList,
            controlActionList);
        masterRuleNames.add(ruleName);
        masterRuleList.add(masterRule);
        ruleMap.put(ruleName, masterRule);

        ruleActionMap.put(masterRule, cardActionList);
      }
    } catch (Exception e) {
      throw new XMLException(e, Factory.MISSING_ERROR + "," + RESOURCES.getString(RULE));
    }
    return masterRuleList;
  }

  /**
   * Extracts the ICardActions and the IControlActions for a given phase.
   *
   * @param phaseName         the String name of the current IPhase being created
   * @param ruleNode          the Node corresponding to the IMasterRule
   * @param ruleName          the String name of the IMasterRule
   * @param controlActionList a List of IControlActions that will be processed upon successful
   *                          validation of an IMasterRule
   * @param cellGroupMap      a Map of String ICellGroup names to ICellGroups
   * @return a List of ICardActions that will be processed upon successful validation of an
   * IMasterRule
   */
  private static List<ICardAction> getCardandControlActions(String phaseName, Element ruleNode,
      String ruleName, List<IControlAction> controlActionList,
      Map<String, ICellGroup> cellGroupMap) {
    List<ICardAction> cardActionList = new ArrayList<>();
    NodeList actionList = ruleNode.getElementsByTagName(RESOURCES.getString(ACTION));
    for (int j = 0; j < actionList.getLength(); j++) {
      Element actionHeadNode = (Element) actionList.item(j);

      NodeList allActions = actionHeadNode.getChildNodes();

      getCardActions(ruleName, cardActionList, allActions, cellGroupMap);

      Node phaseAction = XMLHelper.getNodeByName(allActions, RESOURCES.getString(NEXT_PHASE));
      try {
        String newPhase = XMLHelper.getAttribute((Element) phaseAction, RESOURCES.getString(PHASE));
        String pointVal = phaseAction.getTextContent();
        Double points = 0.0;
        try {
          if (!pointVal.isEmpty()) {
            points = Double.parseDouble(pointVal);
          }
        } catch (NumberFormatException e) {
          points = 0.0;
        }
        IPhaseArrow arrow = new PhaseArrow(phaseName, ruleName, newPhase);
        controlActionList.add(new ControlAction(arrow, points));
      } catch (NullPointerException e) {
        throw new XMLException(e, MISSING_ERROR + "," + RESOURCES.getString(NEXT_PHASE));
      }
    }
    return cardActionList;
  }

  /**
   * Extracts the ICardActions parsed from the XML and adds them to cardActionList.
   *
   * @param ruleName       the name of the rule being created
   * @param cardActionList the List of ICardActions being added to
   * @param allActions     the NodeList being parsed from
   * @param cellGroupMap   a Map of String ICellGroup names to ICellGroups
   */
  private static void getCardActions(String ruleName, List<ICardAction> cardActionList,
      NodeList allActions, Map<String, ICellGroup> cellGroupMap) {
    Node recAction = XMLHelper.getNodeByName(allActions, RESOURCES.getString(RECEIVER_DESTINATION));
    if (recAction != null) {
      cardActionList
          .add(ActionFactory.createAction((Element) recAction, ruleName + R, cellGroupMap));
    }
    Node movAction = XMLHelper.getNodeByName(allActions, RESOURCES.getString(MOVER_DESTINATION));
    if (movAction != null) {
      cardActionList
          .add(ActionFactory.createAction((Element) movAction, ruleName + M, cellGroupMap));
    }
    Node donAction = XMLHelper.getNodeByName(allActions, RESOURCES.getString(DONOR_DESTINATION));
    if (donAction != null) {
      cardActionList
          .add(ActionFactory.createAction((Element) donAction, ruleName + D, cellGroupMap));
    }
  }

  /**
   * Extracts the receiver rules, the mover rules, the donor rules, and the conditional rules for a
   * given IMasterRule. This supports having multiple receive_rules, but all must be satisfied in
   * order for a move to be considered valid.
   *
   * @param cellGroupMap the Map of String ICellGroup names to ICellGroups
   * @param ruleName     the String name of the rule
   * @param ruleNode     the Node corresponding to the IMasterRule
   * @param autoRules    a List of all the automatic IRules that are
   * @return a List of all the IRules for receivers, movers, and donors
   */
  private static List<IRule> getAllRules(Map<String, ICellGroup> cellGroupMap, String ruleName,
      Element ruleNode, List<IRule> autoRules) {
    NodeList receiverRuleNodeList = ruleNode.getElementsByTagName(RESOURCES.getString(VALIDATION));
    List<IRule> allRules = new ArrayList<>();
    for (int j = 0; j < receiverRuleNodeList.getLength(); j++) {
      Element receiverRuleNode = (Element) receiverRuleNodeList.item(j);

      NodeList allConditions = receiverRuleNode.getChildNodes();

      Node recRule = XMLHelper.getNodeByName(allConditions, RESOURCES.getString(RECEIVER));
      if (recRule != null) {
        allRules.add(RuleFactory.createRule((Element) recRule, ruleName + R, cellGroupMap,
            (IMove move) -> checkRecipient(move, ruleName, cellGroupMap)));
      }
      Node movRule = XMLHelper.getNodeByName(allConditions, RESOURCES.getString(MOVER));
      if (movRule != null) {
        allRules.add(RuleFactory.createRule((Element) movRule, ruleName + M, cellGroupMap));
      }
      Node donRule = XMLHelper.getNodeByName(allConditions, RESOURCES.getString(DONOR));
      if (donRule != null) {
        allRules.add(RuleFactory.createRule((Element) donRule, ruleName + D, cellGroupMap));
      }
      NodeList condRuleList = receiverRuleNode.getElementsByTagName(RESOURCES.getString(CONDITION));
      for (int l = 0; l < condRuleList.getLength(); l++) {
        Node condRule = condRuleList.item(l);
        autoRules.add(RuleFactory.createRule((Element) condRule, ruleName + C, cellGroupMap));
      }
    }
    return allRules;
  }

  /**
   * A helper method for extracting the current cell from a move, using internally defined rule name
   * appending.
   *
   * @param ruleName      the name of the rule being analyzed
   * @param moverCell     the function for retrieving the Cell being moved
   * @param donorCell     the function for retrieving the Cell being donated from
   * @param recipientCell the function for retrieving the Cell receiving the move
   * @return a Function to retrieve the current Cell
   */
  protected static Function<IMove, ICell> getCurrentCellFunction(String ruleName,
      Function<IMove, ICell> moverCell, Function<IMove, ICell> donorCell,
      Function<IMove, ICell> recipientCell) {
    Function<IMove, ICell> currCell;
    char currentChar = ruleName.charAt(ruleName.length() - 1);
    if (M.equals("" + currentChar)) {
      currCell = (IMove move) -> moverCell.apply(move);
    } else if (D.equals("" + currentChar)) {
      currCell = (IMove move) -> donorCell.apply(move);
    } else { //R
      currCell = (IMove move) -> recipientCell.apply(move);
    }
    return currCell;
  }

  /**
   * Returns whether or not the current cell or cell's group name matches with the recipient.
   *
   * @param move         the IMove being processed
   * @param name         the name of the cell or cell group in question
   * @param cellGroupMap the Map of String ICellGroup names to ICellGroups
   * @return a Boolean representing whether or not this name is attached to the Receiver
   */
  private static Boolean checkRecipient(IMove move, String name,
      Map<String, ICellGroup> cellGroupMap) {
    return name.isEmpty() ||
        (cellGroupMap.containsKey(name) &&
            cellGroupMap.get(name).isInGroup(move.getRecipient().findHead().getName())) ||
        name.equals(move.getRecipient().getName().split(",")[0]);
  }
}
