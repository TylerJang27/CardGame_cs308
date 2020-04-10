import ooga.cardtable.Card;
import ooga.cardtable.Cell;
import ooga.cardtable.ICell;
import ooga.cardtable.Offset;
import ooga.cardtable.Suit;
import ooga.cardtable.Value;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CellTests {

  @Test
  public void simpleCellEquals() {
    Cell a = new Cell("a");
    Cell b = new Cell("b");
    Suit s = new Suit("Hearts", new int[] {255,0,0});
    Value v = new Value("Ace", 1);
    Value v2 = new Value("Two", 2);
    Card c1 = new Card(s, v);
    Card c2 = new Card(s, v);
    Card c3 = new Card(s, v2);
    assertEquals(a, b);
    a.addCard(Offset.NONE, c1);
    assertNotEquals(a, b);
    b.addCard(Offset.NONE, c2);
    assertEquals(a,b);
    a.addCard(Offset.NONE, c3);
    b.addCard(Offset.NONE, c1);
    assertNotEquals(a,b);
  }

  @Test
  public void offsetCellEquals() {
    Cell a = new Cell("a");
    Cell b = new Cell("b");
    Suit s = new Suit("Hearts", new int[] {255,0,0});
    Value v = new Value("Ace", 1);
    Card c1 = new Card(s, v);
    Card c2 = new Card(s, v);
    a.addCard(Offset.SOUTH, c1);
    a.addCard(Offset.SOUTH, c2);
    b.addCard(Offset.SOUTH, c2);
    b.addCard(Offset.SOUTH, c1);
    assertEquals(a,b);
    a.addCard(Offset.NORTH, c1);
    assertNotEquals(a,b);
    b.addCard(Offset.NORTH, c2);
    assertEquals(a,b);
    ICell as = a.getAllChildren().get(Offset.SOUTH);
    ICell bs = b.getAllChildren().get(Offset.SOUTH);
    as.addCard(Offset.SOUTH, c1);
    assertNotEquals(a,b);
    bs.addCard(Offset.SOUTH, c2);
    assertEquals(a,b);
  }
}
