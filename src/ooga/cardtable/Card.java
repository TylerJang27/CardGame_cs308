package ooga.cardtable;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class Card implements ICard {

  private static String sep = "~~";

  private String name;
  private boolean faceup;
  private Map<IAttribute, Boolean> attributes;
  private double orientation;
  private boolean isFixed = false;

  public Card(String name) {
    this.name = name;
    //System.out.println("card name" + name);
    faceup = false;
    orientation = 0;
  }

  public Card(String name, ISuit s, IValue v) {
    this(name);
    attributes = new HashMap<>();
    attributes.put(s, true);
    attributes.put(v, true);
    //name = s.getName() + "" + v.getName();
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

  @Override
  public void setFixed(boolean fixed) {
    isFixed = fixed;
  }

  @Override
  public boolean isFixed() {
    return isFixed;
  }

  @Override
  public String toStorageString() {
    String ret = name + sep + faceup + sep + orientation + sep + isFixed + sep;
    ret += getSuit().toStorageString() + sep + getValue().toStorageString();
    return ret;
  }

  public static ICard fromStorageString(String input) {
    if (input == null) {
      return null;
    }
    String[] info = input.split(Pattern.quote(sep));
    String nm = info[0];
    boolean fc = Boolean.parseBoolean(info[1]);
    double ori = Double.parseDouble(info[2]);
    boolean fx = Boolean.parseBoolean(info[3]);
    ISuit s = Suit.fromStorageString(info[4]);
    IValue v = Value.fromStorageString(info[5]);
    ICard ret = new Card(nm, s, v);
    ret.setFixed(fx);
    if (fc) {
      ret.flip();
    }
    ret.rotate(ori);
    return ret;
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
      if (e.getKey().getType().toLowerCase()
          .contains("suit")) { // && e.getValue()) {//fixme monster
        ret = (ISuit) e.getKey();
      }
    }
    return ret;
  }

  @Override
  public IValue getValue() { //FIXME duped code
    IValue ret = null;
    for (Entry<IAttribute, Boolean> e : attributes.entrySet()) {
      //System.out.println("Value check: "+e);
      // fixme need to hide info here
      if (e.getKey().getType().toLowerCase()
          .contains("value")) {// && e.getValue()) {//fixme monster
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
    for (Entry<IAttribute, Boolean> e : attributes.entrySet()) {
      attrs.put(e.getKey(), e.getValue()); //fixme hide info
    }
    Card ret = new Card(name, attrs);
    ret.rotate(orientation);
    ret.faceup = isFaceUp();
    ret.setFixed(isFixed);
    assert getSuit().equals(ret.getSuit());
    assert getSuit().getColorName().equals(ret.getSuit().getColorName());
    assert getValue().equals(ret.getValue()) && ret.getValue() != null;
    return ret;
  }

  @Override
  public boolean equals(Object other) {
    return other.getClass() == this.getClass() &&
        name.equals(((Card) other).name) &&
        faceup == ((Card) other).faceup &&
        orientation == ((Card) other).orientation &&
        getSuit().equals(((Card) other).getSuit()) &&
        getValue().equals(((Card) other).getValue());
    //attributes.keySet().equals(c.attributes.keySet()); //fixme add in generic attributes
  }

  @Override
  public String toString() {
    return name;
  }
}
