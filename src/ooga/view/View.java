package ooga.view;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ooga.cardtable.ICell;
import ooga.cardtable.IMove;
import ooga.controller.Controller;
import ooga.data.rules.ILayout;
import ooga.data.rules.Layout;
import ooga.data.style.IStyle;
import ooga.view.gamescreen.GameScreen;
import ooga.view.menu.Menu;

import java.util.Map;

public class View implements ExternalAPI {

    @FunctionalInterface
    public
    interface TriggerMove {
        public void giveIMove(IMove move);
    }

    @FunctionalInterface
    public interface ChangeTheme {
        public void setTheme(String theme);
    }

    private TriggerMove getMove;

    private String myTheme = "Duke"; // fixme decide on a default and implement

    private Stage myStage;
    private Menu myMenu;
    private GameScreen myGameScreen;

    private static final double DEFAULT_WIDTH = 650;
    private static final double DEFAULT_HEIGHT = 500;

    public View(Controller.GiveMove giveMove){

        ChangeTheme getTheme = (String theme) -> {
            myTheme = theme;
        };

        getMove = giveMove::sendMove;

        myMenu = new Menu(getTheme, myTheme, DEFAULT_HEIGHT, DEFAULT_WIDTH);

        myStage = new Stage();
        myStage.setScene(myMenu.getScene());
        myStage.show();
    }

    /**
     * Sets the locations of all cell types and the framework for creating new cell locations if applicable.
     *
     * @param layout
     */
    @Override
    public void setLayout(ILayout layout) {

        Button backButton = new Button();
        backButton.setGraphic(new ImageView(new Image("/ooga/resources/backarrow.png", 20, 20, false, false)));
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                myStage.setScene(myMenu.getScene());
            }
        });

        myGameScreen = new GameScreen(getMove, (Layout) layout, DEFAULT_WIDTH, myTheme, backButton);
        myStage.setScene(myGameScreen.getScene());

        myStage.minHeightProperty().bind(Bindings.multiply(myGameScreen.getDisplayTable().getPane().widthProperty(),layout.getScreenRatio()));
        myStage.minWidthProperty().bind(Bindings.divide(myGameScreen.getDisplayTable().getPane().heightProperty(),layout.getScreenRatio()));

    }


    /**
     * setCellData() is called regularly by the Controller to pass the correct state of the board
     * to the front end from the back end. This is done by sending a list of cell objects which
     * represent groups of cards and their associated state (i.e. face up/down, staggered/even, card type)
     *
     * @param cellData
     */
    @Override
    public void setCellData(Map<String,ICell> cellData) {
        myGameScreen.initializeTable(cellData);
    }


    @Override
    public void setUpdatesToCellData(Map<String,ICell> cellData) {
        myGameScreen.updateTable(cellData);
    }


    public void listenForGameChoice(ChangeListener<String> listener){
        myMenu.addChosenHandler(listener);
    }




    /**
     * Returns a boolean indicating whether a user has made a change since the Controller's last
     * call to getUserInput().
     */
    @Override
    public boolean isUserInput() {
        return false;
    }

    /**
     * getUserInput() is called regularly by the Controller to obtain the new move made by
     * any player.
     * <p>
     * Sets isUserInput() to false, pending a new move from any player.
     *
     * @return a map from the clicked on object to the released on object of the user's action
     */
    @Override
    public IMove getUserInput() {
        return null;
    }

    /**
     * Sets the style of the game, including color of table, location of menu/its display elements,
     * font type, font size, text colors, margins, etc.
     *
     * @param style
     */
    @Override
    public void setStyle(IStyle style) {
    }

    public void reportError(String key, String... formats){
        //TODO
        System.out.println("error received of type: " + key);
    }


    /**
     * Sets score of players to be displayed
     *
     * @param playerScores maps playerID to total score
     */
    @Override
    public void setScores(Map<Integer, Double> playerScores) {

    }


    /**
     * If triggered by player move, please call setCellData() first so that the most recent arrangement
     * of cards can be displayed prior to the game over screen. This function ends the game, progressing to
     * a "high score" or some other end game screen.
     *
     * @param playerOutcomes maps a player integer to their status at the end of a game
     * @param playerScores
     * @param highScores
     */
    @Override
    public void endGame(Map<Integer, Boolean> playerOutcomes, Map<Integer, Double> playerScores, Map<Integer, Integer> highScores) {
        // idk
    }

    /**
     * Similar to endGame, but rather than ending game removes a player from the game, with a message indicating
     * the updated status of that player. For example, in UNO a player may win while other players continue on.
     * This function allows the controller to signal that a player, player i, has won the game, but the frontend
     * will not return to the start menu or display high scores, but rather continue operating as it had.
     *
     * @param playerOutcomes maps a player's ID number to their Boolean win/lose (true/false) status.
     *                       If a player's status is unchanged, do not include these player's ID numbers in playerOutcomes.
     *                       Only include the player's who have either won or lost before the game is ended.
     * @param playerScores   maps a player's ID number to their Double score. If scoring is not enabled for the current game,
     */
    @Override
    public void playerStatusUpdate(Map<Integer, Boolean> playerOutcomes, Map<Integer, Integer> playerScores) {

    }

}
