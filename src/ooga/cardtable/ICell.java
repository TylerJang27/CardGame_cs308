package ooga.cardtable;

import java.util.Map;
import java.util.function.Function;

public interface ICell {
  //deal with unnecessary massive linked lists

  //TODO: MAKE TOP CARD THE DEFAULT NAME OF THE CELL

  void setDraw(Function<IDeck, ICell> initializer); //TODO: ADD TO API CHANGES

  void initializeCards(IDeck mainDeck); //TODO: ADD TO API CHANGES

  IDeck getDeck();

  String getName();

  Map<IOffset, ICell> getAllChildren();

  Map<IOffset, ICell> getHeldCells();

  void addCard(IOffset offset, ICard card);

}
