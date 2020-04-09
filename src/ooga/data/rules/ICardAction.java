package ooga.data.rules;

import java.util.List;
import ooga.cardtable.ICell;
import ooga.cardtable.IGameState;

public interface ICardAction {

  ICell getStartCell();

  ICell getEndCell();

  IGameState execute(List<ICell> cells);
}
