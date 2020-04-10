package ooga.data.rules;

import ooga.cardtable.IMove;

import java.util.List;
import java.util.function.Consumer;

public class CardAction implements ICardAction{

    List<Consumer<IMove>> myActions;

    public CardAction(List<Consumer<IMove>> actions) {
        myActions = actions;
    }


    @Override
    public void execute(IMove move) {
        for (Consumer<IMove> c: myActions) {
            c.accept(move);
        }
    }
}
