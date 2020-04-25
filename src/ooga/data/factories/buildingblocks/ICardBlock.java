package ooga.data.factories.buildingblocks;

import ooga.cardtable.ICard;
import ooga.cardtable.IOffset;

/**
 * This interface serves as a wrapper used to construct initial ICard configuration with additional
 * customization. Extends ICard.
 *
 * @author Tyler Jang
 */
public interface ICardBlock extends ICard {

  /**
   * Retrieves the ICard contained by this wrapper.
   *
   * @return ICard this ICardBlock represents
   */
  ICard getCard();

  /**
   * Retrieves the IOffset contained by this wrapper for initialization.
   *
   * @return a valid IOffset or null
   */
  IOffset getOffset();

  /**
   * Retrieves the rotation contained by this wrapper for initialization.
   *
   * @return a Double representing rotation or null
   */
  Double getFutureRotation();
}
