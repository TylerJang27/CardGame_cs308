package ooga.data.factories.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;
import ooga.cardtable.Card;
import ooga.cardtable.Cell;
import ooga.cardtable.Color;
import ooga.cardtable.Deck;
import ooga.cardtable.ICard;
import ooga.cardtable.ICell;
import ooga.cardtable.IDeck;
import ooga.cardtable.ISuit;
import ooga.cardtable.Move;
import ooga.cardtable.Suit;
import ooga.cardtable.Value;
import ooga.data.XMLHelper;
import ooga.data.factories.MasterRuleFactory;
import ooga.data.rules.CellGroup;
import ooga.data.rules.ICellGroup;
import ooga.data.rules.IMasterRule;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Tests the functionality of MasterRules with respect to their ability to validate different moves
 * after construction.
 *
 * @author Tyler Jang
 */
class MasterRuleFactoryTest {

  private static final String TEST_DIRECTORY = "test/ooga/data/factories/rules/";

  private Node getRulesNode(Element root) {
    return XMLHelper.getNodeByName(root.getChildNodes(), "rules");
  }

  /**
   * Example test for MasterRule Factory, testing a number of moves and their validity against basic
   * rule sets.
   */
  @Test
  void testCreateMasterRules() {
    ISuit heart = new Suit("heart", new Color("red"));
    ISuit diamond = new Suit("diamond", new Color("red"));
    ISuit spade = new Suit("spade", new Color("black"));
    ISuit club = new Suit("club", new Color("black"));

    IDeck deckA = new Deck("a", List.of(new Card("0H", heart, new Value("0H", 10)),
        new Card("9H", heart, new Value("9H", 9)),
        new Card("8H", heart, new Value("8H", 8))));
    IDeck deckB = new Deck("b", List.of(new Card("0D", diamond, new Value("0D", 10)),
        new Card("9C", club, new Value("9C", 9)),
        new Card("8D", diamond, new Value("8D", 8)),
        new Card("7S", spade, new Value("7S", 7))));
    IDeck deckC = new Deck("c", List.of(
        new Card("7S", spade, new Value("7S", 7)),
        new Card("8D", diamond, new Value("8D", 8)),
        new Card("9C", club, new Value("9C", 9)),
        new Card("0D", diamond, new Value("0D", 10))));

    IDeck deckHearts = new Deck("a", List.of(
        new Card("AH", heart, new Value("AH", 1)),
        new Card("2H", heart, new Value("2H", 2)),
        new Card("3H", heart, new Value("3H", 3)),
        new Card("4H", heart, new Value("4H", 4)),
        new Card("5H", heart, new Value("5H", 5)),
        new Card("6H", heart, new Value("6H", 6)),
        new Card("7H", heart, new Value("8H", 7)),
        new Card("8H", heart, new Value("8H", 8)),
        new Card("9H", heart, new Value("9H", 9)),
        new Card("0H", heart, new Value("0H", 10))));

    IDeck deckSpades = new Deck("a", List.of(
        new Card("AS", spade, new Value("AS", 1)),
        new Card("2S", spade, new Value("2S", 2)),
        new Card("3S", spade, new Value("3S", 3)),
        new Card("4S", spade, new Value("4S", 4)),
        new Card("5S", spade, new Value("5S", 5)),
        new Card("6S", spade, new Value("6S", 6)),
        new Card("7S", spade, new Value("8S", 7)),
        new Card("8S", spade, new Value("8S", 8)),
        new Card("9S", spade, new Value("9S", 9)),
        new Card("0S", spade, new Value("0S", 10))));
    IDeck[] decks = {deckA, deckB, deckC, deckHearts, deckSpades};
    for (int k = 0; k < decks.length; k++) {
      for (int j = 0; j < decks[k].size(); j++) {
        ICard c = decks[k].peekCardAtIndex(j);
        if (!c.isFaceUp()) {
          c.flip();
        }
      }
    }

    ICell cellA = new Cell("a", deckA.copy());
    ICell cellB = new Cell("b", deckB.copy());
    ICell cellC = new Cell("c", deckC.copy());

    ICell cell0H = new Cell("d", new Deck("d", List.of(deckA.peekCardAtIndex(0).copy()))); //0H
    ICell cell9H = new Cell("e", new Deck("e", List.of(deckA.peekCardAtIndex(1).copy()))); //9H
    ICell cell8H = new Cell("f", new Deck("f", List.of(deckA.peekCardAtIndex(2).copy()))); //8H

    ICell cell7S = new Cell("g", new Deck("g", List.of(deckC.peekCardAtIndex(0).copy()))); //7S
    ICell cell8D = new Cell("h", new Deck("h", List.of(deckC.peekCardAtIndex(0).copy()))); //8D
    ICell cell9C = new Cell("i", new Deck("i", List.of(deckC.peekCardAtIndex(0).copy()))); //9C
    ICell cell9S = new Cell("j", new Deck("j", List.of(deckSpades.peekCardAtIndex(8).copy()))); //9S
    ICell cell0D = new Cell("k", new Deck("k", List.of(deckC.peekCardAtIndex(0).copy()))); //0D

    //basic1
    //move 8 onto 10, 7 onto 9

    Map<String, ICell> cellMap1 = Map
        .of("a", cellA, "b", cellB, "c", cellC, "d", cell0H, "j", cell9S);
    Map<String, ICellGroup> cellGroupMap1 = Map.of("action", new CellGroup("action", cellMap1));

    Map<String, ICell> cellMap2 = Map.of("d", cell0H, "e", cell9H, "f", cell8H);
    Map<String, ICellGroup> cellGroupMap2 = Map.of("play", new CellGroup("play", cellMap2));

    Element root1 = XMLHelper
        .getRootAndCheck(new File(TEST_DIRECTORY + "rules_basic1.xml"), "rule", "invalid file");
    Node node1 = getRulesNode(root1);
    List<IMasterRule> masterRuleList1 = MasterRuleFactory
        .createMasterRules(node1, cellGroupMap1, cellMap1, "first");
    IMasterRule masterRule1 = masterRuleList1.get(0);

    assertTrue(masterRule1.checkValidMove(new Move(cellA.copy(), cell8H.copy(), cell0H.copy())));
    assertTrue(masterRule1.checkValidMove(new Move(cellB.copy(), cell7S.copy(), cell9S.copy())));
    assertFalse(masterRule1.checkValidMove(new Move(cellA.copy(), cell0H.copy(), cell8H.copy())));
    assertFalse(masterRule1.checkValidMove(new Move(cellB.copy(), cell0H.copy(), cell9S.copy())));
    assertFalse(masterRule1.checkValidMove(new Move(cellA.copy(), cellA.copy(), cellA.copy())));
    assertFalse(masterRule1.checkValidMove(new Move(cellB.copy(), cellB.copy(), cellB.copy())));
    ICell cell8H1 = cell8H.copy();
    ICell cell0H1 = cell0H.copy();
    masterRule1.executeMove(new Move(cellA.copy(), cell8H1, cell0H1));
    assertEquals(cell0H1.getTotalSize(), 2);

    Element root2 = XMLHelper
        .getRootAndCheck(new File(TEST_DIRECTORY + "rules_basic2.xml"), "rule", "invalid file");
    Node node2 = getRulesNode(root2);
    List<IMasterRule> masterRuleList2 = MasterRuleFactory
        .createMasterRules(node2, cellGroupMap1, cellMap1, "first");
    IMasterRule masterRule2 = masterRuleList2.get(0);

    assertTrue(masterRule2.checkValidMove(new Move(cellA.copy(), cell8H.copy(), cell9S.copy())));
    assertTrue(masterRule2.checkValidMove(new Move(cellB.copy(), cell7S.copy(), cell0H.copy())));
    assertFalse(masterRule2.checkValidMove(new Move(cellA.copy(), cell0H.copy(), cell8H.copy())));
    assertFalse(masterRule2.checkValidMove(new Move(cellB.copy(), cell0H.copy(), cellA.copy())));
    assertFalse(masterRule2.checkValidMove(new Move(cellA.copy(), cellA.copy(), cellA.copy())));
    assertFalse(masterRule2.checkValidMove(new Move(cellB.copy(), cellB.copy(), cellB.copy())));
  }
}