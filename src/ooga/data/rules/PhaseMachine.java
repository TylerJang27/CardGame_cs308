package ooga.data.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import ooga.cardtable.GameState;
import ooga.cardtable.ICell;
import ooga.cardtable.IDeck;
import ooga.cardtable.IGameState;
import ooga.cardtable.IMove;
import ooga.cardtable.IOffset;
import ooga.cardtable.IPlayer;
import ooga.cardtable.Offset;
import ooga.data.XMLException;
import ooga.data.factories.Factory;
import ooga.data.factories.PhaseMachineFactory;
import ooga.data.rules.excluded.IPhaseHistoryCell;

/**
 * This class implements IPhaseMachine and governs the Finite State Machine that will process moves
 * and update the IPhase accordingly.
 *
 * @author Maverick Chung, Tyler Jang
 */
public class PhaseMachine implements IPhaseMachine {

  private static final String RESOURCE_PACKAGE = PhaseMachineFactory.RESOURCE_PACKAGE;
  private static final String PHASES = "phases";
  private static final ResourceBundle RESOURCES = ResourceBundle
      .getBundle(RESOURCE_PACKAGE + PHASES);

  private static final String WIN = "Win";
  private static final String LOSS = "Loss";

  private Map<String, IPhase> phases;
  private IPhase startPhase;
  private IPhase currentPhase;
  private List<ICell> cells;
  private List<IPhaseHistoryCell> history;
  private ISettings mySettings;
  private IMove lastMove;
  private IDeck fullDeck;
  private IPlayer currentPlayer;

  /**
   * The Constructor for the PhaseMachine, taking information about phases, settings, and the entire
   * deck. Initializes the ICell information as necessary.
   *
   * @param ph        a Map of String IPhase names to IPhases.
   * @param startName a String representing the intial phase's name.
   * @param settings  an ISettings implementation containing settings information for the game.
   * @param deck      an IDeck implementation containing a copy of the entire deck of cards
   */
  public PhaseMachine(Map<String, IPhase> ph, String startName, ISettings settings, IDeck deck) {
    fullDeck = deck;
    lastMove = null;
    history = new ArrayList<>();
    phases = ph;
    startPhase = phases.get(startName);
    mySettings = settings;
    restartGame();
  }

  /**
   * Restarts the game, reinitializing the cells, cards, and players
   */
  @Override
  public void restartGame() {
    currentPhase = startPhase;
    cells = new ArrayList<>();
    IDeck deckCopy = fullDeck.copy();
    for (Map.Entry<String, ICell> e : getTopLevelCells().entrySet()) {
      cells.add(e.getValue());
    }
    for (Map.Entry<String, ICell> e : currentPhase.getMyCellMap().entrySet()) {
      e.getValue().initializeCards(deckCopy);
    }
    cycleAutomatic();
  }

  /**
   * Updates to the next IPhase if the current phase is automatic and its conditions are met.
   */
  private void cycleAutomatic() {
    if (currentPhase.isAutomatic()) {
      IPhaseArrow arrow = currentPhase.executeAutomaticActions(currentPlayer, lastMove);
      moveToNextPhase(arrow);
    }
  }

  /**
   * Gives a map of phase names to phases
   *
   * @return map containing phase information
   */
  @Override
  public Map<String, IPhase> getPhases() {
    return new HashMap<>(phases);
  }

  /**
   * Gives a list of phase names
   *
   * @return list containing phase names
   */
  @Override
  public List<String> getPhaseNames() {
    return new ArrayList<>(phases.keySet());
  }

  /**
   * Gives the first phase name
   *
   * @return the String corresponding to the default phase
   */
  @Override
  public String getStartingPhaseName() {
    return startPhase.getMyName();
  }

  /**
   * Gives a map of cell names to cells
   *
   * @return map containing cell information
   */
  @Override
  public Map<String, ICell> getTopLevelCells() {
    return currentPhase.getMyCellMap();
  }

  /**
   * Gives a list of cell names
   *
   * @return list containing cell names
   */
  @Override
  public List<String> getTopLevelCellNames() {
    List<String> ret = new ArrayList<>();
    for (ICell c : cells) {
      ret.add(c.getName());
    }
    return ret;
  }

  /**
   * Updates the frontend's IMove to contain relevant references to backend ICells
   *
   * @param move the IMove to process
   * @return the processed IMove
   */
  private IMove replaceMoveCells(IMove move) {
    ICell d = findNamedCell(move.getDonor().getName());
    if (d != null) {
      move.setDonor(d);
    }
    ICell r = findNamedCell(move.getRecipient().getName());
    if (r != null) {
      move.setRecipient(r);
    }
    ICell m = findNamedCell(move.getMover().getName());
    if (m != null) {
      move.setMover(m);
    }
    return move;
  }

  /**
   * Searches for a cell by name.
   *
   * @param nm the name of the cell
   * @return the relevant ICell, otherwise null
   */
  private ICell findNamedCell(String nm) {
    updateCellParents();
    String[] names = nm.split(",");
    String firstName = names[0];
    String[] restNames = new String[names.length - 1];
    for (int i = 1; i < names.length; i++) {
      restNames[i - 1] = names[i];
    }
    String restName = String.join(",", restNames);
    ICell ret = null;
    for (ICell cell : cells) {
      if (cell.getName().toLowerCase().equals(firstName)) {
        ret = cell.followNamespace(restName);
        if (ret != null) {
          return ret;
        }
      }
    }
    return ret;
  }

  /**
   * Updates the phase based on a movement of cells
   *
   * @param move a movement of card cells
   * @return the GameState representing messages for the front end
   */
  @Override
  public IGameState update(IMove move) {
    move = replaceMoveCells(move);
    IPhaseArrow arrow = currentPhase.executeMove(move, currentPlayer);
    lastMove = move;
    if (arrow != null) {
      moveToNextPhase(arrow);
      updateCellParents();
      if (currentPhase.getMyName().equalsIgnoreCase(RESOURCES.getString(WIN))) {
        return GameState.WIN;
      } else if (currentPhase.getMyName().equalsIgnoreCase(RESOURCES.getString(LOSS))) {
        return GameState.LOSS;
      }
      return GameState.WAITING;
    }
    updateCellParents();
    return GameState.INVALID;
  }

  /**
   * Updates all the cells' parentage.
   */
  private void updateCellParents() {
    for (ICell c : cells) {
      c.updateParentage();
    }
  }

  /**
   * Cycles to the next phase based on an IPhaseArrow.
   *
   * @param arrow the IPhaseArrow used to determine the next IPhase
   * @throws XMLException if the phase name in the arrow does not match valid phases
   */
  private void moveToNextPhase(IPhaseArrow arrow) {
    //TODO: UPDATE HISTORY
    if (phases.containsKey(arrow.getEndPhaseName())) {
      currentPhase = phases.get(arrow.getEndPhaseName());
      cycleAutomatic();
    } else {
      throw new XMLException(Factory.CONTROL_ERROR);
    }
  }

  /**
   * Sets the current player for the move.
   *
   * @param player the current player
   */
  @Override
  public void setCurrentPlayer(IPlayer player) {
    currentPlayer = player;
  }

  /**
   * Retrieves the current phase
   *
   * @return the current phase
   */
  @Override
  public IPhase getCurrentPhase() {
    return currentPhase;
  }

  /**
   * Retrieves the history of past history cells
   *
   * @return the list of past phase history cells
   */
  @Override
  public List<IPhaseHistoryCell> getHistory() {
    System.out.println("To be implemented later");
    return null; //FIXME
  }

  /**
   * Determines whether or not an ICell is a valid donor for this phase.
   *
   * @param cell the ICell to validate
   * @return whether or not the ICell is a valid donor
   */
  @Override
  public boolean isValidDonor(ICell cell) {
    return currentPhase.isValidDonor(cell);
  }

  /**
   * Returns the ISettings for the current game.
   *
   * @return the settings for the game
   */
  @Override
  public ISettings getSettings() {
    return mySettings;
  }

  /**
   * Sets the data in the cells from a load.
   *
   * @param cellMap the Map of String ICell names to ICells to load
   */
  @Override
  public void setCellData(Map<String, ICell> cellMap) {
    for (Map.Entry<String, ICell> e : currentPhase.getMyCellMap().entrySet()) {
      for (int k = 0; k < Offset.values().length; k++) {
        IOffset off = Offset.values()[k];
        e.getValue().removeCellAtOffset(off);
        while (e.getValue().getDeck().size() > 0) {
          e.getValue().getDeck().getNextCard();
        }
      }
      e.getValue().addCell(Offset.NONE, cellMap.get(e.getKey()));
    }
  }

  /**
   * Sets the phase from a load.
   *
   * @param phase the name of the phase to load in
   */
  @Override
  public void setPhase(String phase) {
    currentPhase = phases.get(phase);
  }
}
