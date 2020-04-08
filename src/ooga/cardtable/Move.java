package ooga.cardtable;

public class Move implements IMove{

  private ICell dragged;
  private ICell recipient;
  private IOffset offset;

  public Move(ICell d, ICell r) {
    dragged = d;
    recipient = r;
    offset = Offset.NONE;
  }

  public Move(ICell d, ICell r, Offset o){
    this(d,r);
    offset = o;
  }

  @Override
  public ICell getDragged() {
    return dragged;
  }

  @Override
  public void setDragged(ICell cell) {
    dragged = cell;
  }

  @Override
  public ICell getRecipient() {
    return recipient;
  }

  @Override
  public void setRecipient(ICell cell) {
    recipient = cell;
  }

  @Override
  public IOffset getResultantOffset() {
    return offset;
  }

  @Override
  public void setOffset(IOffset offset) {
    this.offset = offset;
  }
}
