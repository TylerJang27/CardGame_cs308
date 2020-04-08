package ooga.data.rules;

import ooga.cardtable.ICell;
import ooga.cardtable.IGameState;

public interface ICardAction {

  ICell getStartCell();

  ICell getEndCell();

  IGameState execute();
}
