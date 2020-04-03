package ooga.rules;

import java.util.List;
import java.util.Map;
import ooga.cardtable.IMove;

/**
 * Gets Phase rules for each cell Gives Table cells with rules implemented Gives Table state machine
 * to process turns/winning/etc.
 */
public interface IPhase {

  boolean isAutomatic();

  IRule identifyMove(IMove move);

  List<IRule> getRuleList();

  Map<IRule, List<ICardAction>> getConditionalActions();

  List<ICardAction> getAutoActions();

  IGameState executeAutomaticActions();

}
