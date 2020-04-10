package ooga.data;

import ooga.cardtable.ICell;
import ooga.data.rules.ICellGroup;
import ooga.data.rules.IPhase;
import ooga.data.rules.IMasterRule;
import ooga.data.rules.Phase;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.util.*;

public class PhaseFactory implements Factory {
    private static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
    private static final String PHASES = "phases";
    private static final ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_PACKAGE+PHASES);

    private static final String PHASE = "Phase";
    private static final String NAME = "Name";
    private static final String PHASE_TYPE = "PhaseType";
    private static final String MANUAL = "Manual";
    private static final String AUTO = "Auto";
    private static final String VALID_DONORS = "ValidDonors";
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

    private static DocumentBuilder documentBuilder;

    public PhaseFactory() { documentBuilder = XMLHelper.getDocumentBuilder();}

    public static Map<String, IPhase> getPhases(Element root, Map<String, ICellGroup> cellGroupMap, Map<String, ICell> cellMap) {
        try {
            Node phases = root.getElementsByTagName(PHASES).item(0);

            NodeList phaseList = ((Element)phases).getElementsByTagName(resources.getString(PHASE));

            Map<String, IPhase> phaseMap = new HashMap<>();


            for (int k = 0; k < phaseList.getLength(); k ++) {
                Element phase = (Element)phaseList.item(k);
                NodeList phaseNodes = phase.getChildNodes();

                //phase info and type
                String phaseName = XMLHelper.getAttribute(phase, resources.getString(NAME));
                boolean automatic = AUTO.equals(XMLHelper.getTextValue(phase, resources.getString(PHASE_TYPE)));

                //valid donors
                Element donorHeadNode = (Element)XMLHelper.getNodeByName(phaseNodes, resources.getString(VALID_DONORS));
                List<String> validDonorNames = new ArrayList<>();
                if (donorHeadNode.hasChildNodes()) {
                    NodeList donorNodeList = donorHeadNode.getElementsByTagName(resources.getString(CATEGORY));
                    for (int j = 0; j < donorNodeList.getLength(); j++) {
                        Node donor = donorNodeList.item(j);
                        validDonorNames.add(donor.getNodeValue());
                    }
                }

                //rules
                Node rules = XMLHelper.getNodeByName(phaseNodes, resources.getString(RULES));
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
