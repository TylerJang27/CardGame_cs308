package ooga.data.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;
import ooga.cardtable.Cell;
import ooga.cardtable.Deck;
import ooga.cardtable.ICard;
import ooga.cardtable.ICell;
import ooga.cardtable.IDeck;
import ooga.cardtable.IMove;
import ooga.cardtable.IOffset;
import ooga.cardtable.Offset;
import ooga.data.XMLException;
import ooga.data.XMLHelper;
import ooga.data.rules.CardAction;
import ooga.data.rules.ICardAction;
import ooga.data.rules.ICellGroup;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This ActionFactory implements Factory and constructs an ICardAction using the createAction()
 * method. These ICardActions govern where cards should move if an IMove is considered valid.
 *
 * @author Tyler Jang
 */
public class ActionFactory implements Factory {

  private static final ResourceBundle RESOURCES = PhaseFactory.RESOURCES;

  private static final String ROTATION = PhaseFactory.ROTATION;
  private static final String NUMBER_CARDS = PhaseFactory.NUMBER_CARDS;
  private static final String ACTION = PhaseFactory.ACTION;
  private static final String DESTINATION = PhaseFactory.DESTINATION;
  private static final String SHUFFLE = PhaseFactory.SHUFFLE;
  private static final String OFFSET = PhaseFactory.OFFSET;
  private static final String FLIP = PhaseFactory.FLIP;
  private static final String YES = PhaseFactory.YES;
  private static final String RANDOM = PhaseFactory.RANDOM;
  private static final String COLLAPSE = PhaseFactory.COLLAPSE;

  private static final String M = PhaseFactory.M;
  private static final String D = PhaseFactory.D;
  private static final String NO = PhaseFactory.NO;

  private static final String HEAD = PhaseFactory.HEAD;
  private static final String TOP = PhaseFactory.TOP;
  private static final String BOTTOM = PhaseFactory.BOTTOM;
  private static final String EXCEPT = PhaseFactory.EXCEPT;
  private static final String PRESERVE = PhaseFactory.PRESERVE;
  private static final String REVERSE = PhaseFactory.REVERSE;
  private static final String D_ROOT = PhaseFactory.D_ROOT;

  private static final List<String> TRUE_CHECKS = MasterRuleFactory.TRUE_CHECKS;

  private static final String ALL = MasterRuleFactory.ALL;

  private ActionFactory() {
  }

  /**
   * Builds and returns an IAction built for an IMasterRule from a rules XML. Requirements for rules
   * XML can be found in doc/XML_Documentation.md.
   *
   * @param e            the Element from which IRules are built
   * @param ruleName     the String name of the IMasterRule
   * @param cellGroupMap a Map of String ICellGroup names to ICellGroups
   * @return an ICardAction built for this IMasterRule
   */
  public static ICardAction createAction(Element e, String ruleName,
      Map<String, ICellGroup> cellGroupMap) {
    Function<IMove, ICell> moverCell = (IMove move) -> move.getMover();
    Function<IMove, ICell> donorCell = (IMove move) -> move.getDonor();
    Function<IMove, ICell> recipientCell = (IMove move) -> move.getRecipient();
    Function<IMove, ICell> currCell = MasterRuleFactory
        .getCurrentCellFunction(ruleName, moverCell, donorCell, recipientCell);

    List<Consumer<IMove>> actions = new ArrayList<>();
    try {
      Consumer<IMove> cardAction = (IMove move) -> {
        Boolean excepted = extractExceptBehavior(e, currCell.apply(move));
        if (!excepted) {
          ICell updatedCurrCell = extractCellsToMove(e, currCell.apply(move));
          extractCollapse(e, updatedCurrCell);
          ICell destination = extractDestinationBehavior(e, moverCell.apply(move),
              donorCell.apply(move), recipientCell.apply(move), cellGroupMap);
          IOffset off = extractOffsetBehavior(e, updatedCurrCell);
          extractRotationBehavior(e, updatedCurrCell);
          extractFlipBehavior(e, updatedCurrCell);
          applyDestinationBehavior(updatedCurrCell, destination, off);
          extractShuffleBehavior(e, updatedCurrCell);
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
   * @param e    the Element from which IRules are built
   * @param curr the current cell
   * @return the ICell representing all cells to be moved
   */
  private static ICell extractCellsToMove(Element e, ICell curr) {
    String numCards = XMLHelper.getTextValue(e, RESOURCES.getString(NUMBER_CARDS)).toUpperCase();
    if (numCards.equals(RESOURCES.getString(ALL))) {
      return extractAllCells(curr);
    } else if (numCards.equalsIgnoreCase(RESOURCES.getString(HEAD))) {
      ICell head = curr.findHead();
      if (head.getDeck().size() == 0 || head.getDeck().peekBottom().isFixed()) {
        return extractAllCells(findTheChild(head));
      }
      return extractAllCells(head);
    } else if (numCards.equalsIgnoreCase(RESOURCES.getString(D_ROOT))) {
      return extractRootDeck(curr);
    } else if (Offset.validOffsets.contains(numCards)) {
      return extractOffsetCells(curr, numCards);
    } else if (numCards.equalsIgnoreCase(RESOURCES.getString(TOP))) {
      return extractTopFromCells(curr);
    } else if (numCards.equalsIgnoreCase(RESOURCES.getString(BOTTOM))) {
      return extractBottomFromCells(curr);
    } else if (numCards.equalsIgnoreCase(RESOURCES.getString(RANDOM))) {
      return extractRandomFromCells(curr);
    } else {
      return extractQuantityfromCell(curr, numCards);
    }
  }

  /**
   * Extracts the ICell to be moved based on an all condition.
   *
   * @param currCell the current cell
   * @return the ICell representing all cells to be moved
   */
  private static ICell extractAllCells(ICell currCell) {
    return currCell.extractDecks((ICell c) -> {
      List<ICard> cardList = new ArrayList<>();
      for (int k = c.getDeck().size() - 1; k >= 0; k--) {
        if (!c.getDeck().peekCardAtIndex(k).isFixed()) {
          cardList.add(c.getDeck().getCardAtIndex(k));
        }
      }
      removeEmptyCells(currCell);
      return new Deck("", cardList);
    });
  }

  /**
   * Extracts the ICell to be moved based on a deck root condition.
   *
   * @param currCell the current cell
   * @return the ICell representing all cells to be moved
   */
  private static ICell extractRootDeck(ICell currCell) {
    ICell c = new Cell("");
    IDeck deck = currCell.getDeck();
    for (int k = deck.size() - 1; k >= 0; k--) {
      if (!deck.peekCardAtIndex(k).isFixed()) {
        c.addCard(Offset.NONE, deck.getCardAtIndex(k));
      }
    }
    removeEmptyCells(currCell, true);
    return c;
  }

  /**
   * Extracts the destination for the ICell being moved.
   *
   * @param e             the Element from which IRules are built
   * @param moverCell     the mover from the IMove
   * @param donorCell     the donor from the IMove
   * @param recipientCell the recipient from the IMove
   * @param cellGroupMap  a Map of String ICellGroup names to ICellGroups
   * @return the ICell to which the ICell in question should move
   */
  private static ICell extractDestinationBehavior(Element e, ICell moverCell, ICell donorCell,
      ICell recipientCell, Map<String, ICellGroup> cellGroupMap) {
    String destination = XMLHelper.getTextValue(e, RESOURCES.getString(DESTINATION));
    Map<String, ICell> allMap = PhaseMachineFactory.getAllCells(cellGroupMap);
    if (destination.equals(RESOURCES.getString(M))) {
      return moverCell;
    } else if (destination.equals(RESOURCES.getString(D))) {
      return donorCell;
    } else if (allMap.containsKey(destination)) {
      return allMap.get(destination).findLeaf(); //note the leaf
    } else {
      return recipientCell;
    }
  }

  /**
   * Extracts the ICell to be moved based on an offset condition.
   *
   * @param currCell the current cell
   * @param offset   a String representing the direction of cards to read from
   * @return the ICell representing all cells to be moved
   */
  private static ICell extractOffsetCells(ICell currCell, String offset) {
    ICell cellToMove;
    cellToMove = (currCell.getPeak(Offset.valueOf(offset.toUpperCase())));
    removeEmptyCells(currCell);
    return cellToMove;
  }

  /**
   * Extracts the ICell to be moved based on a top condition.
   *
   * @param currCell a the current cell
   * @return the ICell representing all cells to be moved
   */
  private static ICell extractTopFromCells(ICell currCell) {
    ICell cellToMove;
    cellToMove = currCell.extractCards((ICell c) -> {
      if (!c.getDeck().peek().isFixed()) {
        ICard card = c.getDeck().getNextCard();
        return card;
      }
      return null;
    });
    removeEmptyCells(currCell);
    return cellToMove;
  }

  /**
   * Extracts the ICell to be moved based on a bottom condition.
   *
   * @param currCell the current cell
   * @return the ICell representing all cells to be moved
   */
  private static ICell extractBottomFromCells(ICell currCell) {
    ICell cellToMove;
    cellToMove = currCell.extractCards((ICell c) -> {
      if (!c.getDeck().peek().isFixed()) {
        return (c.getDeck().getBottomCard());
      }
      return null;
    });
    removeEmptyCells(currCell);
    return cellToMove;
  }

  /**
   * Extracts the ICell to be moved based on a random condition.
   *
   * @param currCell the current cell
   * @return the ICell representing all cells to be moved
   */
  private static ICell extractRandomFromCells(ICell currCell) {
    ICell cellToMove;
    cellToMove = currCell.extractCards((ICell c) -> {
      if (!c.getDeck().peek().isFixed()) {
        ICard card = c.getDeck().getRandomCard();
        return card;
      }
      return null;
    });
    removeEmptyCells(currCell);
    return cellToMove;
  }

  /**
   * Extracts the ICell to be moved based on a number of cards condition.
   *
   * @param currCell the current cell
   * @param numCards a String representing the number of ICards to move
   * @return the ICell representing all cells to be moved
   */
  private static ICell extractQuantityfromCell(ICell currCell, String numCards) {
    ICell cellToMove = new Cell("");
    try {
      int cardQuantity = Integer.parseInt(numCards);
      for (int k = 0; k < cardQuantity; k++) {
        cellToMove.addCard(Offset.NONE, currCell.findLeaf().getDeck().getNextCard());
      }
      removeEmptyCells(currCell);
    } catch (Exception ex) {
      cellToMove = currCell;
    }
    return cellToMove;
  }

  /**
   * Extracts and applies the collapsing for the ICell being moved.
   *
   * @param e        the Element from which IRules are built
   * @param currCell the current cell
   */
  private static void extractCollapse(Element e, ICell currCell) {
    String collapse = XMLHelper.getTextValue(e, RESOURCES.getString(COLLAPSE));
    if (collapse.equalsIgnoreCase(RESOURCES.getString(YES))) {
      while (currCell.getAllChildren().size() > 1) {
        for (Map.Entry<IOffset, ICell> entry : currCell.getAllChildren().entrySet()) {
          if (!entry.getKey().equals(Offset.NONE)) {
            currCell.removeCellAtOffset(entry.getKey());
            currCell.addCell(Offset.NONE, entry.getValue());
          }
        }
      }
    }
  }

  /**
   * A helper method for extraction that removes the cell from its parent if it's empty, and if it's
   * empty and has one child sets the child in place of itself.
   *
   * @param curr           the current ICell implementation
   * @param attachTheChild whether or not the child should be bound to the parent
   */
  private static void removeEmptyCells(ICell curr, boolean attachTheChild) {
    if (curr.getDeck().size() == 0 && curr.getParent() != null) {
      ICell parent = curr.getParent();
      IOffset off = curr.getOffsetFromParent();
      parent.removeCellAtOffset(off);
      if (attachTheChild && curr.getAllChildren().size() == 2) {
        ICell child = findTheChild(curr);
        parent.addCell(off, child);
        //parent.setCellAtOffset(curr.getOffsetFromParent(), child);
      }
    }
  }

  /**
   * A helper method for extraction that removes the cell from its parent if it's empty, and if it's
   * empty and has one child sets the child in place of itself.
   *
   * @param curr the current ICell implementation
   */
  private static void removeEmptyCells(ICell curr) {
    removeEmptyCells(curr, false);
  }

  /**
   * Extracts the child cell from an empty offset cell
   *
   * @param curr the cell to extract from
   * @return the ICell representing the cell
   */
  private static ICell findTheChild(ICell curr) {
    ICell child = new Cell("");
    for (Map.Entry<IOffset, ICell> children : curr.getAllChildren().entrySet()) {
      if (!children.getKey().equals(Offset.NONE)) {
        child = children.getValue();
      }
    }
    return child;
  }

  /**
   * Extracts and applies the shuffle behavior to be applied to the currCell and its children after
   * moving.
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
    String turn = XMLHelper.getTextValue(e, RESOURCES.getString(ROTATION));
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
    if (Offset.validOffsets.contains(flip.toLowerCase()) && currCell.getAllChildren()
        .containsKey(Offset.valueOf(flip.toUpperCase()))) {
      ICard cardToFlip = currCell.getPeak(Offset.valueOf(flip.toUpperCase())).getDeck().peek();
      checkAndFlipUp(cardToFlip);
    } else if (flip.equals(RESOURCES.getString(YES))) {
      for (ICell c : currCell.getAllCells()) {
        applyToDeck(c, (ICard card) -> checkAndFlipUp(card));
      }
    } else if (flip.equals(RESOURCES.getString(ALL))) {
      for (ICell c : currCell.findHead().getAllCells()) {
        applyToDeck(c, (ICard card) -> checkAndFlipUp(card));
      }
    } else if (flip.equals(RESOURCES.getString(NO))) {
      for (ICell c : currCell.getAllCells()) {
        applyToDeck(c, (ICard card) -> checkAndFlipDown(card));
      }
    }
  }

  /**
   * Applies a behavior to an entire deck based on a Consumer.
   *
   * @param c            the cell whose deck to loop through
   * @param cardConsumer the Consumer to apply for each card
   */
  private static void applyToDeck(ICell c, Consumer<ICard> cardConsumer) {
    for (int k = 0; k < c.getDeck().size(); k++) {
      ICard card = c.getDeck().peekCardAtIndex(k);
      cardConsumer.accept(card);
    }
  }

  /**
   * Checks that a card is valid and not fixed and flips it up.
   *
   * @param cardToFlip the card to flip up
   */
  private static void checkAndFlipDown(ICard cardToFlip) {
    if (cardToFlip.isFaceUp() && !cardToFlip.isFixed()) {
      cardToFlip.flip();
    }
  }

  /**
   * Checks that a card is valid and not fixed and flips it down.
   *
   * @param cardToFlip the card to flip down
   */
  private static void checkAndFlipUp(ICard cardToFlip) {
    if (cardToFlip != null && !cardToFlip.isFaceUp() && !cardToFlip.isFixed()) {
      cardToFlip.flip();
    }
  }

  /**
   * Extracts whether or not this current ICell should be excepted from the action behavior
   *
   * @param e        the Element from which IRules are built
   * @param currCell the current cell
   * @return a Boolean representing whether or not the ICell should be excepted
   */
  private static Boolean extractExceptBehavior(Element e, ICell currCell) {
    NodeList excepts = e.getElementsByTagName(RESOURCES.getString(EXCEPT));
    for (int k = 0; k < excepts.getLength(); k++) {
      Node exceptedCell = excepts.item(k);
      if (exceptedCell.getTextContent().equalsIgnoreCase(currCell.findHead().getName())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Applies the move from the currCell to the destination.
   *
   * @param currCell    the current ICell being moved
   * @param destination the destination of the currCell
   * @param off         the IOffset to be applied from the destination
   */
  private static void applyDestinationBehavior(ICell currCell, ICell destination, IOffset off) {
    if (!destination.findHead().getName().equalsIgnoreCase(currCell.findHead().getName())) {
      IOffset offsetFromParent = null;
      if (currCell.getParent() != null) {
        offsetFromParent = currCell.getOffsetFromParent();
      }
      if (offsetFromParent != null) {
        currCell.getParent().removeCellAtOffset(offsetFromParent);
      }
      destination.addCell(off, currCell);
    }
  }
}
