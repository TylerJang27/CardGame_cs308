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
    private static final String DONOR = "Donor";
    private static final String ALL = "*";
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
                    receiverRuleList.add(buildRule((Element)recRule, ruleName + R, (IMove move) -> checkRecipient(move, ruleName)));
                }
                Node movRule = (Element)XMLHelper.getNodeByName(allConditions, resources.getString(MOVER));
                if (movRule != null) {
                    moverRuleList.add(buildRule((Element) movRule, ruleName + M));
                }
                Node donRule = (Element)XMLHelper.getNodeByName(allConditions, resources.getString(DONOR));
                if (donRule != null) {
                    donorRuleList.add(buildRule((Element)donRule, ruleName + D));
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

    private static IRule buildRule(Element e, String ruleName) {
        return buildRule(e, ruleName, (IMove move)->true);
    }

    private static IRule buildRule(Element e, String ruleName, Function<IMove, Boolean> cond) {
        List<Function<IMove, Boolean>> conditions = new ArrayList<>();
        conditions.add(cond);

        NodeList conditionNodeList = e.getChildNodes();


        return new Rule(ruleName, conditions)
    }

    private static Boolean checkRecipient(IMove move, String name) {
        return name.equals(move.getRecipient().getName().split(",")[0]);
    }

}
