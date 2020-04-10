package ooga.cardtable;

import java.util.Arrays;
import java.util.List;

public enum Offset implements IOffset {
  NONE ("none"),
  NORTH ("north"),
  NORTHEAST("northeast"),
  EAST("east"),
  SOUTHEAST("southeast"),
  SOUTH("south"),
  SOUTHWEST("southwest"),
  WEST("west"),
  NORTHWEST("northwest");

  private String type;
  Offset(String t) {
    type = t;
  }

  @Override
  public String getOffset() {
    return type;
  }

  public static final List<String> validOffsets = Arrays.asList(NONE.getOffset(),
          NORTH.getOffset(),
          NORTHEAST.getOffset(),
          EAST.getOffset(),
          SOUTHEAST.getOffset(),
          SOUTH.getOffset(),
          SOUTHWEST.getOffset(),
          WEST.getOffset(),
          NORTH.getOffset());
}
