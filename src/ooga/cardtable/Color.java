package ooga.cardtable;

import java.lang.reflect.Field;

public class Color implements IColor {

  String myName;
  javafx.scene.paint.Color myColor;
  int[] myColors;

  public Color(String color) {
    myName = color;
    try {
      Field field = Class.forName("javafx.scene.paint.Color").getField(color.toUpperCase());
      myColor = (javafx.scene.paint.Color) field.get(null);
    } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
      myColor = javafx.scene.paint.Color.BLACK;
    }
    myColors = new int[]{(int) myColor.getRed(), (int) myColor.getGreen(), (int) myColor.getBlue()};

  }

  @Override
  public String getName() {
    return myName;
  }

  @Override
  public int[] getColors() {
    return myColors;
  }

  @Override
  public javafx.scene.paint.Color getColor() {
    return myColor;
  }
}
