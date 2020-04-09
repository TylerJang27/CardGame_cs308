package ooga.cardtable;

import java.util.Map;

public interface ICell {
  //deal with unnecessary massive linked lists

  //TODO: MAKE TOP CARD THE DEFAULT NAME OF THE CELL

  IDeck getDeck();

  String getName();

  Map<IOffset, ICell> getAllChildren();

  Map<IOffset, ICell> getHeldCells();

  void addCard(IOffset offset, ICard card);

}
