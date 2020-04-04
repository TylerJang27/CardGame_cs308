package ooga.rules;

public interface IGameState {
  //may be an enum
  //possible states include win, loss, continue, waiting for user input, invalid move

  /**
   * Retrieves the current String state as a result of a Move and its processing
   *
   * @return the current state
   */
  String getState();

}
