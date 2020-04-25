package ooga.data.rules;

/**
 * This class implements IPhaseArrow and holds information about a phase change.
 *
 * @author Maverick Chung
 */
public class PhaseArrow implements IPhaseArrow {

  private String myStartName;
  private String myMoveName;
  private String myEndName;

  /**
   * The constructor for a PhaseArrow, settings its relevant IPhase names.
   *
   * @param startName the starting name of the arrow
   * @param moveName  the name of the arrow
   * @param endName   the ending name of the arrow
   */
  public PhaseArrow(String startName, String moveName, String endName) {
    myStartName = startName;
    myMoveName = moveName;
    myEndName = endName;
  }

  /**
   * Retrieves the starting phase name
   *
   * @return the starting phase name for an arrow
   */
  @Override
  public String getStartPhaseName() {
    return myStartName;
  }

  /**
   * Retrieves the ending phase name
   *
   * @return the ending phase name for an arrow
   */
  @Override
  public String getEndPhaseName() {
    return myEndName;
  }

  /**
   * Retrieves the name of the phase arrow move
   *
   * @return the move name for the arrow
   */
  @Override
  public String getMoveName() {
    return myMoveName;
  }
}
