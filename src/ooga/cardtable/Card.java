package ooga.cardtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Card implements ICard {

  private String name;
  private boolean faceup;
  private Map<IAttribute, Boolean> attributes;
  private double orientation;

  public Card() {
    name = "Unknown Card";
    faceup = false;
    orientation = 0;
  }

  public Card(ISuit s, IValue v) {
    this();
    attributes = new HashMap<>();
    attributes.put(s, true);
    attributes.put(v, true);
  }

  public Card(Map<IAttribute, Boolean> visible) {
    this();
    attributes = visible;
  }

  private void setAttributes(Map<IAttribute, Boolean> attr) {
    attributes = attr;
  }

  private Map<IAttribute, Boolean> invert(Map<IAttribute, Boolean> attr) {
    Map<IAttribute, Boolean> ret = new HashMap<>();
    for (Entry<IAttribute, Boolean> e: attr.entrySet()) {
      ret.put(e.getKey(), !e.getValue());
    }
    return ret;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public ISuit getSuit() {
    ISuit ret = null;
    for (Entry<IAttribute, Boolean> e: attributes.entrySet()) {
      if (e.getValue() && e.getKey().getType().toLowerCase().contains("suit")) { //fixme monster
        ret = (ISuit) e.getKey();
      }
    }
    return ret;
  }

  @Override
  public IValue getValue() { //FIXME duped code
    IValue ret = null;
    for (Entry<IAttribute, Boolean> e: attributes.entrySet()) {
      if (e.getValue() && e.getKey().getType().toLowerCase().contains("value")) { //fixme monster
        ret = (IValue) e.getKey();
      }
    }
    return ret;
  }

  @Override
  public boolean isFaceUp() {
    return faceup;
  }

  @Override
  public void flip() {
    faceup = !faceup;
  }

  @Override
  public void rotate(double degrees) {
    orientation += degrees;
    orientation %= 360;
  }

  @Override
  public double getRotate() {
    return orientation;
  }

  @Override
  public ICard getVisibleData() {
    return this;
  }
}
