package ooga.cardtable;

public interface IMove {

  ICell getDragged();

  void setDragged(ICell cell);

  ICell getRecipient();

  void setRecipient(ICell cell);

  IOffset getResultantOffset();

  void setOffset(IOffset offset);

}
