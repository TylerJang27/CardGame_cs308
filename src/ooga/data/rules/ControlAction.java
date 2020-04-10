package ooga.data.rules;

import ooga.cardtable.IPlayer;

//TODO: DOCUMENTATION
public class ControlAction implements IControlAction {

    IPhaseArrow myArrow;
    IPlayer myPlayer;
    int myVal;

    public ControlAction(IPhaseArrow arrow, IPlayer player, int pointVal) {
        myArrow = arrow;
        myPlayer = player;
        myVal = pointVal;
    }

    @Override
    public IPhaseArrow execute() {
        //TODO: ADJUST POINTS HERE
        return myArrow;
    }
}
