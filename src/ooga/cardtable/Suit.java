package ooga.cardtable;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Suit implements ISuit {

  private int[] color;
  private String name;
  private IColor myColor;
  private String myColorName;
  private static String sep = ";;";

  public Suit(String nm, IColor color) {
    this(nm, color.getColors());
    myColor = color;
    myColorName = myColor.getName();
  }

  public Suit(String nm, int[] c) {
    if (c.length != 3) {
      throw new IllegalArgumentException("color not 3 long"); //fixme
    }
    name = nm;
    color = Arrays.copyOf(c, c.length);
    myColorName = "" + color[0] + "" + color[1] + "" + color[2];
  }

  @Override
  public int[] getColor() {
    return color;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getType() {
    return "suit";
  }

  @Override
  public String toStorageString() { //fixme switch to icolor
    return name + sep + myColorName;
  }

  public static ISuit fromStorageString(String input) {
    String[] info = input.split(Pattern.quote(sep));
    String nm = info[0];
    String cnm = info[1];
    return new Suit(nm, new Color(cnm));
  }

  @Override
  public String getColorName() {
    return myColorName;
  }

  @Override
  public boolean equals(Object other) { //fixme switch to icolor
    //return name.equals(s.name) && Arrays.equals(color, ((Suit) other).color);
    return other.getClass() == this.getClass() &&
        name.equals(((Suit) other).name) &&
        myColorName.equals(((Suit) other).myColorName);
  }

  @Override
  public String toString() {
    return name;
  }
}
