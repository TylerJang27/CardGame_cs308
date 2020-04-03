package ooga.data;

import ooga.cardtable.ICell;

import java.util.List;
import java.util.Map;

/**
 * Gets construction data from XML file
 * Gives Table cells with rules implemented
 * Gives Table state machine to process turns/winning/etc.
 */
public interface IRuleSet {

    /** Deck Stuff **/ //TODO: GET MAVERICK'S INPUT/HELP

    /**
     Map of Phase Names to Phase
        Each Phase has Map of Cell to boolean for donor
        Each Phase has Map of Cell to Function
            Each Function takes in 2 Cells(subcells) and computes an operation

     Map of Cell Names to Cells data
        Cell data with Consumers for drawing initial cards
        Cell data booleans for fanning and sectionable
     **/

    /**
     * Gives a map of phase names to phases
     * @return map containing phase information
     */
    Map<String, IPhase> getPhases();

    /**
     * Gives a list of phase names
     * @return list containing phase names
     */
    List<String> getPhaseNames();

    /**
     * Gives the first phase name
     * @return the String corresponding to the default phase
     */
    String getStartingPhaseName();

    /**
     * Gives a map of cell names to cells
     * @return map containing cell information
     */
    Map<String, ICell> getCells(); //TODO: SHOULD THIS BE ICELL?

    /**
     * Gives a list of cell names
     * @return list containing cell names
     */
    List<String> getCellNames();

    //TODO: methods for more advanced accessing and behavior

}
