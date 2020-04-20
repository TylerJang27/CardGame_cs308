package ooga.data.factories;

import ooga.cardtable.*;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.rules.CardAction;
import ooga.data.rules.ICardAction;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class ActionFactory implements Factory {
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
    //private static final String ALL = PhaseFactory.ALL;
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

    private static final String TOP = PhaseFactory.TOP;
    private static final String BOTTOM = PhaseFactory.BOTTOM;
    private static final String EXCEPT = PhaseFactory.EXCEPT;
    private static final String PRESERVE = PhaseFactory.PRESERVE;
    private static final String REVERSE = PhaseFactory.REVERSE;

    private static final List<String> TRUE_CHECKS = MasterRuleFactory.TRUE_CHECKS;

    private static final String ALL = MasterRuleFactory.ALL;

    public static ICardAction getAction(Element e, String ruleName) {
        Function<IMove, ICell> moverCell = (IMove move) -> move.getMover();
        Function<IMove, ICell> donorCell = (IMove move) -> move.getDonor();
        Function<IMove, ICell> recipientCell = (IMove move) -> move.getRecipient();
        Function<IMove, ICell> currCell = MasterRuleFactory.getCurrentCellFunction(ruleName, moverCell, donorCell, recipientCell);
        String curr = ("" + ruleName.charAt(ruleName.length() - 1));

        List<Consumer<IMove>> actions = new ArrayList<>();
        try {
            Consumer<IMove> cardAction = (IMove move) -> {
                Boolean excepted = extractExceptBehavior(e, currCell, move);
                if (!excepted) {
                    ICell updatedCurrCell = extractCellsToMove(e, currCell, move); ///TODO: DOES THIS DO ANYTHING????

                    ICell destination = extractDestinationBehavior(e, moverCell, donorCell, recipientCell, move);

                    IOffset off = extractOffsetBehavior(e, updatedCurrCell, move);

                    extractRotationBehavior(e, updatedCurrCell, move);

                    extractFlipBehavior(e, updatedCurrCell, move);

                    extractShuffleBehavior(e, updatedCurrCell);

                    //TODO: IMPLEMENT SHUFFLE

                    //System.out.println("Time to move");
                    //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
                    //System.out.println("d: " + move.getDonor().getTotalSize() + "|m: " + move.getMover().getTotalSize() + "|r: " + move.getRecipient().getTotalSize());


                    //applyDestinationBehavior(recipientCell, currCell, curr, move, destination, off);
                    applyDestinationBehavior(recipientCell, updatedCurrCell, curr, move, destination, off);
                }
            };
            actions.add(cardAction);
        } catch (Exception ex) {
            throw new XMLException(ex, Factory.MISSING_ERROR + "," + RESOURCES.getString(ACTION));
        }
        //System.out.println("ACTINO" + actions.size());
        return new CardAction(actions);
    }

    private static Boolean extractExceptBehavior(Element e, Function<IMove, ICell> currCell, IMove move) {
        NodeList excepts = e.getElementsByTagName(RESOURCES.getString(EXCEPT));
        for (int k = 0; k < excepts.getLength(); k ++) {
            Node exceptedCell = excepts.item(k);
            if (exceptedCell.getTextContent().equalsIgnoreCase(currCell.apply(move).findHead().getName())) {
                //System.out.println("EXCEPTED!");
                return true;
            }
        }
        //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
        //System.out.println("\tcurr: " + currCell.apply(move).getName());
        //System.out.println("NOT EXCEPTED!");
        return false;
    }

    private static ICell extractCellsToMove(Element e, Function<IMove, ICell> currCell, IMove move) {
        ICell cellToMove = currCell.apply(move);
        String numCards = XMLHelper.getTextValue(e, RESOURCES.getString(NUMBER_CARDS)).toUpperCase();
        //determines number of cards to move
        if (numCards.equals(RESOURCES.getString(ALL))) {
            //cellsToMove.addAll(currCell.apply(move).getAllCells());
            cellToMove = (currCell.apply(move));
        } else if (Offset.validOffsets.contains(numCards)) {
            cellToMove = (currCell.apply(move).getPeak(Offset.valueOf(numCards.toUpperCase())));
            if (currCell.apply(move).getDeck().size() == 0 && currCell.apply(move).getParent() != null) {
                currCell.apply(move).getParent().removeCellAtOffset(currCell.apply(move).getOffsetFromParent());
            }
        } else if (numCards.equalsIgnoreCase(RESOURCES.getString(TOP))) {
            cellToMove = currCell.apply(move).copy((ICell c) -> {
                if (!c.getDeck().peek().isFixed()) {
                    return c.getDeck().getNextCard();
                }
                return null;
            });
            if (currCell.apply(move).getDeck().size() == 0 && currCell.apply(move).getParent() != null) {
                currCell.apply(move).getParent().removeCellAtOffset(currCell.apply(move).getOffsetFromParent());
            }
        } else if (numCards.equalsIgnoreCase(RESOURCES.getString(BOTTOM))) {
            cellToMove = currCell.apply(move).copy((ICell c) -> {
                if (!c.getDeck().peek().isFixed()) {
                    return (c.getDeck().getBottomCard());
                }
                return null;
            });
            if (currCell.apply(move).getDeck().size() == 0 && currCell.apply(move).getParent() != null) {
                currCell.apply(move).getParent().removeCellAtOffset(currCell.apply(move).getOffsetFromParent());
            }
        } else {
            try {
                int cardQuantity = Integer.parseInt(numCards);
                cellToMove = new Cell("");
                for (int k = 0; k < cardQuantity; k ++) {
                    cellToMove.addCard(Offset.NONE, currCell.apply(move).findLeaf().getDeck().getNextCard());
                }
                if (currCell.apply(move).getDeck().size() == 0 && currCell.apply(move).getParent() != null) {
                    currCell.apply(move).getParent().removeCellAtOffset(currCell.apply(move).getOffsetFromParent());
                }
            } catch (Exception ex) {

            }
        }
        return cellToMove;
    }

    private static void applyDestinationBehavior(Function<IMove, ICell> recipientCell, ICell currCell, String curr, IMove move, ICell destination, IOffset off) {
        //System.out.println("destination: fun!" + destination.findHead().getName() + "\n\t" + currCell.apply(move).findHead().getName());
        if (!destination.findHead().getName().equalsIgnoreCase(currCell.findHead().getName())) {
            //IOffset offsetFromParent = recipientCell.apply(move).getOffsetFromParent();
            //recipientCell.apply(move).getParent().removeCellAtOffset(offsetFromParent); //fixme commented by maverick
            //destination.addCell(off, currCell.apply(move));
            //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
            //System.out.println("is this null: " + currCell);
            IOffset offsetFromParent = null;
            if (currCell.getParent() != null) {
                offsetFromParent = currCell.getOffsetFromParent();
            }
            //TODO: WRITING BAD CODE
            //System.out.println("hello");


            //System.out.println("current cell parent: "+currParent);
            if (offsetFromParent != null) {
                currCell.getParent().removeCellAtOffset(offsetFromParent); //fixme commented by maverick
            }
            //System.out.println("current cell: "+currParent);
            recipientCell.apply(move).addCell(off, currCell);
            //System.out.println(destination.getName());
        }
    }

    private static ICell extractDestinationBehavior(Element e, Function<IMove, ICell> moverCell, Function<IMove, ICell> donorCell, Function<IMove, ICell> recipientCell, IMove move) {
        String destination = XMLHelper.getTextValue(e, RESOURCES.getString(DESTINATION));
        ICell dest;
        if (destination.equals(RESOURCES.getString(M))) {
            dest = moverCell.apply(move);
        } else if (destination.equals(RESOURCES.getString(D))) {
            dest = donorCell.apply(move);
        } else {
            dest = recipientCell.apply(move);
        }
        return dest;
    }

    private static IOffset extractOffsetBehavior(Element e, ICell currCell, IMove move) {
        String offset = XMLHelper.getTextValue(e, RESOURCES.getString(OFFSET));
        IOffset off;
        if (Offset.validOffsets.contains(offset)) {
            off = Offset.valueOf(offset.toUpperCase());
        } else if (offset.equalsIgnoreCase(RESOURCES.getString(PRESERVE))) {
            off = currCell.getOffsetFromParent();
        } else {
            off = Offset.NONE;
        }
        //System.out.println(off.getOffset() + "is my offset!"); //TODO: DEBUG OFFSET
        return off;
    }

    private static void extractRotationBehavior(Element e, ICell currCell, IMove move) {
        String turn = XMLHelper.getTextValue(e, RESOURCES.getString(DIRECTION));
        if (!TRUE_CHECKS.contains(turn)) {
            Double angle = Double.parseDouble(turn);
            for (ICell c : currCell.getAllCells()) {
                for (int k = 0; k < c.getDeck().size(); k++) {
                    c.getDeck().peekCardAtIndex(k).rotate(angle);
                }
            }
        }
    }

    private static void extractFlipBehavior(Element e, ICell currCell, IMove move) {
        //System.out.println("nodeheaderflippy" + e.getNodeName() + ":" + e.getTextContent());
        //System.out.println("d: " + move.getDonor().getName() + "|m: " + move.getMover().getName() + "|r: " + move.getRecipient().getName());
        //System.out.println("\tcurr: " + currCell.getName());
        String flip = XMLHelper.getTextValue(e, RESOURCES.getString(FLIP));
        //System.out.println("flippy: " + flip);
        if (Offset.validOffsets.contains(flip.toLowerCase()) && currCell.getAllChildren().containsKey(Offset.valueOf(flip.toUpperCase()))) {
            //System.out.println("my flippy cell: " + currCell.getPeak(Offset.valueOf(flip.toUpperCase())).getDeck().peek().getName());
            ICard cardToFlip = currCell.getPeak(Offset.valueOf(flip.toUpperCase())).getDeck().peek();
            if (cardToFlip != null && !cardToFlip.isFaceUp()) {
                cardToFlip.flip();
            }
        } else if (flip.equals(RESOURCES.getString(ALL))) {
            //System.out.println("flippy all");
            for (ICell c : currCell.getAllCells()) {
                for (int k = 0; k < c.getDeck().size(); k++) {
                    ICard cardToFlip = c.getDeck().peekCardAtIndex(k);
                    if (!cardToFlip.isFaceUp()) {
                        cardToFlip.flip();
                    }
                }
            }
        } else if (flip.equals(RESOURCES.getString(NO))) {
            //System.out.println("flippy DOWN");
            for (ICell c : currCell.getAllCells()) {
                for (int k = 0; k < c.getDeck().size(); k++) {
                    ICard cardToFlip = c.getDeck().peekCardAtIndex(k);
                    if (cardToFlip.isFaceUp()) {
                        cardToFlip.flip();
                    }
                }
            }
        }
        //System.out.println("sad flippy");
    }

    private static void extractShuffleBehavior(Element e, ICell currCell) {
        String shuffle = XMLHelper.getTextValue(e, RESOURCES.getString(SHUFFLE));
        if (shuffle.equalsIgnoreCase(RESOURCES.getString(REVERSE))) {
            for (Map.Entry<IOffset, ICell> entry: currCell.getAllChildren().entrySet()) {
                entry.getValue().getDeck().reverse();
            }
        }
    }

}
