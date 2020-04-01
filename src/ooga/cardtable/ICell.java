package ooga.cardtable;

import java.util.Map;

public interface ICell {

  public ICard getCard();

  public String getName();

  public Map<IOffset, ICell> getChildren();

}
