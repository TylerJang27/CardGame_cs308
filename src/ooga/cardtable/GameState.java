package ooga.cardtable;

public enum GameState implements IGameState {
  WIN("win"),
  LOSS("loss"),
  WAITING("waiting"),
  INVALID("invalid");

  private String state;

  GameState(String st) {
    state = st;
  }

  @Override
  public String getState() {
    return state;
  }
}
