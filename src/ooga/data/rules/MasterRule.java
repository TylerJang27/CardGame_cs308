package ooga.data.rules;

import ooga.cardtable.IGameState;
import ooga.cardtable.IMove;
import ooga.cardtable.IPlayer;

import java.util.List;

public class MasterRule implements IMasterRule {

    private List<IRule> myRules;
    private List<IRule> myAutoRules;
    private List<ICardAction> myCardActions;
    private List<IControlAction> myControlActions;

    public MasterRule(List<IRule> rules, List<IRule> autoRules, List<ICardAction> cardActions, List<IControlAction> controlActions) {
        myRules = rules;
        myAutoRules = autoRules;
        myCardActions = cardActions;
        myControlActions = controlActions;
    }

    @Override
    public IGameState executeMove(IMove move) {
        if (checkValidMove(move)) {
            for (ICardAction action: myCardActions) {
                action.execute(move);
            }
        }
    }

    @Override
    public IPhaseArrow executeAutoActions(IPlayer player) {
        for (IControlAction action: myControlActions) {
            action.execute(player);
        }
    }

    @Override
    public boolean checkValidMove(IMove move) {
        boolean flag = true;
        for (IRule rule: myRules) {
            if (!rule.checkValidMove(move)) {
                return false;
            }
        }
        return flag;
    }
}
