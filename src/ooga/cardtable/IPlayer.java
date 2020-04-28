package ooga.cardtable;

public interface IPlayer {

  ICell getHand();

  String getName();

  void setName(String nm);

  int getID();

  double getScore();

  void setScore(double d);
}
