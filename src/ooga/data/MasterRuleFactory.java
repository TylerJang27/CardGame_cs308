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

public class MasterRuleFactory implements Factory {
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
    private static final String PHASE = "Phase";

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
    public static final List<String> TRUE_CHECKS = new ArrayList<String>(Arrays.asList(new String[]{"", resources.getString(ALL)}));

    public MasterRuleFactory() { documentBuilder = XMLHelper.getDocumentBuilder();}

    public static List<IMasterRule> getRules(Node rules, Map<String, ICellGroup> cellGroupMap, Map<String, ICell> cellMap, String phaseName) {
        List<String> masterRuleNames = new ArrayList<>();
        List<IMasterRule> masterRuleList = new ArrayList<>();
        Map<String, IMasterRule> ruleMap = new HashMap<>();
        Map<IMasterRule, List<ICardAction>> ruleActionMap = new HashMap<>();

        NodeList ruleNodeList = ((Element)rules).getElementsByTagName(resources.getString(RULE));

        for (int k = 0; k < ruleNodeList.getLength(); k++) {
            Element ruleNode = (Element)ruleNodeList.item(k);
            String ruleName = XMLHelper.getAttribute(ruleNode, resources.getString(CATEGORY));

            //supports multiple receive rules
            NodeList receiverRuleNodeList = ruleNode.getElementsByTagName(resources.getString(RECEIVE_RULE));
            List<IRule> allRules = new ArrayList<>();
            List<IRule> receiverRuleList = new ArrayList<>();
            List<IRule> moverRuleList = new ArrayList<>();
            List<IRule> donorRuleList = new ArrayList<>();
            List<IRule> autoRules = new ArrayList<>();

            for (int j = 0; j < receiverRuleNodeList.getLength(); j++) {
                Element receiverRuleNode = (Element)receiverRuleNodeList.item(j);

                NodeList allConditions = receiverRuleNode.getChildNodes();

                Node recRule = XMLHelper.getNodeByName(allConditions, resources.getString(RECEIVER));
                if (recRule != null) {
                    receiverRuleList.add(RuleFactory.buildRule((Element)recRule, ruleName + R, cellGroupMap, (IMove move) -> checkRecipient(move, ruleName, cellGroupMap)));
                }
                Node movRule = XMLHelper.getNodeByName(allConditions, resources.getString(MOVER));
                if (movRule != null) {
                    moverRuleList.add(RuleFactory.buildRule((Element) movRule, ruleName + M, cellGroupMap));
                }
                Node donRule = XMLHelper.getNodeByName(allConditions, resources.getString(DONOR));
                if (donRule != null) {
                    donorRuleList.add(RuleFactory.buildRule((Element)donRule, ruleName + D, cellGroupMap));
                }
                NodeList condRuleList = receiverRuleNode.getElementsByTagName(resources.getString(CONDITION));
                for (int l = 0; l < condRuleList.getLength(); l ++) {
                    Node condRule = condRuleList.item(l);
                    autoRules.add(RuleFactory.buildRule((Element)condRule, ruleName + C, cellGroupMap));
                }
            }
            allRules.addAll(receiverRuleList);
            allRules.addAll(moverRuleList);
            allRules.addAll(donorRuleList);
            //allRules.addAll(autoRules);

            List<ICardAction> cardActionList = new ArrayList<>();
            List<IControlAction> controlActionList = new ArrayList<>();
            NodeList actionList = ruleNode.getElementsByTagName(resources.getString(ACTION));               //TODO: REFACTOR FROM HERE TO AN ACTION FACTORY
            for (int j = 0; j < actionList.getLength(); j++) {
                Element actionHeadNode = (Element) actionList.item(j);

                NodeList allActions = actionHeadNode.getChildNodes();

                Node recAction = (Element)XMLHelper.getNodeByName(allActions, resources.getString(RECEIVER_DESTINATION));
                if (recAction != null) {
                    cardActionList.add(ActionFactory.getAction((Element)recAction, ruleName + R));
                    //TODO: FOLLOW CONVENTION WITH FACTORY METHOD NAMES
                }
                Node movAction = (Element)XMLHelper.getNodeByName(allActions, resources.getString(MOVER_DESTINATION));
                if (movAction != null) {
                    cardActionList.add(ActionFactory.getAction((Element)recAction, ruleName + M));
                }

                Node phaseAction = XMLHelper.getNodeByName(allActions, resources.getString(NEXT_PHASE));
                try {
                    String newPhase = XMLHelper.getAttribute((Element)phaseAction, resources.getString(PHASE));
                    String pointVal = phaseAction.getNodeValue();
                    Integer points = 0;
                    if (!pointVal.isEmpty()) {
                        points = Integer.parseInt(pointVal);
                    }
                    IPhaseArrow arrow = new PhaseArrow(phaseName, ruleName, newPhase);
                    controlActionList.add(new ControlAction(arrow, points));
                } catch (NullPointerException e) {
                    throw new XMLException(e, MISSING_ERROR + "," + resources.getString(NEXT_PHASE));
                }
            }
                                                                                                            //TODO: REFACTOR TO HERE TO AN ACTION FACTORY
            IMasterRule masterRule = new MasterRule(allRules, autoRules, cardActionList, controlActionList);
            masterRuleNames.add(ruleName);
            masterRuleList.add(masterRule);
            ruleMap.put(ruleName, masterRule);

            ruleActionMap.put(masterRule, cardActionList);
        }
        return masterRuleList;
    }

    protected static Function<IMove, ICell> getCurrentCellFunction(String ruleName, Function<IMove, ICell> moverCell, Function<IMove, ICell> donorCell, Function<IMove, ICell> recipientCell) {
        Function<IMove, ICell> currCell;
        char currentChar = ruleName.charAt(ruleName.length()-1);
        if (M.equals("" + currentChar)) {
            currCell = (IMove move) -> moverCell.apply(move);
        } else if (D.equals("" + currentChar)) {
            currCell = (IMove move) -> donorCell.apply(move);
        } else { //R
            currCell = (IMove move) -> recipientCell.apply(move);
        }
        return currCell;
    }

    private static Boolean checkRecipient(IMove move, String name, Map<String, ICellGroup> cellGroupMap) {
        return name.isEmpty()||(cellGroupMap.containsKey(name) && cellGroupMap.get(name).isInGroup(move.getRecipient().getName()))||name.equals(move.getRecipient().getName().split(",")[0]);
    }
}
