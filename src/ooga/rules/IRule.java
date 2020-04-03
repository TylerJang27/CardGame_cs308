package ooga.rules;

import ooga.cardtable.ICell;

public interface IRule {

  boolean checkValidAcceptor(ICell cell);

  boolean checkValidDonor(ICell cell);

  ICellRegex getAcceptorRegex();

  ICellRegex getDonorRegex();
}
