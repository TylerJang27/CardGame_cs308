package ooga.data.factories;

import ooga.cardtable.ICell;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.rules.ICellGroup;
import ooga.data.rules.IMasterRule;
import ooga.data.rules.IPhase;
import ooga.data.rules.Phase;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

public class PhaseFactory implements Factory {
    protected static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
    protected static final String PHASES = "phases";
    protected static final ResourceBundle RESOURCES = ResourceBundle.getBundle(RESOURCE_PACKAGE + PHASES);

    protected static final String PHASE = "Phase";
    protected static final String NAME = "Name";
    protected static final String PHASE_TYPE = "PhaseType";
    protected static final String MANUAL = "Manual";
    protected static final String AUTO = "Auto";
    protected static final String AUTOMATIC = "Automatic";
    protected static final String VALID_DONORS = "ValidDonors";
    protected static final String CATEGORY = "Category";
    protected static final String RULES = "Rules";
    protected static final String RULE = "Rule";
    protected static final String RECEIVE_RULE = "ReceiveRule";
    protected static final String RECEIVER = "Receiver";
    protected static final String MOVER = "Mover";
    protected static final String DIRECTION = "Direction";
    protected static final String VALUE = "Value";
    protected static final String COLOR = "Color";
    protected static final String SUIT = "Suit";
    protected static final String NUMBER_CARDS = "NumberCards";
    protected static final String IS_FACEUP = "IsFaceup";
    protected static final String DONOR = "Donor";
    protected static final String ALL = "*";
    protected static final String ACTION = "Action";
    protected static final String RECEIVER_DESTINATION = "ReceiverDestination";
    protected static final String DESTINATION = "Destination";
    protected static final String STACK = "Stack";
    protected static final String SHUFFLE = "Shuffle";
    protected static final String OFFSET = "Offset";
    protected static final String MOVER_DESTINATION = "MoverDestination";
    protected static final String FLIP = "Flip";
    protected static final String POINTS = "Points";
    protected static final String NEXT_PHASE = "NextPhase";
    protected static final String DONOR_DESTINATION = "DonorDestination";
    protected static final String CONDITION = "Condition";

    protected static final String R = "R";
    protected static final String M = "M";
    protected static final String D = "D";
    protected static final String C = "C";
    protected static final String UP = "Up";
    protected static final String DOWN = "Down";
    protected static final String NOT = "Not";
    protected static final String SAME = "Same";
    protected static final String YES = "Yes";
    protected static final String NO = "No";

    protected static final String TOP = "Top";
    protected static final String BOTTOM = "Bottom";
    protected static final String EXCEPT = "Except";
    protected static final String PRESERVE = "Preserve";
    protected static final String REVERSE = "Reverse";

    public static Map<String, IPhase> getPhases(Element root, Map<String, ICellGroup> cellGroupMap, Map<String, ICell> cellMap) {
        try {
            Node phases = root.getElementsByTagName(PHASES).item(0);

            NodeList phaseList = ((Element) phases).getElementsByTagName(RESOURCES.getString(PHASE));

            Map<String, IPhase> phaseMap = new HashMap<>();


            for (int k = 0; k < phaseList.getLength(); k++) {
                Element phase = (Element) phaseList.item(k);
                NodeList phaseNodes = phase.getChildNodes();

                //phase info and type
                String phaseName = XMLHelper.getAttribute(phase, RESOURCES.getString(NAME));
                boolean automatic = RESOURCES.getString(AUTOMATIC).equals(XMLHelper.getTextValue(phase, RESOURCES.getString(PHASE_TYPE)));

                //valid donors
                Element donorHeadNode = (Element) XMLHelper.getNodeByName(phaseNodes, RESOURCES.getString(VALID_DONORS));
                List<String> validDonorNames = new ArrayList<>();
                if (donorHeadNode.hasChildNodes()) {
                    NodeList donorNodeList = donorHeadNode.getElementsByTagName(RESOURCES.getString(CATEGORY));
                    for (int j = 0; j < donorNodeList.getLength(); j++) {
                        Node donor = donorNodeList.item(j);
                        validDonorNames.add(donor.getTextContent());
                    }
                }

                //rules
                Node rules = XMLHelper.getNodeByName(phaseNodes, RESOURCES.getString(RULES));
                List<IMasterRule> phaseRules = MasterRuleFactory.getRules(rules, cellGroupMap, cellMap, phaseName);

                //phase
                IPhase newPhase = new Phase(phaseName, phaseRules, validDonorNames, cellGroupMap, cellMap, automatic);
                if (phaseMap.isEmpty()) {
                    phaseMap.put(PhaseMachineFactory.START, newPhase);
                }
                phaseMap.put(phaseName, newPhase);

            }
            return phaseMap;

        } catch (Exception e) {
            throw new XMLException(e, MISSING_ERROR + "," + PHASES);
        }


    }

}
