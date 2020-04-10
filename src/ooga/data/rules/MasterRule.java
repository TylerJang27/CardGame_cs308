package ooga.data.rules;


import ooga.cardtable.ICell;
import ooga.cardtable.IGameState;
import ooga.cardtable.IMove;

import java.util.List;

public class MasterRule implements IMasterRule {

    private List<IRule> myRules;

    public MasterRule() {
        
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

    @Override
    public IGameState executeMove(IMove move) {
        return null;
    }

    @Override
    public boolean checkValidMove(IMove move) {
        return false;
    }
}
