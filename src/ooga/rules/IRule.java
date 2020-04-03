package ooga.rules;

import ooga.cardtable.ICell;

public interface IRule {

  boolean checkValidAcceptor(ICell cell);

  boolean checkValidDonor(ICell cell);

  boolean checkValidTransfer(ICell donor, ICell acceptor);

  ICellRegex getAcceptorRegex();

  ICellRegex getDonorRegex();
}
