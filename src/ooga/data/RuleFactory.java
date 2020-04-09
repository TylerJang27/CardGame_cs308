package ooga.data;

import ooga.cardtable.ICell;
import ooga.cardtable.IMove;
import ooga.data.rules.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.util.*;
import java.util.function.Function;

public class RuleFactory implements Factory {
    private static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
    private static final String PHASES = "phases";
    private static final ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_PACKAGE+PHASES);

    private static final String CATEGORY = "Category";
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
    private static final String UP = "Up";
    private static final String DOWN = "Down";
    private static final String NOT = "Not";
    private static final String SAME = "Same";
    private static final String YES = "Yes";
    private static final String NO = "No";

    private static DocumentBuilder documentBuilder;

    public RuleFactory() { documentBuilder = XMLHelper.getDocumentBuilder();}

    public static List<IMasterRule> getRules(Node rules, Map<String, ICellGroup> cellGroupMap, Map<String, ICell> cellMap) {
        Map<String, IMasterRule> ruleMap = new HashMap<>();
        Map<IMasterRule, List<ICardAction>> ruleActionMap = new HashMap<>();

        NodeList ruleNodeList = ((Element)rules).getElementsByTagName(resources.getString(RULE));

        for (int k = 0; k < ruleNodeList.getLength(); k++) {
            Element ruleNode = (Element)ruleNodeList.item(k);
            String ruleName = XMLHelper.getAttribute(ruleNode, resources.getString(CATEGORY));

            //supports multiple receive rules
            NodeList receiverRuleNodeList = ruleNode.getElementsByTagName(resources.getString(RECEIVE_RULE));
            List<IRule> receiverRuleList = new ArrayList<>();
            List<IRule> moverRuleList = new ArrayList<>();
            List<IRule> donorRuleList = new ArrayList<>();

            for (int j = 0; j < receiverRuleNodeList.getLength(); j++) {
                Element receiverRuleNode = (Element)receiverRuleNodeList.item(j);

                NodeList allConditions = receiverRuleNode.getChildNodes();

                Node recRule = XMLHelper.getNodeByName(allConditions, resources.getString(RECEIVER));
                if (recRule != null) {
                    receiverRuleList.add(buildRule((Element)recRule, ruleName + R, cellGroupMap, (IMove move) -> checkRecipient(move, ruleName, cellGroupMap)));
                }
                Node movRule = (Element)XMLHelper.getNodeByName(allConditions, resources.getString(MOVER));
                if (movRule != null) {
                    moverRuleList.add(buildRule((Element) movRule, ruleName + M, cellGroupMap));
                }
                Node donRule = (Element)XMLHelper.getNodeByName(allConditions, resources.getString(DONOR));
                if (donRule != null) {
                    donorRuleList.add(buildRule((Element)donRule, ruleName + D, cellGroupMap));
                }
            }

            IMasterRule masterRule = new MasterRule(receiverRuleList, moverRuleList, donorRuleList);
            ruleMap.put(ruleName, masterRule);

            List<ICardAction> cardActions = new ArrayList<>();
            //donor action

            //other actions

            ruleActionMap.put(masterRule, cardActions);
        }
        return null;
    }

    private static IRule buildRule(Element e, String ruleName, Map<String, ICellGroup> cellGroupMap) {
        return buildRule(e, ruleName, cellGroupMap, (IMove move)->true);
    }

    private static IRule buildRule(Element e, String ruleName, Map<String, ICellGroup> cellGroupMap, Function<IMove, Boolean> cond) {
        List<Function<IMove, Boolean>> conditions = new ArrayList<>();
        conditions.add(cond);

        Function<IMove, ICell> moverCell = (IMove move) -> move.getMover();
        Function<IMove, ICell> donorCell = (IMove move) -> move.getDonor();
        Function<IMove, ICell> recipientCell = (IMove move) -> move.getRecipient();
        char currentChar = ruleName.charAt(ruleName.length()-1);
        Function<IMove, ICell> currCell;
        if (M.equals("" + currentChar)) {
            currCell = (IMove move) -> moverCell.apply(move);
        } else if (D.equals("" + currentChar)) {
            currCell = (IMove move) -> donorCell.apply(move);
        } else { //R
            currCell = (IMove move) -> recipientCell.apply(move);
        }

        NodeList conditionNodeList = e.getChildNodes();
        List<String> trueChecks = new ArrayList<String>(Arrays.asList(new String[]{"", resources.getString(ALL)}));
        Function<IMove, Boolean> valueChecker;

        String direction = XMLHelper.getTextValue(e, resources.getString(DIRECTION));
        String valueText = XMLHelper.getTextValue(e, resources.getString(VALUE));
        if (!trueChecks.contains(valueText) && !trueChecks.contains(direction)) { //don't forget *
            Integer value;
            if (direction.equals(resources.getString(DOWN))) {
                value = -1 * Integer.parseInt(valueText);
            } else {
                value = Integer.parseInt(valueText);
            }
            valueChecker = (IMove move) -> (currCell.apply(move).getDeck().peekBottom().getValue().getNumber() - value == recipientCell.apply(move).getDeck().peekBottom().getValue().getNumber());
            conditions.add(valueChecker);
        }

        String color = XMLHelper.getTextValue(e, resources.getString(COLOR));
        if (!trueChecks.contains(color)) {
            if (color.equals(resources.getString(SAME))) {
                valueChecker = (IMove move) -> (currCell.apply(move).getDeck().peekBottom().getSuit().getColorName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peekBottom().getSuit().getColorName()));
            } else if (color.equals(resources.getString(NOT))) {
                valueChecker = (IMove move) -> !(currCell.apply(move).getDeck().peekBottom().getSuit().getColorName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peekBottom().getSuit().getColorName()));
            } else {
                valueChecker = (IMove move) -> (currCell.apply(move).getDeck().peekBottom().getSuit().getColorName().equalsIgnoreCase(color.toUpperCase()));
            }
            conditions.add(valueChecker);
        }

        String suit = XMLHelper.getTextValue(e, resources.getString(SUIT));
        if (!trueChecks.contains(suit)) {
            if (suit.equals(resources.getString(SAME))) {
                valueChecker = (IMove move) -> (currCell.apply(move).getDeck().peekBottom().getSuit().getName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peekBottom().getSuit().getName()));
            } else if (suit.equals(resources.getString(NOT))) {
                valueChecker = (IMove move) -> !(currCell.apply(move).getDeck().peekBottom().getSuit().getName().equalsIgnoreCase(recipientCell.apply(move).getDeck().peekBottom().getSuit().getName()));
            } else {
                valueChecker = (IMove move) -> (currCell.apply(move).getDeck().peekBottom().getSuit().getName().equalsIgnoreCase(suit.toUpperCase()));
            }
            conditions.add(valueChecker);
        }

        String numCards = XMLHelper.getTextValue(e, resources.getString(NUMBER_CARDS));
        if (!trueChecks.contains(numCards)) {
            Integer value = Integer.parseInt(numCards);
            valueChecker = (IMove move) -> (currCell.apply(move).getTotalSize()==value);
            conditions.add(valueChecker);
        }

        String faceUp = XMLHelper.getTextValue(e, resources.getString(IS_FACEUP));
        if (!trueChecks.contains(faceUp)) {
            if (faceUp.equals(resources.getString(YES))) {
                valueChecker = (IMove move) -> (currCell.apply(move).getDeck().peekBottom().isFaceUp());
            } else {
                valueChecker = (IMove move) -> !(currCell.apply(move).getDeck().peekBottom().isFaceUp());
            }
            conditions.add(valueChecker);
        }

        String name = XMLHelper.getTextValue(e, resources.getString(NAME));
        if (!trueChecks.contains(name)) {
            valueChecker = (IMove move) -> (cellGroupMap.containsKey(name) && cellGroupMap.get(name).isInGroup(currCell.apply(move).getName()))||(currCell.apply(move).getName().split(",")[0].equalsIgnoreCase(name));
            conditions.add(valueChecker);
        }


        return new Rule(ruleName, conditions);
    }

    private static Boolean checkRecipient(IMove move, String name, Map<String, ICellGroup> cellGroupMap) {
        return (cellGroupMap.containsKey(name) && cellGroupMap.get(name).isInGroup(move.getRecipient().getName()))||name.equals(move.getRecipient().getName().split(",")[0]);
    }

}
