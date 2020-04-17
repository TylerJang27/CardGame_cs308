package ooga.data.factories;

import ooga.cardtable.ICell;
import ooga.cardtable.IMove;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.rules.ICellGroup;
import ooga.data.rules.IRule;
import ooga.data.rules.Rule;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import java.util.*;
import java.util.function.Function;

public class RuleFactory implements Factory {
    private static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
    private static final String PHASES = "phases";
    private static final ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_PACKAGE+PHASES);

    private static final String CATEGORY = "Category";
    private static final String CONDITION = "Condition";
    private static final String RULES = "Rules";
    private static final String RULE = "Rule";
    private static final String RECEIVE_RULE = "ReceiveRule";
    private static final String RECEIVER = "Receiver";
    private static final String MOVER = "Mover";
    private static final String DIRECTION = "Direction";
    private static final String VALUE = "Value";
    private static final String COLOR = "Color";
    private static final String SUIT = "Suit";
    private static final String NUMBER_CARDS = "NumberCards";
    private static final String IS_FACEUP = "IsFaceup";
    private static final String NAME = "Name";
    private static final String DONOR = "Donor";
    private static final String ALL = "All";
    private static final String ACTION = "Action";
    private static final String RECEIVER_DESTINATION = "ReceiverDestination";
    private static final String DESTINATION = "Destination";
    private static final String STACK = "Stack";
    private static final String SHUFFLE = "Shuffle";
    private static final String OFFSET = "Offset";
    private static final String MOVER_DESTINATION = "MoverDestination";
    private static final String FLIP = "Flip";
    private static final String POINTS = "Points";
    private static final String NEXT_PHASE = "NextPhase";

    private static final String R = "R";
    private static final String M = "M";
    private static final String D = "D";
    private static final String C = "C";
    private static final String UP = "Up";
    private static final String DOWN = "Down";
    private static final String NOT = "Not";
    private static final String SAME = "Same";
    private static final String YES = "Yes";
    private static final String NO = "No";

    private static DocumentBuilder documentBuilder;
    public static final List<String> TRUE_CHECKS = List.of("", resources.getString(ALL).strip());

    public RuleFactory() { documentBuilder = XMLHelper.getDocumentBuilder();}

    public static IRule buildRule(Element e, String ruleName, Map<String, ICellGroup> cellGroupMap) {
        return buildRule(e, ruleName, cellGroupMap, (IMove move)->{
            //System.out.println("default true");
            return true;
        });
    }

    public static IRule buildRule(Element e, String ruleName, Map<String, ICellGroup> cellGroupMap, Function<IMove, Boolean> cond) {
        List<Function<IMove, Boolean>> conditions = new ArrayList<>();
        conditions.add(cond);

        try {
            Function<IMove, ICell> moverCell = (IMove move) -> move.getMover();
            Function<IMove, ICell> donorCell = (IMove move) -> move.getDonor();
            //Function<IMove, ICell> recipientCell = (IMove move) -> move.getRecipient();
            Function<IMove, ICell> recipientCell = (IMove move) -> move.getRecipient();
            Function<IMove, ICell> currCell = MasterRuleFactory.getCurrentCellFunction(ruleName, moverCell, donorCell, recipientCell);

            extractValueCondition(e, conditions, recipientCell, currCell);
            extractColorCondition(e, conditions, recipientCell, currCell);
            extractSuitCondition(e, conditions, recipientCell, currCell);
            extractNumCardsCondition(e, conditions, currCell);
            extractFaceUpCondition(e, conditions, currCell);
            extractNameCondition(e, cellGroupMap, conditions, currCell);

            extractConditionCondition(e, cellGroupMap, conditions);
            //System.out.println(conditions.size() + " is my condition size");
            return new Rule(ruleName, conditions);
        } catch (Exception ee) {
            throw new XMLException(ee, Factory.MISSING_ERROR + "," + resources.getString(RULES));
        }
    }

    private static void extractConditionCondition(Element e, Map<String, ICellGroup> cellGroupMap, List<Function<IMove, Boolean>> conditions) {
        String cellOrGroupName = XMLHelper.getAttribute(e, resources.getString(CATEGORY));
        if (cellGroupMap != null && !TRUE_CHECKS.contains(cellOrGroupName)) {
            List<ICell> allMatchingCells = new ArrayList<>();
            for (Map.Entry<String, ICellGroup> entry: cellGroupMap.entrySet()) {
                allMatchingCells.addAll(entry.getValue().getCellsbyName(cellOrGroupName));
            }
            for (ICell cell: allMatchingCells) {
                extractFaceUpCondition(e, conditions, (IMove move) -> cell);
                extractColorCondition(e, conditions, (IMove move) -> cell, (IMove move) -> cell);
                extractSuitCondition(e, conditions, (IMove move) -> cell, (IMove move) -> cell);
                extractNumCardsCondition(e, conditions, (IMove move) -> cell);
            }
        }
    }

    private static void extractNameCondition(Element e, Map<String, ICellGroup> cellGroupMap, List<Function<IMove, Boolean>> conditions, Function<IMove, ICell> currCell) {
        Function<IMove, Boolean> valueChecker;
        String name = XMLHelper.getTextValue(e, resources.getString(NAME));
        if (!TRUE_CHECKS.contains(name)) {
            valueChecker = (IMove move) -> {
                //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                //System.out.println("\tcurr: " + currCell.apply(move).getName());
                //System.out.println("\t\tname result: " + ((cellGroupMap.containsKey(name) && cellGroupMap.get(name).isInGroup(currCell.apply(move).findHead().getName())) || (currCell.apply(move).findHead().getName().equalsIgnoreCase(name))));
                return (cellGroupMap.containsKey(name) && cellGroupMap.get(name).isInGroup(currCell.apply(move).findHead().getName())) || (currCell.apply(move).findHead().getName().equalsIgnoreCase(name));
            };
            conditions.add(valueChecker);
        }
    }

    private static void extractFaceUpCondition(Element e, List<Function<IMove, Boolean>> conditions, Function<IMove, ICell> currCell) {
        Function<IMove, Boolean> valueChecker;
        String faceUp = XMLHelper.getTextValue(e, resources.getString(IS_FACEUP));
        if (!TRUE_CHECKS.contains(faceUp)) {
            if (faceUp.equalsIgnoreCase(resources.getString(YES))) {
                valueChecker = (IMove move) -> {
                    //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                    //System.out.println("\tcurr: " + currCell.apply(move).getName());
                    //System.out.println("\t\tfaceupY: " + (currCell.apply(move).getDeck().peek().isFaceUp()));
                    return (currCell.apply(move).getDeck().peek().isFaceUp());
                };
            } else {
                valueChecker = (IMove move) -> {
                    //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                    //System.out.println("\tcurr: " + currCell.apply(move).getName());
                    //System.out.println("deck faceup?: " + currCell.apply(move).getDeck().peek().isFaceUp());
                    //System.out.println("\t\tfaceupN: " + !(currCell.apply(move).getDeck().peek().isFaceUp()));
                    return !(currCell.apply(move).getDeck().peek().isFaceUp());
                };
            }
            conditions.add(valueChecker);
        }
    }

    private static void extractNumCardsCondition(Element e, List<Function<IMove, Boolean>> conditions, Function<IMove, ICell> currCell) {
        Function<IMove, Boolean> valueChecker;
        //System.out.println("yolofdsafdsafdsafdasfsdaffs");
        //System.out.println(resources.getString(ALL).strip());
        //System.out.println(XMLHelper.getTextValue(e, resources.getString(NUMBER_CARDS)));
        String numCards = XMLHelper.getTextValue(e, resources.getString(NUMBER_CARDS)).strip();
        if (!TRUE_CHECKS.contains(numCards)) {
            Integer value = Integer.parseInt(numCards);
            valueChecker = (IMove move) -> {
                //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                //System.out.println("\tcurr: " + currCell.apply(move).getName());
                //System.out.println("\texpected numcards value: " + value);
                //System.out.println("\t\tnumcards: " + (currCell.apply(move).getTotalSize() == value)); //FIXME: BREAKS HERE
                return (currCell.apply(move).getTotalSize() == value);
            };
            conditions.add(valueChecker);
        }
    }

    private static void extractSuitCondition(Element e, List<Function<IMove, Boolean>> conditions, Function<IMove, ICell> recipientCell, Function<IMove, ICell> currCell) {
        Function<IMove, Boolean> valueChecker;
        String suit = XMLHelper.getTextValue(e, resources.getString(SUIT));
        if (!TRUE_CHECKS.contains(suit)) {
            if (suit.equals(resources.getString(SAME))) {
                valueChecker = (IMove move) -> {
                    //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                    //System.out.println("\tcurr: " + currCell.apply(move).getName());
                    //System.out.println("\t\tsuit: " + (currCell.apply(move).getDeck().peek().getSuit().getName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peek().getSuit().getName())));
                    return (currCell.apply(move).getDeck().peek().getSuit().getName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peek().getSuit().getName()));
                };
            } else if (suit.equals(resources.getString(NOT))) {
                valueChecker = (IMove move) -> {
                    //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                    //System.out.println("\tcurr: " + currCell.apply(move).getName());
                    //System.out.println("\t\tsuit: " + !(currCell.apply(move).getDeck().peek().getSuit().getName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peek().getSuit().getName())));
                    return !(currCell.apply(move).getDeck().peek().getSuit().getName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peek().getSuit().getName()));
                };
            } else {
                valueChecker = (IMove move) -> {
                    //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                    //System.out.println("\tcurr: " + currCell.apply(move).getName());
                    //System.out.println("\t\tsuit: " + (currCell.apply(move).getDeck().peek().getSuit().getName().equalsIgnoreCase(suit.toUpperCase())));
                    return (currCell.apply(move).getDeck().peek().getSuit().getName().equalsIgnoreCase(suit.toUpperCase()));
                };
            }
            conditions.add(valueChecker);
        }
    }

    private static void extractColorCondition(Element e, List<Function<IMove, Boolean>> conditions, Function<IMove, ICell> recipientCell, Function<IMove, ICell> currCell) {
        Function<IMove, Boolean> valueChecker;
        String color = XMLHelper.getTextValue(e, resources.getString(COLOR));
        if (!TRUE_CHECKS.contains(color)) {
            if (color.equals(resources.getString(SAME))) {
                valueChecker = (IMove move) -> {
                    //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                    //System.out.println("\tcurr: " + currCell.apply(move).getName());
                    //System.out.println("\t\tcolor: " + (currCell.apply(move).getDeck().peek().getSuit().getColorName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peek().getSuit().getColorName())));
                    return currCell.apply(move).getDeck().peek().getSuit().getColorName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peek().getSuit().getColorName());
                };
            } else if (color.equals(resources.getString(NOT))) {
                valueChecker = (IMove move) -> {
                    //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                    //System.out.println("\tcurr: " + currCell.apply(move).getName());
                    //System.out.println("\t\tcolor: " +!(currCell.apply(move).getDeck().peek().getSuit().getColorName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peek().getSuit().getColorName())));
                    return !(currCell.apply(move).getDeck().peek().getSuit().getColorName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peek().getSuit().getColorName()));
                };
            } else {
                valueChecker = (IMove move) -> {
                    System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                    System.out.println("\tcurr: " + currCell.apply(move).getName());
                    System.out.println("\t\tcolor: " +  (currCell.apply(move).getDeck().peek().getSuit().getColorName().equalsIgnoreCase(color.toUpperCase())));
                    return (currCell.apply(move).getDeck().peek().getSuit().getColorName().equalsIgnoreCase(color.toUpperCase()));
                };
            }
            conditions.add(valueChecker);
        }
    }

    private static void extractValueCondition(Element e, List<Function<IMove, Boolean>> conditions, Function<IMove, ICell> recipientCell, Function<IMove, ICell> currCell) {
        Function<IMove, Boolean> valueChecker;
        String direction = XMLHelper.getTextValue(e, resources.getString(DIRECTION));
        String valueText = XMLHelper.getTextValue(e, resources.getString(VALUE));
        if (!TRUE_CHECKS.contains(valueText) && !TRUE_CHECKS.contains(direction)) {
            Integer value;
            if (direction.equals(resources.getString(DOWN))) {
                value = -1 * Integer.parseInt(valueText);
            } else {
                value = Integer.parseInt(valueText);
            }

            valueChecker = (IMove move) -> {
                //System.out.println("checking the value of mover:" + currCell.apply(move).getDeck());
                //System.out.println("checking the value of mover:" + currCell.apply(move).getDeck().peek().getValue().getNumber());
                //System.out.println("checking the value of rec:" + recipientCell.apply(move).getDeck().peek().getValue());
                //System.out.println("checking the value of rec:" + recipientCell.apply(move).getDeck().peek().getValue().getNumber());
                System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                System.out.println("\tcurr: " + currCell.apply(move).getName());
                System.out.println("\t\tvalue: " + (currCell.apply(move).getDeck().peek().getValue().getNumber() - value ==
                        recipientCell.apply(move).getDeck().peek().getValue().getNumber()));

                return (currCell.apply(move).getDeck().peek().getValue().getNumber() - value ==
                        recipientCell.apply(move).getDeck().peek().getValue().getNumber());
                };
            conditions.add(valueChecker);
        }
    }
}
