package ooga.cardtable;

import java.util.Arrays;

public class Suit implements ISuit {
  private int[] color;
  private String name;
  private IColor myColor;
  private String myColorName;

  //TODO: CAN WE CHANGE THIS TO AN ICOLOR
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
  public String getColorName() { return myColorName; }
}
