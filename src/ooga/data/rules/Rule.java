package ooga.data.rules;

import ooga.cardtable.IMove;

import java.util.List;
import java.util.function.Function;

public class Rule implements IRule { //TODO: ADD DOCUMENTATION

    String myName;
    List<Function<IMove, Boolean>> myConditions;

    public Rule(String name, List<Function<IMove, Boolean>> conditions) {
        myName = name;
        myConditions = conditions;
    }

    public Rule(List<Function<IMove, Boolean>> conditions) {
        this("", conditions);
    }

    @Override
    public boolean checkValidMove(IMove move) {
        for (Function<IMove, Boolean> f: myConditions) {
            if (!f.apply(move)) {
                return false; //TODO: CHECK FAILURE CONDITION
            }
        }
        return true;
    }
}
