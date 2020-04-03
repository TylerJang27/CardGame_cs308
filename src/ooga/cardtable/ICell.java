package ooga.cardtable;

import java.util.Map;

public interface ICell {
  //deal with unnecessary massive linked lists

  ICard getCard();

  String getName();

  Map<IOffset, ICell> getAllChildren();

  Map<IOffset, ICell> getHeldCells();

}
