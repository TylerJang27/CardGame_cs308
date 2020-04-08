package ooga.cardtable;

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
}
