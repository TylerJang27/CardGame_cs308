package ooga.data.factories;

import ooga.cardtable.*;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.rules.CardAction;
import ooga.data.rules.ICardAction;
import ooga.data.rules.ICellGroup;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This ActionFactory implements Factory and constructs an ICardAction using the createAction() method.
 * These ICardActions govern where cards should move if an IMove is considered valid.
 *
 * @author Tyler Jang
 */
public class ActionFactory implements Factory {
    private static final ResourceBundle RESOURCES = PhaseFactory.RESOURCES;


    private static final String DIRECTION = PhaseFactory.DIRECTION;
    private static final String NUMBER_CARDS = PhaseFactory.NUMBER_CARDS;
    private static final String ACTION = PhaseFactory.ACTION;
    private static final String DESTINATION = PhaseFactory.DESTINATION;
    private static final String SHUFFLE = PhaseFactory.SHUFFLE;
    private static final String OFFSET = PhaseFactory.OFFSET;
    private static final String FLIP = PhaseFactory.FLIP;
    private static final String YES = PhaseFactory.YES;

    private static final String M = PhaseFactory.M;
    private static final String D = PhaseFactory.D;
    private static final String NO = PhaseFactory.NO;

    private static final String TOP = PhaseFactory.TOP;
    private static final String BOTTOM = PhaseFactory.BOTTOM;
    private static final String EXCEPT = PhaseFactory.EXCEPT;
    private static final String PRESERVE = PhaseFactory.PRESERVE;
    private static final String REVERSE = PhaseFactory.REVERSE;

    private static final List<String> TRUE_CHECKS = MasterRuleFactory.TRUE_CHECKS;

    private static final String ALL = MasterRuleFactory.ALL;

    /**
     * Builds and returns an IAction built for an IMasterRule from a rules XML. Requirements for rules XML can be found in ___.
     *
     * @param e            the Element from which IRules are built
     * @param ruleName     the String name of the IMasterRule
     * @param cellGroupMap a Map of String ICellGroup names to ICellGroups
     * @return an ICardAction built for this IMasterRule
     */
    public static ICardAction createAction(Element e, String ruleName, Map<String, ICellGroup> cellGroupMap) {
        Function<IMove, ICell> moverCell = (IMove move) -> move.getMover();
        Function<IMove, ICell> donorCell = (IMove move) -> move.getDonor();
        Function<IMove, ICell> recipientCell = (IMove move) -> move.getRecipient();
        Function<IMove, ICell> currCell = MasterRuleFactory.getCurrentCellFunction(ruleName, moverCell, donorCell, recipientCell);

        List<Consumer<IMove>> actions = new ArrayList<>();
        try {
            Consumer<IMove> cardAction = (IMove move) -> {
                Boolean excepted = extractExceptBehavior(e, currCell, move);
                if (!excepted) {
                    ICell updatedCurrCell = extractCellsToMove(e, currCell, move);
                    ICell destination = extractDestinationBehavior(e, moverCell, donorCell, recipientCell, move, cellGroupMap);
                    IOffset off = extractOffsetBehavior(e, updatedCurrCell);
                    extractRotationBehavior(e, updatedCurrCell);
                    extractFlipBehavior(e, updatedCurrCell);
                    extractShuffleBehavior(e, updatedCurrCell);
                    applyDestinationBehavior(recipientCell, updatedCurrCell, move, destination, off);
                }
            };
            actions.add(cardAction);
        } catch (Exception ex) {
            throw new XMLException(ex, Factory.MISSING_ERROR + "," + RESOURCES.getString(ACTION));
        }
        return new CardAction(actions);
    }

    /**
     * Extracts the ICell that should be moved for a given IAction.
     *
     * @param e        the Element from which IRules are built
     * @param currCell a Function to retrieve the current cell
     * @param move     the IMove from which to extract behavior
     * @return the ICell representing all cells to be moved
     */
    private static ICell extractCellsToMove(Element e, Function<IMove, ICell> currCell, IMove move) {
        String numCards = XMLHelper.getTextValue(e, RESOURCES.getString(NUMBER_CARDS)).toUpperCase();
        if (numCards.equals(RESOURCES.getString(ALL))) {
            return currCell.apply(move);
        } else if (Offset.validOffsets.contains(numCards)) {
            return extractOffsetCells(currCell, move, numCards);
        } else if (numCards.equalsIgnoreCase(RESOURCES.getString(TOP))) {
            return extractTopFromCells(currCell, move);
        } else if (numCards.equalsIgnoreCase(RESOURCES.getString(BOTTOM))) {
            return extractBottomFromCells(currCell, move);
        } else {
            return extractQuantityfromCell(currCell, move, numCards);
        }
    }

    /**
     * Extracts the destination for the ICell being moved.
     *
     * @param e             the Element from which IRules are built
     * @param moverCell     the Function representing the mover from the IMove
     * @param donorCell     the Function representing the donor from the IMove
     * @param recipientCell the Function representing the recipient from the IMove
     * @param move          the IMove being processed
     * @param cellGroupMap  a Map of String ICellGroup names to ICellGroups
     * @return the ICell to which the ICell in question should move
     */
    private static ICell extractDestinationBehavior(Element e, Function<IMove, ICell> moverCell, Function<IMove, ICell> donorCell, Function<IMove, ICell> recipientCell, IMove move, Map<String, ICellGroup> cellGroupMap) {
        String destination = XMLHelper.getTextValue(e, RESOURCES.getString(DESTINATION));
        Map<String, ICell> allMap = PhaseMachineFactory.getAllCells(cellGroupMap);
        if (destination.equals(RESOURCES.getString(M))) {
            return moverCell.apply(move);
        } else if (destination.equals(RESOURCES.getString(D))) {
            return donorCell.apply(move);
        } else if (allMap.containsKey(destination)) {
            return allMap.get(destination).findLeaf(); //note the leaf
        } else {
            return recipientCell.apply(move);
        }
    }

    /**
     * Extracts the ICell to be moved based on an offset condition.
     *
     * @param currCell a Function to retrieve the current cell
     * @param move     the IMove from which to extract behavior
     * @param offset   a String representing the direction of cards to read from
     * @return the ICell representing all cells to be moved
     */
    private static ICell extractOffsetCells(Function<IMove, ICell> currCell, IMove move, String offset) {
        ICell cellToMove;
        cellToMove = (currCell.apply(move).getPeak(Offset.valueOf(offset.toUpperCase())));
        if (currCell.apply(move).getDeck().size() == 0 && currCell.apply(move).getParent() != null) {
            currCell.apply(move).getParent().removeCellAtOffset(currCell.apply(move).getOffsetFromParent());
        }
        return cellToMove;
    }

    /**
     * Extracts the ICell to be moved based on a top condition.
     *
     * @param currCell a Function to retrieve the current cell
     * @param move     the IMove from which to extract behavior
     * @return the ICell representing all cells to be moved
     */
    private static ICell extractTopFromCells(Function<IMove, ICell> currCell, IMove move) {
        ICell cellToMove;
        cellToMove = currCell.apply(move).copy((ICell c) -> {
            if (!c.getDeck().peek().isFixed()) {
                return c.getDeck().getNextCard();
            }
            return null;
        });
        if (currCell.apply(move).getDeck().size() == 0 && currCell.apply(move).getParent() != null) {
            currCell.apply(move).getParent().removeCellAtOffset(currCell.apply(move).getOffsetFromParent());
        }
        return cellToMove;
    }

    /**
     * Extracts the ICell to be moved based on a bottom condition.
     *
     * @param currCell a Function to retrieve the current cell
     * @param move     the IMove from which to extract behavior
     * @return the ICell representing all cells to be moved
     */
    private static ICell extractBottomFromCells(Function<IMove, ICell> currCell, IMove move) {
        ICell cellToMove;
        cellToMove = currCell.apply(move).copy((ICell c) -> {
            if (!c.getDeck().peek().isFixed()) {
                return (c.getDeck().getBottomCard());
            }
            return null;
        });
        if (currCell.apply(move).getDeck().size() == 0 && currCell.apply(move).getParent() != null) {
            currCell.apply(move).getParent().removeCellAtOffset(currCell.apply(move).getOffsetFromParent());
        }
        return cellToMove;
    }

    /**
     * Extracts the ICell to be moved based on a number of cards condition.
     *
     * @param currCell a Function to retrieve the current cell
     * @param move     the IMove from which to extract behavior
     * @param numCards a String representing the number of ICards to move
     * @return the ICell representing all cells to be moved
     */
    private static ICell extractQuantityfromCell(Function<IMove, ICell> currCell, IMove move, String numCards) {
        ICell cellToMove = new Cell("");
        try {
            int cardQuantity = Integer.parseInt(numCards);
            for (int k = 0; k < cardQuantity; k++) {
                cellToMove.addCard(Offset.NONE, currCell.apply(move).findLeaf().getDeck().getNextCard());
            }
            if (currCell.apply(move).getDeck().size() == 0 && currCell.apply(move).getParent() != null) {
                currCell.apply(move).getParent().removeCellAtOffset(currCell.apply(move).getOffsetFromParent());
            }
        } catch (Exception ex) {
            cellToMove = currCell.apply(move);
        }
        return cellToMove;
    }

    /**
     * Extracts and applies the shuffle behavior to be applied to the currCell and its children after moving.
     *
     * @param e        the Element from which IRules are built
     * @param currCell the current ICell after moving
     */
    private static void extractShuffleBehavior(Element e, ICell currCell) {
        String shuffle = XMLHelper.getTextValue(e, RESOURCES.getString(SHUFFLE));
        if (shuffle.equalsIgnoreCase(RESOURCES.getString(REVERSE))) {
            for (Map.Entry<IOffset, ICell> entry : currCell.getAllChildren().entrySet()) {
                entry.getValue().getDeck().reverse();
            }
        } else if (shuffle.equalsIgnoreCase(RESOURCES.getString(YES))) {
            for (Map.Entry<IOffset, ICell> entry : currCell.getAllChildren().entrySet()) {
                entry.getValue().getDeck().shuffle();
            }
        }
    }

    /**
     * Extracts and applies the rotation to be applied to the ICell.
     *
     * @param e        the Element from which IRules are built
     * @param currCell the current ICell being moved
     */
    private static void extractRotationBehavior(Element e, ICell currCell) {
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

    /**
     * Extracts the IOffset for the ICardAction.
     *
     * @param e        the Element from which IRules are built
     * @param currCell the current ICell being moved
     * @return the IOffset to be applied from the destination
     */
    private static IOffset extractOffsetBehavior(Element e, ICell currCell) {
        String offset = XMLHelper.getTextValue(e, RESOURCES.getString(OFFSET));
        IOffset off;
        if (Offset.validOffsets.contains(offset)) {
            off = Offset.valueOf(offset.toUpperCase());
        } else if (offset.equalsIgnoreCase(RESOURCES.getString(PRESERVE))) {
            off = currCell.getOffsetFromParent();
        } else {
            off = Offset.NONE;
        }
        return off;
    }

    /**
     * Extracts and applies the flipping to the ICell.
     *
     * @param e        the Element from which IRules are built
     * @param currCell the current ICell being moved
     */
    private static void extractFlipBehavior(Element e, ICell currCell) {
        String flip = XMLHelper.getTextValue(e, RESOURCES.getString(FLIP));
        if (Offset.validOffsets.contains(flip.toLowerCase()) &&
                currCell.getAllChildren().containsKey(Offset.valueOf(flip.toUpperCase()))) {
            ICard cardToFlip = currCell.getPeak(Offset.valueOf(flip.toUpperCase())).getDeck().peek();
            if (cardToFlip != null && !cardToFlip.isFaceUp()) {
                cardToFlip.flip();
            }
        } else if (flip.equals(RESOURCES.getString(ALL))) {
            for (ICell c : currCell.getAllCells()) {
                for (int k = 0; k < c.getDeck().size(); k++) {
                    ICard cardToFlip = c.getDeck().peekCardAtIndex(k);
                    if (!cardToFlip.isFaceUp()) {
                        cardToFlip.flip();
                    }
                }
            }
        } else if (flip.equals(RESOURCES.getString(NO))) {
            for (ICell c : currCell.getAllCells()) {
                for (int k = 0; k < c.getDeck().size(); k++) {
                    ICard cardToFlip = c.getDeck().peekCardAtIndex(k);
                    if (cardToFlip.isFaceUp() && !cardToFlip.isFixed()) {
                        cardToFlip.flip();
                    }
                }
            }
        }
    }

    /**
     * Extracts whether or not this current ICell should be excepted from the action behavior
     *
     * @param e        the Element from which IRules are built
     * @param currCell a Function to retrieve the current cell
     * @param move     the IMove from which to extract behavior
     * @return a Boolean representing whether or not the ICell should be excepted
     */
    private static Boolean extractExceptBehavior(Element e, Function<IMove, ICell> currCell, IMove move) {
        NodeList excepts = e.getElementsByTagName(RESOURCES.getString(EXCEPT));
        for (int k = 0; k < excepts.getLength(); k++) {
            Node exceptedCell = excepts.item(k);
            if (exceptedCell.getTextContent().equalsIgnoreCase(currCell.apply(move).findHead().getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Applies the move from the currCell to the destination.
     *
     * @param recipientCell the Function representing the recipient from the IMove
     * @param currCell      the current ICell being moved
     * @param move          the IMove being processed
     * @param destination   the destination of the currCell
     * @param off           the IOffset to be applied from the destination
     */
    private static void applyDestinationBehavior(Function<IMove, ICell> recipientCell, ICell currCell, IMove move, ICell destination, IOffset off) {
        if (!destination.findHead().getName().equalsIgnoreCase(currCell.findHead().getName())) {
            IOffset offsetFromParent = null;
            if (currCell.getParent() != null) {
                offsetFromParent = currCell.getOffsetFromParent();
            }
            if (offsetFromParent != null) {
                currCell.getParent().removeCellAtOffset(offsetFromParent); //fixme commented by maverick
            }

            destination.addCell(off, currCell);
        }
    }
}
