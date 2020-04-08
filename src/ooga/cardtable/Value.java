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
}
