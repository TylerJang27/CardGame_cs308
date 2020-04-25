//import ooga.cardtable.Card;
//import ooga.cardtable.Cell;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import ooga.cardtable.Card;
import ooga.cardtable.Cell;
import ooga.cardtable.ICell;
import ooga.cardtable.Offset;
import ooga.cardtable.Suit;
import ooga.cardtable.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CellTests {

  @BeforeEach
  public void setup() {
    Cell a = new Cell("a");
    Cell b = new Cell("b");
    Suit s = new Suit("Hearts", new int[]{255, 0, 0});
    Value v = new Value("Ace", 1);
    Value v2 = new Value("Two", 2);
    String name = s.getName() + "" + v.getName();
    Card c1 = new Card(name, s, v);
    Card c2 = new Card(name, s, v);
    Card c3 = new Card(s, v2);
  }

  @Test
  public void simpleCellEquals() {
    Cell a = new Cell("a");
    Cell b = new Cell("b");
    Suit s = new Suit("Hearts", new int[]{255, 0, 0});
    Value v = new Value("Ace", 1);
    Value v2 = new Value("Two", 2);
    String name = s.getName() + "" + v.getName();
    Card c1 = new Card(name, s, v);
    Card c2 = new Card(name, s, v);
    Card c3 = new Card(s, v2);
    assertEquals(a, b);
    a.addCard(Offset.NONE, c1);
    assertNotEquals(a, b);
    b.addCard(Offset.NONE, c2);
    assertEquals(a, b);
    a.addCard(Offset.NONE, c3);
    b.addCard(Offset.NONE, c1);
    assertNotEquals(a, b);
  }

  @Test
  public void offsetCellEquals() {
    Cell a = new Cell("a");
    Cell b = new Cell("b");
    Suit s = new Suit("Hearts", new int[]{255, 0, 0});
    Value v = new Value("Ace", 1);
    Value v2 = new Value("Two", 2);
    Card c1 = new Card(s, v);
    Card c2 = new Card(s, v);
    Card c3 = new Card(s, v2);
    a.addCard(Offset.SOUTH, c1);
    a.addCard(Offset.SOUTH, c2);
    b.addCard(Offset.SOUTH, c2);
    b.addCard(Offset.SOUTH, c1);
    assertEquals(a, b);
    a.addCard(Offset.NORTH, c1);
    assertNotEquals(a, b);
    b.addCard(Offset.NORTH, c2);
    assertEquals(a, b);
    ICell as = a.getAllChildren().get(Offset.SOUTH);
    ICell bs = b.getAllChildren().get(Offset.SOUTH);
    as.addCard(Offset.SOUTH, c1);
    assertNotEquals(a, b);
    bs.addCard(Offset.SOUTH, c2);
    assertEquals(a, b);
  }

  @Test
  public void addCellTest() {
    Cell a = new Cell("a");
    Cell b = new Cell("b");
    Cell c = new Cell("c");
    Suit s1 = new Suit("Hearts", new int[]{255, 0, 0});
    Value v1 = new Value("Ace", 1);
    Value v2 = new Value("Two", 2);
    Card c1 = new Card(s1, v1);
    Card c2 = new Card(s1, v2);
    a.addCard(Offset.NONE, c1);
    a.addCard(Offset.SOUTH, c1);
    b.addCard(Offset.NONE, c2);
    b.addCard(Offset.SOUTH, c2);

    a.addCell(Offset.NONE, b);

    c.addCard(Offset.NONE, c1);
    c.addCard(Offset.SOUTH, c1);
    c.addCard(Offset.NONE, c2);
    c.addCard(Offset.SOUTH, c2);
    System.out.println(a);
    System.out.println(b);
    System.out.println(c);
    assertEquals(a, c);
  }

  @Test
  public void addToNoneTest() {
    Cell a = new Cell("a");
    Cell b = new Cell("b");
    Suit s1 = new Suit("Hearts", new int[]{255, 0, 0});
    Value v1 = new Value("Ace", 1);
    Value v2 = new Value("Two", 2);
    Card c1 = new Card(s1, v1);
    Card c2 = new Card(s1, v2);

    b.addCard(Offset.NONE, c1);
    b.addCard(Offset.SOUTH, c2);
    b.addCard(Offset.SOUTH, c2);
    b.addCard(Offset.SOUTH, c2);
    b.addCard(Offset.SOUTH, c2);
    ICell temp = b;
    for (int i = 0; i < 10; i++) {
      temp = temp.getAllChildren().get(Offset.SOUTH);
      temp.addCard(Offset.SOUTH, c2);
    }
    Cell c = (Cell) b.copy();
    a.addCell(Offset.NONE, b);
    System.out.println(a);
    System.out.println(c);

    assertEquals(a, c);
  }

  @Test
  public void copyTest() {
    Cell a = new Cell("a");
    Cell b = new Cell("b");
    Suit s1 = new Suit("Hearts", new int[]{255, 0, 0});
    Value v1 = new Value("Ace", 1);
    Value v2 = new Value("Two", 2);
    Card c1 = new Card(s1, v1);
    Card c2 = new Card(s1, v2);

    b.addCard(Offset.NONE, c1);
    b.addCard(Offset.SOUTH, c2);
    b.addCard(Offset.SOUTH, c2);
    b.addCard(Offset.SOUTH, c2);
    b.addCard(Offset.SOUTH, c2);
    ICell temp = b;
    for (int i = 0; i < 10; i++) {
      temp = temp.getAllChildren().get(Offset.SOUTH);
      temp.addCard(Offset.SOUTH, c2);
    }
    Cell c = (Cell) b.copy();
    System.out.println(b);
    System.out.println(c);

    assertEquals(b, c);
  }

  @Test
  public void stringConversionTest() {
    Cell a = new Cell("a");
    Cell b = new Cell("b");
    Suit s1 = new Suit("Hearts", new int[]{255, 0, 0});
    Value v1 = new Value("Ace", 1);
    Value v2 = new Value("Two", 2);
    Card c1 = new Card(s1, v1);
    Card c2 = new Card(s1, v2);

    b.addCard(Offset.NONE, c1);
    b.addCard(Offset.SOUTH, c2);
    ICell temp = b;
    for (int i = 0; i < 10; i++) {
      temp = temp.getAllChildren().get(Offset.SOUTH);
      temp.addCard(Offset.SOUTH, c2);
    }
    System.out.println(b.toStorageString());
    a = (Cell) Cell.fromStorageString(b.toStorageString());

    assertEquals(b, a);
  }

  @Test
  public void stringConversionNoneTest() {
    Cell a = new Cell("a");
    Cell b = new Cell("b");
    Suit s1 = new Suit("Hearts", new int[]{255, 0, 0});
    Value v1 = new Value("Ace", 1);
    Value v2 = new Value("Two", 2);
    Card c1 = new Card(s1, v1);
    Card c2 = new Card(s1, v2);

    b.addCard(Offset.NONE, c1);
    b.addCard(Offset.SOUTH, c2);
    ICell temp = b;
    for (int i = 0; i < 10; i++) {
      temp = temp.getAllChildren().get(Offset.SOUTH);
      temp.addCard(Offset.SOUTH, c2);
    }
    b.getAllChildren().get(Offset.SOUTH).getDeck().getNextCard();
    System.out.println(b.toStorageString());
    System.out.println(b);
    a = (Cell) Cell.fromStorageString(b.toStorageString());

    assertEquals(b, a);
  }

  @Test
  public void stringConversionFlatDeckTest() {
    Cell a = new Cell("a");
    Cell b = new Cell("b");
    Suit s1 = new Suit("Hearts", new int[]{255, 0, 0});
    Value v1 = new Value("Ace", 1);
    Value v2 = new Value("Two", 2);
    Card c1 = new Card(s1, v1);
    Card c2 = new Card(s1, v2);

    b.addCard(Offset.NONE, c1);
    b.addCard(Offset.SOUTH, c2);
    ICell temp = b;
    for (int i = 0; i < 10; i++) {
      //temp = temp.getAllChildren().get(Offset.SOUTH);
      temp.addCard(Offset.SOUTH, c2);
    }
    b.getAllChildren().get(Offset.SOUTH).getDeck().getNextCard();
    System.out.println(b.toStorageString());
    System.out.println(b);
    a = (Cell) Cell.fromStorageString(b.toStorageString());

    assertEquals(b, a);
  }

  @Test
  public void stringConversionStackedDeckTest() {
    Cell a = new Cell("a");
    Cell b = new Cell("b");
    Suit s1 = new Suit("Hearts", new int[]{255, 0, 0});
    Value v1 = new Value("Ace", 1);
    Value v2 = new Value("Two", 2);
    Card c1 = new Card(s1, v1);
    Card c2 = new Card(s1, v2);

    b.addCard(Offset.NONE, c1);
    b.addCard(Offset.SOUTH, c2);
    ICell temp = b;
    for (int i = 0; i < 10; i++) {
      temp = temp.getAllChildren().get(Offset.SOUTH);
      for (int j = 0; j < 5; j++) {
        temp.addCard(Offset.SOUTH, c2);
        temp.addCard(Offset.SOUTH, c1);
      }
    }
    b.getAllChildren().get(Offset.SOUTH).getDeck().getNextCard();
    System.out.println(b.toStorageString());
    System.out.println(b);
    a = (Cell) Cell.fromStorageString(b.toStorageString());

    assertEquals(b, a);
  }
}
