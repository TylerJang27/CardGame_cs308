package ooga.cardtable;

public class Value implements IValue {
  private char symbol;
  private int number;
  private String name;

  public Value(String nm, int n) {
    this(nm, n, nm.charAt(0));
  }

  public Value(String nm, int n, char ch) {
    name = nm;
    number = n;
    symbol = ch;
  }

  @Override
  public char getChar() {
    return symbol;
  }

  @Override
  public int getNumber() {
    return number;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getType() {
    return "value";
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Value)) {
      return false;
    }
    Value v = (Value) other;
    return name.equals(v.name) && number == v.number && symbol == v.symbol;
  }
}
