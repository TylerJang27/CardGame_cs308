package ooga.cardtable;

import java.util.regex.Pattern;

public class Value implements IValue {

  private char symbol;
  private int number;
  private String name;

  private static String sep = "%%";

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
  public String toStorageString() {
    return name + sep + number + sep + symbol;
  }

  public static IValue fromStorageString(String input) {
    String[] info = input.split(Pattern.quote(sep));
    String nm = info[0];
    int n = Integer.parseInt(info[1]);
    char c = info[2].charAt(0);
    return new Value(nm, n, c);
  }

  @Override
  public boolean equals(Object other) {
    return other.getClass() == this.getClass() &&
        name.equals(((Value) other).name) &&
        number == ((Value) other).number &&
        symbol == ((Value) other).symbol;
  }

  @Override
  public String toString() {
    return name;
  }
}
