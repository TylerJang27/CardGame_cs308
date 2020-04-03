

public interface InternalAPI {

    /**
     * Once game has finished, displays end of game screen.
     */
    void displayEndScreen();

    /**
     * When game begins, displays startup screen.
     */
    void displayStartupScreen();

    /**
     * Updates boolean isUserInput to true and updates the Map which contains the most recent user
     * action. These values are then available to be called by the controller.
     * TODO: as explained in external api, Object class needs to be decided
     */
    void setUserInput(Object myClick, Object myRelease);

    /**
     * Updates map from player ID to player's display name with new input
     * @param playerID internally held ID of player
     * @param playername user name entered by player at startup
     */
    void setPlayerDisplayName(Integer playerID, String playername);

}

