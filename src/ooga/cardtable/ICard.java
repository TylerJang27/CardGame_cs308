package ooga.cardtable;

public interface ICard {

  String getName();

  ISuit getSuit();

  IValue getValue();

  boolean isFaceUp();

  void flip();

  void rotate(double degrees);

  double getRotate();

  /**
   * Returns an ICard where data that is visible is accessible, and data that is invisible (e.g.
   * facedown) is null
   *
   * @return
   */
  ICard getVisibleData();

  ICard copy();

  void setFixed(boolean fixed);

  boolean isFixed();

  String toStorageString();
}
