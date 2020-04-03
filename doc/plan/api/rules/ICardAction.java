package ooga.rules;

import ooga.cardtable.ICell;

public interface ICardAction {

  ICell getStartCell();

  ICell getEndCell();

  IGameState execute();
}
