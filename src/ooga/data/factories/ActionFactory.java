package ooga.data.factories;

import ooga.cardtable.*;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.rules.CardAction;
import ooga.data.rules.ICardAction;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;

public class ActionFactory implements Factory {
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
    public static final List<String> TRUE_CHECKS = new ArrayList<>(Arrays.asList(new String[]{"", resources.getString(ALL)}));

    public ActionFactory() { documentBuilder = XMLHelper.getDocumentBuilder();}

    public static ICardAction getAction(Element e, String ruleName) {
        Function<IMove, ICell> moverCell = (IMove move) -> move.getMover();
        Function<IMove, ICell> donorCell = (IMove move) -> move.getDonor();
        Function<IMove, ICell> recipientCell = (IMove move) -> move.getRecipient();
        Function<IMove, ICell> currCell = MasterRuleFactory.getCurrentCellFunction(ruleName, moverCell, donorCell, recipientCell);
        String curr = ("" + ruleName.charAt(ruleName.length() - 1));

        List<Consumer<IMove>> actions = new ArrayList<>();
        try {
            Consumer<IMove> cardAction = (IMove move) -> {
                extractCellsToMove(e, currCell, move);

                ICell destination = extractDestinationBehavior(e, moverCell, donorCell, recipientCell, move);

                IOffset off = extractOffsetBehavior(e);

                extractRotationBehavior(e, currCell, move);

                extractFlipBehavior(e, currCell, move);

                //TODO: IMPLEMENT SHUFFLE

                System.out.println("Time to move");

                applyDestinationBehavior(recipientCell, currCell, curr, move, destination, off);
            };
            actions.add(cardAction);
        } catch (Exception ex) {
            throw new XMLException(ex, Factory.MISSING_ERROR + "," + resources.getString(ACTION));
        }
        System.out.println("ACTINO" + actions.size());
        return new CardAction(actions);
    }

    private static void extractCellsToMove(Element e, Function<IMove, ICell> currCell, IMove move) {
        List< ICell > cellsToMove = new ArrayList<>();
        String numCards = XMLHelper.getTextValue(e, resources.getString(NUMBER_CARDS));
        //determines number of cards to move
        if (numCards.equals(resources.getString(ALL))) {
            cellsToMove.addAll(currCell.apply(move).getAllCells());
        } else if (Offset.validOffsets.contains(numCards)) {
            cellsToMove.add(currCell.apply(move).getPeak(Offset.valueOf(numCards.toUpperCase())));
        }
    }

    private static void applyDestinationBehavior(Function<IMove, ICell> recipientCell, Function<IMove, ICell> currCell, String curr, IMove move, ICell destination, IOffset off) {
        System.out.println("destination: fun!" + destination.findHead().getName() + "\n\t" + currCell.apply(move).findHead().getName());
        if (!destination.findHead().getName().equalsIgnoreCase(currCell.apply(move).findHead().getName())) {
            //IOffset offsetFromParent = recipientCell.apply(move).getOffsetFromParent();
            //recipientCell.apply(move).getParent().removeCellAtOffset(offsetFromParent); //fixme commented by maverick
            //destination.addCell(off, currCell.apply(move));
            System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
            System.out.println("is this null: " + currCell.apply(move));
            IOffset offsetFromParent = null;
            if (currCell.apply(move).getParent() != null) {
                offsetFromParent = currCell.apply(move).getOffsetFromParent();
            }
            //TODO: WRITING BAD CODE
            System.out.println("hello");


            ICell currParent = currCell.apply(move).getParent();
            System.out.println("current cell parent: "+currParent);
            if (offsetFromParent != null) {
                currCell.apply(move).getParent().removeCellAtOffset(offsetFromParent); //fixme commented by maverick
            }
            System.out.println("current cell: "+currParent);
            recipientCell.apply(move).addCell(off, currCell.apply(move));
            System.out.println(destination.getName());
        }
    }

    private static ICell extractDestinationBehavior(Element e, Function<IMove, ICell> moverCell, Function<IMove, ICell> donorCell, Function<IMove, ICell> recipientCell, IMove move) {
        String destination = XMLHelper.getTextValue(e, resources.getString(DESTINATION));
        ICell dest;
        if (destination.equals(resources.getString(M))) {
            dest = moverCell.apply(move);
        } else if (destination.equals(resources.getString(D))) {
            dest = donorCell.apply(move);
        } else {
            dest = recipientCell.apply(move);
        }
        return dest;
    }

    private static IOffset extractOffsetBehavior(Element e) {
        String offset = XMLHelper.getTextValue(e, resources.getString(OFFSET));
        IOffset off;
        if (Offset.validOffsets.contains(offset)) {
            off = Offset.valueOf(offset.toUpperCase());
        } else {
            off = Offset.NONE;
        }
        return off;
    }

    private static void extractRotationBehavior(Element e, Function<IMove, ICell> currCell, IMove move) {
        String turn = XMLHelper.getTextValue(e, resources.getString(DIRECTION));
        if (!TRUE_CHECKS.contains(turn)) {
            Double angle = Double.parseDouble(turn);
            for (ICell c : currCell.apply(move).getAllCells()) {
                for (int k = 0; k < c.getDeck().size(); k++) {
                    c.getDeck().peekCardAtIndex(k).rotate(angle);
                }
            }
        }
    }

    private static void extractFlipBehavior(Element e, Function<IMove, ICell> currCell, IMove move) {
        System.out.println("nodeheaderflippy" + e.getNodeName() + ":" + e.getTextContent());
        String flip = XMLHelper.getTextValue(e, resources.getString(FLIP));
        System.out.println("flippy: " + flip);
        if (Offset.validOffsets.contains(flip.toLowerCase())) {
            System.out.println("flippy offset");
            System.out.println("my flippy cell: " + currCell.apply(move).getPeak(Offset.valueOf(flip.toUpperCase())).getDeck().peek().getName());
            currCell.apply(move).getPeak(Offset.valueOf(flip.toUpperCase())).getDeck().peek().flip();
        } else if (flip.equals(resources.getString(ALL))) {
            System.out.println("flippy all");
            for (ICell c : currCell.apply(move).getAllCells()) {
                for (int k = 0; k < c.getDeck().size(); k++) {
                    c.getDeck().peekCardAtIndex(k).flip();
                }
            }
        }
        System.out.println("sad flippy");
    }


}
