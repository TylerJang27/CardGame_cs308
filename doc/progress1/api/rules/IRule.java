package ooga.rules;

import ooga.cardtable.ICell;

/**
 * Gets and evaluates rules for a given phase, based on generic and specific rules.
 */
public interface IRule {

  /**
   * Evaluates whether the cell in question can receive a cell in this phase
   *
   * @param cell the cell to be receiving a cell
   * @return whether or not the cell is a valid acceptor
   */
  boolean checkValidAcceptor(ICell cell);

  /**
   * Evaluates whether the cell in question can donate a cell in this phase
   *
   * @param cell the cell to be receiving a cell
   * @return whether or not the cell is a valid donor
   */
  boolean checkValidDonor(ICell cell);

  /**
   * Evaluates whether the cell transfer is valid
   *
   * @param don the donor cell
   * @param rec the acceptor cell
   * @return whether or not the transfer is allowed
   */
  boolean checkValidTransfer(ICell don, ICell rec);

  /**
   * Returns the regex rules for generic acceptors
   *
   * @return the acceptor regex logic
   */
  ICellRegex getAcceptorRegex();

  /**
   * Returns the regex rules for generic donors
   *
   * @return the donor regex logic
   */
  ICellRegex getDonorRegex();

  /**
   * Returns the regex rules for specific transfers
   *
   * @return the transfer regex logic
   */
  ICellRegex getTransferRegex();
}
