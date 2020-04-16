package ooga.cardtable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Card implements ICard {

  private String name;
  private boolean faceup;
  private Map<IAttribute, Boolean> attributes;
  private double orientation;

  public Card(String name) {
    this.name = name;
    faceup = false;
    orientation = 0;
  }

  //TODO: TO MAVERICK, PLZ EXPLAIN (FROM TYLER AND ANDREW)
  public Card(String name, ISuit s, IValue v) {
    this(name);
    attributes = new HashMap<>();
    attributes.put(s, true);
    attributes.put(v, true);
    name = s.getName() + "" + v.getName();
  }

  public Card(ISuit s, IValue v) { //fixme remove later? added to make compile
    this(s.getName() + "" + v.getName());
    attributes = new HashMap<>();
    attributes.put(s, true);
    attributes.put(v, true);
  }

  public Card(String name, Map<IAttribute, Boolean> visible) {
    this(name);
    attributes = visible;
  }

  private void setAttributes(Map<IAttribute, Boolean> attr) {
    attributes = attr;
  }

  private Map<IAttribute, Boolean> invert(Map<IAttribute, Boolean> attr) {
    Map<IAttribute, Boolean> ret = new HashMap<>();
    for (Entry<IAttribute, Boolean> e : attr.entrySet()) {
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
    for (Entry<IAttribute, Boolean> e : attributes.entrySet()) {
      //fixme also need to hide info here
      if (e.getKey().getType().toLowerCase().contains("suit")) { // && e.getValue()) {//fixme monster
        ret = (ISuit) e.getKey();
      }
    }
    return ret;
  }

  @Override
  public IValue getValue() { //FIXME duped code
    IValue ret = null;
    for (Entry<IAttribute, Boolean> e : attributes.entrySet()) {
      System.out.println("Value check: "+e);
      // fixme need to hide info here
      if (e.getKey().getType().toLowerCase().contains("value")){// && e.getValue()) {//fixme monster
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
    setAttributes(invert(attributes));
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

  @Override
  public ICard copy() {
    Map<IAttribute, Boolean> attrs = new HashMap<>();
    for (Entry<IAttribute, Boolean> e: attributes.entrySet()) {
      attrs.put(e.getKey(), e.getValue()); //fixme hide info
    }
    Card ret = new Card(name, attrs);
    ret.rotate(orientation);
    ret.faceup = isFaceUp();
    assert getSuit().equals(ret.getSuit());
    assert getSuit().getColorName().equals(ret.getSuit().getColorName());
    assert getValue().equals(ret.getValue()) && ret.getValue()!=null;
    return ret;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Card)) {
      return false;
    }
    Card c = (Card) other;
    return name.equals(c.name) && faceup == c.faceup && orientation == c.orientation &&
        attributes.equals(c.attributes);
  }

  @Override
  public String toString() {
    return name;
  }
}
