package ooga.data.rules;


import ooga.cardtable.IGameState;
import ooga.cardtable.IMove;

public interface ICardAction { //TODO: UPDATE API CHANGES

  void execute(IMove move);
}
