package ooga.cardtable;

public enum GameState implements IGameState {
  WIN("win"),
  LOSS("loss"),
  WAITING("waiting")
  ;

  private String state;

  GameState(String st){
    state = st;
  }
  @Override
  public String getState() {
    return state;
  }
}
