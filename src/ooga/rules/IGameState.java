package ooga.rules;

public interface IGameState {
  //may be an enum
  //possible states include win, loss, continue, waiting for user input, invalid move

  String getState();

}
