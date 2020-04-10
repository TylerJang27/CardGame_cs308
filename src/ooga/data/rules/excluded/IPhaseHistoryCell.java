package ooga.data.rules.excluded;

import ooga.data.rules.IPhase;
import ooga.data.rules.IPhaseArrow;

/**
 * Holds the history of a phase and its previous error for gameplay.
 */
public interface IPhaseHistoryCell {

  /**
   * Retrieves the most recent recent PhaseArrow
   *
   * @return the Phase Arrow that brought the phase machine to this phase
   */
  IPhaseArrow getArrowToPhase();

  /**
   * Retrieves the current Phase
   *
   * @return the current Phase
   */
  IPhase getPhase();

}
