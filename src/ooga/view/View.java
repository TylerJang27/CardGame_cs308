package ooga.view;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ooga.cardtable.ICell;
import ooga.cardtable.IMove;
import ooga.controller.Controller;
import ooga.data.rules.ILayout;
import ooga.data.rules.Layout;
import ooga.data.style.IStyle;
import ooga.view.gamescreen.GameScreen;
import ooga.view.menu.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class View implements ExternalAPI {

    private static final String APPLICATION_NAME = "Solitaire Confinement";
    private static final String APPLICATION_ICON = "/ooga/resources/cards.png";

    private static final ResourceBundle LANGUAGES = ResourceBundle.getBundle("ooga.resources.languages.supportedlanguages");
    private static final ResourceBundle SKINS = ResourceBundle.getBundle("ooga.resources.skins.supportedskins");

    private static final String MESSAGES = "ooga.resources.languages.messages.";

    @FunctionalInterface
    public
    interface TriggerMove {
        public void giveIMove(IMove move);
    }

    @FunctionalInterface
    public interface ChangeValue {
        public void setValue(String newValue);
    }

    private TriggerMove getMove;
    private Runnable restarter;

    private String myTheme = "Duke";
    private String myLanguage = "English";

    private Stage myStage;
    private Menu myMenu;
    private GameScreen myGameScreen;

    private IStyle myStyle;

    private static final double DEFAULT_WIDTH = 650;
    private static final double DEFAULT_HEIGHT = 500;

    /**
     * Constructs an instance of View
     * @param giveMove lambda which will be called when a user makes a move on the game table
     * @param restart runnable which will be executed when a user hits restart on game table
     * @param style is updated to reflect user's language and theme preferences so they can be reloaded
     */
    public View(Controller.GiveMove giveMove, Runnable restart, IStyle style){
        restarter = restart;

        ChangeValue getTheme = (String theme) -> {
            myTheme = theme;
            myStyle.setTheme(theme);
        };

        ChangeValue getLanguage = (String language) -> {
            myLanguage = language;
            myStyle.setLanguage(language);
        };

        getMove = giveMove::sendMove;

        myStyle = style;

        if (myStyle.getTheme() != null) {
            myTheme = myStyle.getTheme();
        }
        if (myStyle.getLanguage() != null) {
            myLanguage = myStyle.getLanguage();
        }
        myMenu = new Menu(APPLICATION_NAME, LANGUAGES, SKINS, getTheme, getLanguage, myTheme, myLanguage, DEFAULT_HEIGHT, DEFAULT_WIDTH);

        myStage = new Stage();
        myStage.setScene(myMenu.getScene());
        myStage.getIcons().add(new Image(APPLICATION_ICON));
        myStage.setTitle(APPLICATION_NAME);
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

        ResourceBundle currentMessages = ResourceBundle.getBundle(MESSAGES+myLanguage);
        Button restartButton = new Button(currentMessages.getString("restart"));
        restartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                restarter.run();
                // System.out.println("View 114: Call lambda to notify backend");
            }
        });

        myGameScreen = new GameScreen(getMove, (Layout) layout, DEFAULT_WIDTH, myTheme, backButton, restartButton, myMenu.getGame(), currentMessages.getString("score"), myLanguage);
        myStage.setScene(myGameScreen.getScene());

        myStage.minHeightProperty().bind(Bindings.multiply(myGameScreen.getDisplayTable().getPane().widthProperty(),layout.getScreenRatio()));
        myStage.minWidthProperty().bind(Bindings.divide(myGameScreen.getDisplayTable().getPane().heightProperty(),layout.getScreenRatio()));

    }

    public void displayMessage(String key) {
        displayMessage(key, new ArrayList<>());
    }

    public void displayMessage(String key, List<String> args){
        // fixme im horribly inefficient
        System.out.println("reported");
        ResourceBundle currentMessages = ResourceBundle.getBundle(MESSAGES+myLanguage);
        String displayMessage = translateAndFormat(key, args, currentMessages);
        System.out.println(displayMessage);
        Text text = new Text(displayMessage);
        Pane messagePane = new Pane();
        HBox textHolder = new HBox();
        textHolder.getChildren().add(text);
        messagePane.getChildren().add(textHolder);
        Scene messageScene = new Scene(messagePane);
        Stage popUp = new Stage();
        popUp.setScene(messageScene);
        popUp.show();
    }

    /**
     * Translates a String key and its replacements args and formats them. If a translation or format is not found, the original language or key is displayed.
     *
     * @param key               the String containing the formatting elements
     * @param args              the List of Strings containing elements to insert
     * @param currentMessages   the ResourceBundle with which to translate
     * @return                  the properly translated and formatted String
     */
    private String translateAndFormat(String key, List<String> args, ResourceBundle currentMessages) {
        List<String> argsTranslated = new ArrayList<>();
        for (String s:args) {
            try {
                argsTranslated.add(currentMessages.getString(s));
            } catch (Exception e) {
                argsTranslated.add(s);
            }
        }
        String message = currentMessages.getString(key);
        String displayMessage = message;
        try {
            displayMessage=String.format(message, argsTranslated);
        } catch (Exception e) {
            displayMessage = message;
        }
        return displayMessage;
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

    public void reportError(String key, String... formats){
        //try translate
        //catch use the original arguments
        //pop up the errors
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
        myGameScreen.updateScore(playerScores.get(1));
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
