package ooga.cardtable;

public interface IMove {

  public ICell getDragged();

  public ICell getRecipient();

  public IOffset getResultantOffset();

  public void setDragged(ICell cell);

  public void setRecipient(ICell cell);

  public void setOffset(IOffset offset);

}
