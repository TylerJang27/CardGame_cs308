package ooga.data.rules;

import ooga.cardtable.ICell;
import ooga.cardtable.IGameState;
import ooga.cardtable.IMove;
import ooga.data.rules.excluded.IPhaseHistoryCell;

import java.util.List;
import java.util.Map;

/**
 * Gets construction data from XML file Gives Table cells with rules implemented Gives Table state
 * machine to process turns/winning/etc.
 *
 * @author Maverick Chung, Tyler Jang
 */
public interface IPhaseMachine {

    String DATA_TYPE = "rule";

    /**
     * Restarts the game, reinitializing the cells, cards, and players
     */
    void restartGame(); //TODO: ADD TO API CHANGES

    /**
     * Gives a map of phase names to phases
     *
     * @return map containing phase information
     */
    Map<String, IPhase> getPhases();

    //void addPhase(IPhase phase); //TODO: REMOVE IN API CHANGES

    /**
     * Gives a list of phase names
     *
     * @return list containing phase names
     */
    List<String> getPhaseNames();

    /**
     * Gives the first phase name
     *
     * @return the String corresponding to the default phase
     */
    String getStartingPhaseName();

    /**
     * Gives a map of cell names to cells
     *
     * @return map containing cell information
     */
    Map<String, ICell> getTopLevelCells();

    //void setCellList(List<ICell> cells);

    /**
     * Gives a list of cell names
     *
     * @return list containing cell names
     */
    List<String> getTopLevelCellNames();

    /**
     * Updates the phase based on a movement of cells
     *
     * @param move a movement of card cells
     * @return the GameState representing messages for the front end
     */
    IGameState update(IMove move);

    /**
     * Updates the phase based on an arrow
     *
     * @param arrow a transition of states
     */
    //void moveToNextPhase(IPhaseArrow arrow);

    /**
     * Retrieves the current phase
     *
     * @return the current phase
     */
    IPhase getCurrentPhase();

    /**
     * Retrieves the history of past history cells
     *
     * @return the list of past phase history cells
     */
    List<IPhaseHistoryCell> getHistory();

    /**
     * Determines whether or not an ICell is a valid donor for this phase.
     *
     * @param cell the ICell to validate
     * @return whether or not the ICell is a valid donor
     */
    boolean isValidDonor(ICell cell); //TODO: ADD TO API CHANGES

    /**
     * Returns the ISettings for the current game.
     *
     * @return the settings for the game
     */
    ISettings getSettings(); //TODO: ADD TO API CHANGES
}
