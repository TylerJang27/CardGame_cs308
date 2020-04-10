import ooga.cardtable.Card;
import ooga.cardtable.Cell;
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
    Card c1 = new Card(s, v);
    Card c2 = new Card(s, v);
    assertEquals(a, b);
    a.addCard(Offset.NONE, c1);
    assertNotEquals(a, b);
    b.addCard(Offset.NONE, c2);
    assertEquals(a,b);
  }
}
