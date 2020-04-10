package ooga.cardtable;

public interface IMove {

  ICell getMover();

  void setMover(ICell cell);

  ICell getDonor();

  void setDonor(ICell cell);

  ICell getRecipient();

  void setRecipient(ICell cell);

  IOffset getResultantOffset();

  void setOffset(IOffset offset);

}
