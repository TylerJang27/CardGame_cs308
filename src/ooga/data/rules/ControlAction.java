package ooga.data.rules;

import ooga.cardtable.IPlayer;

//TODO: DOCUMENTATION
public class ControlAction implements IControlAction {

    IPhaseArrow myArrow;
    int myVal;

    public ControlAction(IPhaseArrow arrow, int pointVal) {
        myArrow = arrow;
        myVal = pointVal;
    }

    @Override
    public IPhaseArrow execute(IPlayer player) {
        //TODO: ADJUST POINTS HERE
        return myArrow;
    }
}
