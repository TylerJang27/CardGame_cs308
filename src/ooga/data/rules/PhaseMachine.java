package ooga.data.rules;

import ooga.cardtable.ICell;
import ooga.cardtable.IGameState;
import ooga.cardtable.IMove;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Tyler Jang, Andrew Krier, Maverick Chung
 */
public class PhaseMachine implements IPhaseMachine {

    //TODO: FINISH THIS CLASS
    public PhaseMachine(List<IPhase> phases) {

    }

    /**
     * Gives a map of phase names to phases
     *
     * @return map containing phase information
     */
    @Override
    public Map<String, IPhase> getPhases() {
        return null;
    }

    /**
     * Gives a list of phase names
     *
     * @return list containing phase names
     */
    @Override
    public List<String> getPhaseNames() {
        return null;
    }

    /**
     * Gives the first phase name
     *
     * @return the String corresponding to the default phase
     */
    @Override
    public String getStartingPhaseName() {
        return null;
    }

    /**
     * Gives a map of cell names to cells
     *
     * @return map containing cell information
     */
    @Override
    public Map<String, ICell> getTopLevelCells() {
        return null;
    }

    /**
     * Gives a list of cell names
     *
     * @return list containing cell names
     */
    @Override
    public List<String> getTopLevelCellNames() {
        return null;
    }

    /**
     * Updates the phase based on a movement of cells
     *
     * @param move a movement of card cells
     * @return the GameState representing messages for the front end
     */
    @Override
    public IGameState update(IMove move) {
        return null;
    }

    /**
     * Updates the phase based on an arrow
     *
     * @param arrow a transition of states
     */
    @Override
    public void moveToNextPhase(IPhaseArrow arrow) {

    }

    /**
     * Retrieves the current phase
     *
     * @return the current phase
     */
    @Override
    public IPhase getCurrentPhase() {
        return null;
    }

    /**
     * Retrieves the history of past history cells
     *
     * @return the list of past phase history cells
     */
    @Override
    public List<IPhaseHistoryCell> getHistory() {
        return null;
    }
}
