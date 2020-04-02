package ooga.cardtable;

import java.util.Map;

public interface ICell {
  //deal with unnecessary massive linked lists

  public ICard getCard();

  public String getName();

  public Map<IOffset, ICell> getAllChildren();

  public Map<IOffset, ICell> getHeldCells();

}
