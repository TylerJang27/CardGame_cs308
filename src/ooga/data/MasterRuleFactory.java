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

    public static List<IMasterRule> getRules(Node rules, Map<String, ICellGroup> cellGroupMap, Map<String, ICell> cellMap) {
        List<String> masterRuleNames = new ArrayList<>();
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
                Node movRule = (Element)XMLHelper.getNodeByName(allConditions, resources.getString(MOVER));
                if (movRule != null) {
                    moverRuleList.add(RuleFactory.buildRule((Element) movRule, ruleName + M, cellGroupMap));
                }
                Node donRule = (Element)XMLHelper.getNodeByName(allConditions, resources.getString(DONOR));
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
            //////////each master rule needs an action

            NodeList actionList = ruleNode.getElementsByTagName(resources.getString(ACTION));               //TODO: REFACTOR FROM HERE TO AN ACTION FACTORY
            for (int j = 0; j < actionList.getLength(); j++) {
                Element actionHeadNode = (Element) actionList.item(j);

                NodeList allActions = actionHeadNode.getChildNodes();

                Node recAction = XMLHelper.getNodeByName(allActions, resources.getString(RECEIVER_DESTINATION));

                Node movAction = XMLHelper.getNodeByName(allActions, resources.getString(MOVER_DESTINATION));

                Node phaseAction = XMLHelper.getNodeByName(allActions, resources.getString(NEXT_PHASE));
                try {
                    
                } catch (NullPointerException e) {
                    throw new XMLException(e, MISSING_ERROR + "," + resources.getString(NEXT_PHASE));
                }
            }
            List<ICardAction> cardActionList = new ArrayList<>();
            List<IControlAction> controlActionList = new ArrayList<>();

                                                                                                            //TODO: REFACTOR TO HERE TO AN ACTION FACTORY
            IMasterRule masterRule = new MasterRule(receiverRuleList, moverRuleList, donorRuleList);
            //entire rule list, auto rules, card actions, other actions
            masterRuleNames.add(ruleName);
            ruleMap.put(ruleName, masterRule);

            List<ICardAction> cardActions = new ArrayList<>();
            //donor action

            //other actions

            ruleActionMap.put(masterRule, cardActions);
        }
        return null;
    }



    private static Boolean checkRecipient(IMove move, String name, Map<String, ICellGroup> cellGroupMap) {
        return name.isEmpty()||(cellGroupMap.containsKey(name) && cellGroupMap.get(name).isInGroup(move.getRecipient().getName()))||name.equals(move.getRecipient().getName().split(",")[0]);
    }

}
