package ooga.data.factories.buildingblocks;

import ooga.cardtable.ICard;
import ooga.cardtable.IOffset;
import ooga.cardtable.ISuit;
import ooga.cardtable.IValue;

/**
 * This class implements ICardBlock and serves as a wrapper for an ICard. Holds offset and rotation
 * information for initialization.
 *
 * @author Tyler Jang
 */
public class CardBlock implements ICardBlock {

  private ICard myCard;
  private IOffset myOffset;
  private Double myRotation;

  /**
   * Constructor for CardBlock, setting the wrapper's card, offset, and rotation
   *
   * @param card     ICard held by this CardBlock
   * @param offset   IOffset offset of the card to add
   * @param rotation Double representing the rotation of the card
   */
  public CardBlock(ICard card, IOffset offset, Double rotation) {
    myCard = card;
    myOffset = offset;
    myRotation = rotation;
  }

  /**
   * Retrieves the ICard contained by this wrapper.
   *
   * @return ICard this ICardBlock represents
   */
  @Override
  public ICard getCard() {
    return myCard;
  }

  /**
   * Retrieves the IOffset contained by this wrapper for initialization.
   *
   * @return a valid IOffset or null
   */
  @Override
  public IOffset getOffset() {
    return myOffset;
  }

  /**
   * Retrieves the rotation contained by this wrapper for initialization.
   *
   * @return a Double representing rotation or null
   */
  @Override
  public Double getFutureRotation() {
    return myRotation;
  }

  @Override
  public String getName() {
    return myCard.getName();
  }

  @Override
  public ISuit getSuit() {
    return myCard.getSuit();
  }

  @Override
  public IValue getValue() {
    return myCard.getValue();
  }

  @Override
  public boolean isFaceUp() {
    return myCard.isFaceUp();
  }

  @Override
  public void flip() {
    myCard.flip();
  }

  @Override
  public void rotate(double degrees) {
    myCard.rotate(degrees);
  }

  @Override
  public double getRotate() {
    return myCard.getRotate();
  }

  /**
   * Returns an ICard where data that is visible is accessible, and data that is invisible (e.g.
   * facedown) is null
   *
   * @return
   */
  @Override
  public ICard getVisibleData() {
    return myCard.getVisibleData();
  }

  @Override
  public ICard copy() {
    return myCard.copy();
  }

  @Override
  public void setFixed(boolean fixed) {
    myCard.setFixed(fixed);
  }

  @Override
  public boolean isFixed() {
    return myCard.isFixed();
  }

  @Override
  public String toStorageString() { //fixme added by maverick to make compile
    return getCard().toStorageString();
  }
}
