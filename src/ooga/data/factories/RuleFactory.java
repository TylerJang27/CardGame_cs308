package ooga.data.factories;

import ooga.cardtable.ICell;
import ooga.cardtable.IMove;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.rules.ICellGroup;
import ooga.data.rules.IRule;
import ooga.data.rules.Rule;
import org.w3c.dom.Element;

import java.util.*;
import java.util.function.Function;

public class RuleFactory implements Factory {
    private static final ResourceBundle RESOURCES = PhaseFactory.RESOURCES;

    private static final String PHASE = PhaseFactory.PHASE;
    private static final String NAME = PhaseFactory.NAME;
    private static final String PHASE_TYPE = PhaseFactory.PHASE_TYPE;
    private static final String MANUAL = PhaseFactory.MANUAL;
    private static final String AUTO = PhaseFactory.AUTO;
    private static final String AUTOMATIC = PhaseFactory.AUTOMATIC;
    private static final String VALID_DONORS = PhaseFactory.VALID_DONORS;
    private static final String CATEGORY = PhaseFactory.CATEGORY;
    private static final String RULES = PhaseFactory.RULES;
    private static final String RULE = PhaseFactory.RULE;
    private static final String RECEIVE_RULE = PhaseFactory.RECEIVE_RULE;
    private static final String RECEIVER = PhaseFactory.RECEIVER;
    private static final String MOVER = PhaseFactory.MOVER;
    private static final String DIRECTION = PhaseFactory.DIRECTION;
    private static final String VALUE = PhaseFactory.VALUE;
    private static final String COLOR = PhaseFactory.COLOR;
    private static final String SUIT = PhaseFactory.SUIT;
    private static final String NUMBER_CARDS = PhaseFactory.NUMBER_CARDS;
    private static final String IS_FACEUP = PhaseFactory.IS_FACEUP;
    private static final String DONOR = PhaseFactory.DONOR;
    private static final String ALL_STAR = PhaseFactory.ALL_STAR;
    private static final String ACTION = PhaseFactory.ACTION;
    private static final String RECEIVER_DESTINATION = PhaseFactory.RECEIVER_DESTINATION;
    private static final String DESTINATION = PhaseFactory.DESTINATION;
    private static final String STACK = PhaseFactory.STACK;
    private static final String SHUFFLE = PhaseFactory.SHUFFLE;
    private static final String OFFSET = PhaseFactory.OFFSET;
    private static final String MOVER_DESTINATION = PhaseFactory.MOVER_DESTINATION;
    private static final String FLIP = PhaseFactory.FLIP;
    private static final String POINTS = PhaseFactory.POINTS;
    private static final String NEXT_PHASE = PhaseFactory.NEXT_PHASE;
    private static final String DONOR_DESTINATION = PhaseFactory.DONOR_DESTINATION;
    private static final String CONDITION = PhaseFactory.CONDITION;

    private static final String R = PhaseFactory.R;
    private static final String M = PhaseFactory.M;
    private static final String D = PhaseFactory.D;
    private static final String C = PhaseFactory.C;
    private static final String UP = PhaseFactory.UP;
    private static final String DOWN = PhaseFactory.DOWN;
    private static final String NOT = PhaseFactory.NOT;
    private static final String SAME = PhaseFactory.SAME;
    private static final String YES = PhaseFactory.YES;
    private static final String NO = PhaseFactory.NO;

    private static final List<String> TRUE_CHECKS = MasterRuleFactory.TRUE_CHECKS;
    
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
            throw new XMLException(ee, Factory.MISSING_ERROR + "," + RESOURCES.getString(RULES));
        }
    }

    private static void extractConditionCondition(Element e, Map<String, ICellGroup> cellGroupMap, List<Function<IMove, Boolean>> conditions) {
        String cellOrGroupName = XMLHelper.getAttribute(e, RESOURCES.getString(CATEGORY));
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
        String name = XMLHelper.getTextValue(e, RESOURCES.getString(NAME));
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
        String faceUp = XMLHelper.getTextValue(e, RESOURCES.getString(IS_FACEUP));
        if (!TRUE_CHECKS.contains(faceUp)) {
            if (faceUp.equalsIgnoreCase(RESOURCES.getString(YES))) {
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
        //System.out.println(RESOURCES.getString(ALL).strip());
        //System.out.println(XMLHelper.getTextValue(e, RESOURCES.getString(NUMBER_CARDS)));
        String numCards = XMLHelper.getTextValue(e, RESOURCES.getString(NUMBER_CARDS)).strip();
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
        String suit = XMLHelper.getTextValue(e, RESOURCES.getString(SUIT));
        if (!TRUE_CHECKS.contains(suit)) {
            if (suit.equals(RESOURCES.getString(SAME))) {
                valueChecker = (IMove move) -> {
                    //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                    //System.out.println("\tcurr: " + currCell.apply(move).getName());
                    //System.out.println("\t\tsuit: " + (currCell.apply(move).getDeck().peek().getSuit().getName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peek().getSuit().getName())));
                    return (currCell.apply(move).getDeck().peek().getSuit().getName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peek().getSuit().getName()));
                };
            } else if (suit.equals(RESOURCES.getString(NOT))) {
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
        String color = XMLHelper.getTextValue(e, RESOURCES.getString(COLOR));
        if (!TRUE_CHECKS.contains(color)) {
            if (color.equals(RESOURCES.getString(SAME))) {
                valueChecker = (IMove move) -> {
                    //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                    //System.out.println("\tcurr: " + currCell.apply(move).getName());
                    //System.out.println("\t\tcolor: " + (currCell.apply(move).getDeck().peek().getSuit().getColorName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peek().getSuit().getColorName())));
                    return currCell.apply(move).getDeck().peek().getSuit().getColorName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peek().getSuit().getColorName());
                };
            } else if (color.equals(RESOURCES.getString(NOT))) {
                valueChecker = (IMove move) -> {
                    //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                    //System.out.println("\tcurr: " + currCell.apply(move).getName());
                    //System.out.println("\t\tcolor: " +!(currCell.apply(move).getDeck().peek().getSuit().getColorName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peek().getSuit().getColorName())));
                    return !(currCell.apply(move).getDeck().peek().getSuit().getColorName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peek().getSuit().getColorName()));
                };
            } else {
                valueChecker = (IMove move) -> {
                    //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                    //System.out.println("\tcurr: " + currCell.apply(move).getName());
                    //System.out.println("\t\tcolor: " +  (currCell.apply(move).getDeck().peek().getSuit().getColorName().equalsIgnoreCase(color.toUpperCase())));
                    return (currCell.apply(move).getDeck().peek().getSuit().getColorName().equalsIgnoreCase(color.toUpperCase()));
                };
            }
            conditions.add(valueChecker);
        }
    }

    private static void extractValueCondition(Element e, List<Function<IMove, Boolean>> conditions, Function<IMove, ICell> recipientCell, Function<IMove, ICell> currCell) {
        Function<IMove, Boolean> valueChecker;
        String direction = XMLHelper.getTextValue(e, RESOURCES.getString(DIRECTION));
        String valueText = XMLHelper.getTextValue(e, RESOURCES.getString(VALUE));
        if (!TRUE_CHECKS.contains(valueText) && !TRUE_CHECKS.contains(direction)) {
            Integer value;
            if (direction.equals(RESOURCES.getString(DOWN))) {
                value = -1 * Integer.parseInt(valueText);
            } else {
                value = Integer.parseInt(valueText);
            }

            valueChecker = (IMove move) -> {
                //System.out.println("checking the value of mover:" + currCell.apply(move).getDeck());
                //System.out.println("checking the value of mover:" + currCell.apply(move).getDeck().peek().getValue().getNumber());
                //System.out.println("checking the value of rec:" + recipientCell.apply(move).getDeck().peek().getValue());
                //System.out.println("checking the value of rec:" + recipientCell.apply(move).getDeck().peek().getValue().getNumber());
                //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                //System.out.println("\tcurr: " + currCell.apply(move).getName());
                //System.out.println("\t\tvalue: " + (currCell.apply(move).getDeck().peek().getValue().getNumber() - value ==
                //        recipientCell.apply(move).getDeck().peek().getValue().getNumber()));

                return (currCell.apply(move).getDeck().peek().getValue().getNumber() - value ==
                        recipientCell.apply(move).getDeck().peek().getValue().getNumber());
                };
            conditions.add(valueChecker);
        }
    }
}
