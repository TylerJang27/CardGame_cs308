package ooga.cardtable;

public class Player implements IPlayer {

  private String name;
  private int ID;
  private double score;
  private ICell hand;

  public Player(ICell cell, int id) {
    hand = cell;
    ID = id;
    name = "" + id;
    score = 0;
  }

  public Player(ICell cell, int id, String nm) {
    this(cell, id);
    name = nm;
  }

  public Player() {
    this(null, 1);
  }

  @Override
  public ICell getHand() {
    return hand;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String nm) {
    name = nm;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public double getScore() {
    return score;
  }

  @Override
  public void setScore(double d) {
    score = d;
  }
}
