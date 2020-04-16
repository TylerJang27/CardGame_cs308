package ooga.cardtable;

public class Move implements IMove {

  private ICell donor;
  private ICell mover;
  private ICell recipient;
  private IOffset offset;

  public Move(ICell d, ICell m, ICell r) {
    donor = d;
    mover = m;
    recipient = r;
    offset = Offset.NONE;
  }

  public Move(ICell d, ICell m, ICell r, Offset o) {
    this(d, m, r);
    offset = o;
  }

  @Override
  public ICell getMover() {
    return mover;
  }

  @Override
  public void setMover(ICell cell) {
    mover = cell;
  }

  @Override
  public ICell getDonor() {
    return donor;
  }

  @Override
  public void setDonor(ICell cell) {
    donor = cell;
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
