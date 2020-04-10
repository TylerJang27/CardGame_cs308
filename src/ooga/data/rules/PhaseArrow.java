package ooga.data.rules;

//TODO: ADD DOCUMENTATION
public class PhaseArrow implements IPhaseArrow {

    private String myStartName;
    private String myMoveName;
    private String myEndName;

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
