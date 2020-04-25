package ooga.data.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import ooga.cardtable.ICell;
import ooga.cardtable.IMove;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.rules.ICellGroup;
import ooga.data.rules.IRule;
import ooga.data.rules.Rule;
import org.w3c.dom.Element;

/**
 * This RuleFactory implements Factory and constructs IRules using the createRule() method. These
 * IRules validate logic at runtime for given IMoves.
 *
 * @author Tyler Jang
 */
public class RuleFactory implements Factory {

  private static final ResourceBundle RESOURCES = PhaseFactory.RESOURCES;

  private static final String NAME = PhaseFactory.NAME;
  private static final String CATEGORY = PhaseFactory.CATEGORY;
  private static final String RULES = PhaseFactory.RULES;
  private static final String VALUE = PhaseFactory.VALUE;
  private static final String COLOR = PhaseFactory.COLOR;
  private static final String SUIT = PhaseFactory.SUIT;
  private static final String NUMBER_CARDS = PhaseFactory.NUMBER_CARDS;
  private static final String IS_FACEUP = PhaseFactory.IS_FACEUP;

  private static final String NOT = PhaseFactory.NOT;
  private static final String SAME = PhaseFactory.SAME;
  private static final String YES = PhaseFactory.YES;
  private static final String GREATER_THAN = PhaseFactory.GREATER_THAN;
  private static final String LESS_THAN = PhaseFactory.LESS_THAN;

  private static final List<String> TRUE_CHECKS = MasterRuleFactory.TRUE_CHECKS;

  private RuleFactory() {
  }

  /**
   * The default method for building and returning a singular IRule from a rules XML. Requirements
   * for rules XML can be found in doc/XML_Documentation.md.
   *
   * @param e            the Element from which IRules are built
   * @param ruleName     the Name of the Rule being created
   * @param cellGroupMap the Map of String ICellGroup names to ICellGroups
   * @return an IRule built for this IMasterRule
   */
  public static IRule createRule(Element e, String ruleName, Map<String, ICellGroup> cellGroupMap) {
    return createRule(e, ruleName, cellGroupMap, (IMove move) -> true);
  }

  /**
   * Builds and returns a singular IRule from a rules XML. Requirements for rules XML can be found
   * in doc/XML_Documentation.md.
   *
   * @param e            the Element from which IRules are built
   * @param ruleName     the Name of the Rule being created
   * @param cellGroupMap the Map of String ICellGroup names to ICellGroups
   * @param cond         the first condition to be added to createRule
   * @return an IRule built for this IMasterRule
   */
  public static IRule createRule(Element e, String ruleName, Map<String, ICellGroup> cellGroupMap,
      Function<IMove, Boolean> cond) {
    List<Function<IMove, Boolean>> conditions = new ArrayList<>();
    conditions.add(cond);

    try {
      Function<IMove, ICell> moverCell = (IMove move) -> move.getMover();
      Function<IMove, ICell> donorCell = (IMove move) -> move.getDonor();
      Function<IMove, ICell> recipientCell = (IMove move) -> move.getRecipient();
      Function<IMove, ICell> currCell = MasterRuleFactory
          .getCurrentCellFunction(ruleName, moverCell, donorCell, recipientCell);
      if (!ruleName.substring(ruleName.length() - 1).equalsIgnoreCase(PhaseFactory.C)) {
        extractValueCondition(e, conditions, recipientCell, currCell);
        extractColorCondition(e, conditions, recipientCell, currCell);
        extractSuitCondition(e, conditions, recipientCell, currCell);
        extractNumCardsCondition(e, conditions, currCell);
        extractFaceUpCondition(e, conditions, currCell);
        extractNameCondition(e, cellGroupMap, conditions, currCell);
      }
      extractConditionCondition(e, cellGroupMap, conditions);
      return new Rule(ruleName, conditions);
    } catch (Exception ee) {
      throw new XMLException(ee, Factory.MISSING_ERROR + "," + RESOURCES.getString(RULES));
    }
  }

  /**
   * Extracts the global conditions that must be met for a rule to be considered valid.
   *
   * @param e            the Element from which to parse conditions
   * @param cellGroupMap the Map of String ICellGroup names to ICellGroups
   * @param conditions   the List of Functions to which conditions should be added
   */
  private static void extractConditionCondition(Element e, Map<String, ICellGroup> cellGroupMap,
      List<Function<IMove, Boolean>> conditions) {
    String cellOrGroupName = XMLHelper.getAttribute(e, RESOURCES.getString(CATEGORY));
    if (cellGroupMap != null && !TRUE_CHECKS.contains(cellOrGroupName)) {
      List<ICell> allMatchingCells = new ArrayList<>();
      for (Map.Entry<String, ICellGroup> entry : cellGroupMap.entrySet()) {
        allMatchingCells.addAll(entry.getValue().getCellsbyName(cellOrGroupName));
      }
      for (ICell cell : allMatchingCells) {
        extractFaceUpCondition(e, conditions, (IMove move) -> cell);
        extractColorCondition(e, conditions, (IMove move) -> cell, (IMove move) -> cell);
        extractSuitCondition(e, conditions, (IMove move) -> cell, (IMove move) -> cell);
        extractNumCardsCondition(e, conditions, (IMove move) -> cell);
      }
    }
  }

  /**
   * Extracts how many cards should be part of an IMove for this ICell and adds it as a Function to
   * conditions.
   *
   * @param e          the Element from which to parse conditions
   * @param conditions the List of Functions to which conditions should be added
   * @param currCell   a Function to retrieve the current cell
   */
  private static void extractNumCardsCondition(Element e, List<Function<IMove, Boolean>> conditions,
      Function<IMove, ICell> currCell) {
    Function<IMove, Boolean> valueChecker;
    String numCards = XMLHelper.getTextValue(e, RESOURCES.getString(NUMBER_CARDS)).strip();
    if (!TRUE_CHECKS.contains(numCards)) {
      try {
        Integer value = Integer.parseInt(numCards);
        valueChecker = (IMove move) -> currCell.apply(move).getTotalSize() == value;
        conditions.add(valueChecker);
      } catch (NumberFormatException ex) {
      }
    }
  }

  /**
   * Extracts what value should be this part of an IMove for this ICell and adds it as a Function to
   * conditions.
   *
   * @param e             the Element from which to parse conditions
   * @param conditions    the List of Functions to which conditions should be added
   * @param recipientCell a Function to retrieve the recipient cell
   * @param currCell      a Function to retrieve the current cell
   */
  private static void extractValueCondition(Element e, List<Function<IMove, Boolean>> conditions,
      Function<IMove, ICell> recipientCell, Function<IMove, ICell> currCell) {
    Function<IMove, Boolean> valueChecker;
    String valueText = XMLHelper.getTextValue(e, RESOURCES.getString(VALUE));
    if (!TRUE_CHECKS.contains(valueText)) {
      if (valueText.equalsIgnoreCase(RESOURCES.getString(LESS_THAN))) {
        valueChecker = (IMove move) -> {
          int currNumber = currCell.apply(move).getDeck().peek().getValue().getNumber();
          int recNumber = recipientCell.apply(move).getDeck().peek().getValue().getNumber();
          return currNumber < recNumber;
        };
        conditions.add(valueChecker);
      } else if (valueText.equalsIgnoreCase(RESOURCES.getString(GREATER_THAN))) {
        valueChecker = (IMove move) -> {
          int currNumber = currCell.apply(move).getDeck().peek().getValue().getNumber();
          int recNumber = recipientCell.apply(move).getDeck().peek().getValue().getNumber();
          return currNumber > recNumber;
        };
        conditions.add(valueChecker);
      } else {
        try {
          Integer value = Integer.parseInt(valueText);
          valueChecker = (IMove move) -> {
            int currNumber = currCell.apply(move).getDeck().peek().getValue().getNumber();
            int recNumber = recipientCell.apply(move).getDeck().peek().getValue().getNumber();
            return currNumber - value == recNumber;
          };
          conditions.add(valueChecker);
        } catch (NumberFormatException ex) {
        }
      }
    }
  }

  /**
   * Extracts what color should be this part of an IMove for this ICell and adds it as a Function to
   * conditions.
   *
   * @param e             the Element from which to parse conditions
   * @param conditions    the List of Functions to which conditions should be added
   * @param recipientCell a Function to retrieve the recipient cell
   * @param currCell      a Function to retrieve the current cell
   */
  private static void extractColorCondition(Element e, List<Function<IMove, Boolean>> conditions,
      Function<IMove, ICell> recipientCell, Function<IMove, ICell> currCell) {
    Function<IMove, Boolean> colorChecker;
    String color = XMLHelper.getTextValue(e, RESOURCES.getString(COLOR));
    if (!TRUE_CHECKS.contains(color)) {
      if (color.equals(RESOURCES.getString(SAME))) {
        colorChecker = (IMove move) -> {
          String currColor = currCell.apply(move).getDeck().peek().getSuit().getColorName();
          String recColor = recipientCell.apply(move).getDeck().peek().getSuit().getColorName();
          return currColor.equalsIgnoreCase(recColor);
        };
      } else if (color.equals(RESOURCES.getString(NOT))) {
        colorChecker = (IMove move) -> {
          String currColor = currCell.apply(move).getDeck().peek().getSuit().getColorName();
          String recColor = recipientCell.apply(move).getDeck().peek().getSuit().getColorName();
          return !(currColor.equalsIgnoreCase(recColor));
        };
      } else {
        colorChecker = (IMove move) -> {
          String currColor = currCell.apply(move).getDeck().peek().getSuit().getColorName();
          String recColor = recipientCell.apply(move).getDeck().peek().getSuit().getColorName();
          return currColor.equalsIgnoreCase(recColor);
        };
      }
      conditions.add(colorChecker);
    }
  }

  /**
   * Extracts what suit should be this part of an IMove for this ICell and adds it as a Function to
   * conditions.
   *
   * @param e             the Element from which to parse conditions
   * @param conditions    the List of Functions to which conditions should be added
   * @param recipientCell a Function to retrieve the recipient cell
   * @param currCell      a Function to retrieve the current cell
   */
  private static void extractSuitCondition(Element e, List<Function<IMove, Boolean>> conditions,
      Function<IMove, ICell> recipientCell, Function<IMove, ICell> currCell) {
    Function<IMove, Boolean> suitChecker;
    String suit = XMLHelper.getTextValue(e, RESOURCES.getString(SUIT));
    if (!TRUE_CHECKS.contains(suit)) {
      if (suit.equals(RESOURCES.getString(SAME))) {
        suitChecker = (IMove move) -> {
          String currSuit = currCell.apply(move).getDeck().peek().getSuit().getName();
          String recSuit = recipientCell.apply(move).getDeck().peek().getSuit().getName();
          return currSuit.equalsIgnoreCase(recSuit);
        };
      } else if (suit.equals(RESOURCES.getString(NOT))) {
        suitChecker = (IMove move) -> {
          String currSuit = currCell.apply(move).getDeck().peek().getSuit().getName();
          String recSuit = recipientCell.apply(move).getDeck().peek().getSuit().getName();
          return !(currSuit.equalsIgnoreCase(recSuit));
        };
      } else {
        suitChecker = (IMove move) -> {
          String currSuit = currCell.apply(move).getDeck().peek().getSuit().getName();
          String recSuit = recipientCell.apply(move).getDeck().peek().getSuit().getName();
          return currSuit.equalsIgnoreCase(recSuit);
        };
      }
      conditions.add(suitChecker);
    }
  }

  /**
   * Extracts whether an ICell should be faceup and adds it as a Function to conditions.
   *
   * @param e          the Element from which to parse conditions
   * @param conditions the List of Functions to which conditions should be added
   * @param currCell   a Function to retrieve the current cell
   */
  private static void extractFaceUpCondition(Element e, List<Function<IMove, Boolean>> conditions,
      Function<IMove, ICell> currCell) {
    Function<IMove, Boolean> valueChecker;
    String faceUp = XMLHelper.getTextValue(e, RESOURCES.getString(IS_FACEUP));
    if (!TRUE_CHECKS.contains(faceUp)) {
      if (faceUp.equalsIgnoreCase(RESOURCES.getString(YES))) {
        valueChecker = (IMove move) -> (currCell.apply(move).getDeck().peek().isFaceUp());
      } else {
        valueChecker = (IMove move) -> !(currCell.apply(move).getDeck().peek().isFaceUp());
      }
      conditions.add(valueChecker);
    }
  }

  /**
   * Extracts the name of an ICell that must match the particular Rule and adds it as a Function to
   * conditions.
   *
   * @param e            the Element from which to parse conditions
   * @param cellGroupMap the Map of String ICellGroup names to ICellGroups
   * @param conditions   the List of Functions to which conditions should be added
   * @param currCell     a Function to retrieve the current cell
   */
  private static void extractNameCondition(Element e, Map<String, ICellGroup> cellGroupMap,
      List<Function<IMove, Boolean>> conditions, Function<IMove, ICell> currCell) {
    Function<IMove, Boolean> valueChecker;
    String name = XMLHelper.getTextValue(e, RESOURCES.getString(NAME));
    if (!TRUE_CHECKS.contains(name)) {
      valueChecker = (IMove move) -> {
        String currHeadName = currCell.apply(move).findHead().getName();
        boolean isAGroup = cellGroupMap.containsKey(name);
        boolean cellNameValid = currCell.apply(move).findHead().getName().equalsIgnoreCase(name);
        return (isAGroup && cellGroupMap.get(name).isInGroup(currHeadName)) || (cellNameValid);
      };
      conditions.add(valueChecker);
    }
  }

}
