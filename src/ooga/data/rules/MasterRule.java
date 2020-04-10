package ooga.data.rules;

import ooga.cardtable.ICell;
import ooga.cardtable.IMove;

public class MasterRule implements IMasterRule {

    private Rule myReceiverRule;
    private Rule myMoverRule;
    private Rule myDonorRule;

    /**
     * Evaluates whether the cell in question can receive a cell in this phase
     *
     * @param cell the cell to be receiving a cell
     * @return whether or not the cell is a valid acceptor
     */
    @Override
    public boolean checkValidAcceptor(ICell cell) {
        return false;
    }

    /**
     * Evaluates whether the cell in question can donate a cell in this phase
     *
     * @param cell the cell to be receiving a cell
     * @return whether or not the cell is a valid donor
     */
    @Override
    public boolean checkValidDonor(ICell cell) {
        return false;
    }

    /**
     * Evaluates whether the cell transfer is valid
     *
     * @param don the donor cell
     * @param rec the acceptor cell
     * @return whether or not the transfer is allowed
     */
    @Override
    public boolean checkValidTransfer(ICell don, ICell rec) {
        return false;
    }

    @Override
    public boolean checkValidMove(IMove move) {
        return false;
    }
}
