package ooga.data.rules;

import java.util.List;
import java.util.function.Consumer;
import ooga.cardtable.IMove;

/**
 * This class implements ICardAction and stores information about where ICells should move upon
 * processing an IMove.
 *
 * @author Tyler Jang
 */
public class CardAction implements ICardAction {

  List<Consumer<IMove>> myActions;

  /**
   * Constructor for CardAction, taking in a List of Consumers representing the updates to be
   * processed.
   *
   * @param actions a List of Consumers for IMoves
   */
  public CardAction(List<Consumer<IMove>> actions) {
    myActions = actions;
  }


  /**
   * Executes the CardAction, moving the ICells based on move.
   *
   * @param move an IMove to be processed into ICell movements.
   */
  @Override
  public void execute(IMove move) {
    for (Consumer<IMove> c : myActions) {
      c.accept(move);
    }
  }
}
