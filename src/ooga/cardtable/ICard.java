package ooga.cardtable;

public interface ICard {

  public String getName();

  public ISuit getSuit();

  public IValue getValue();

  public boolean isFaceUp();

  /**
   * Returns an ICard where data that is visible is accessible, and data that is invisible (e.g. facedown)
   * is null
   * @return
   */
  public ICard getVisibleData();


}
