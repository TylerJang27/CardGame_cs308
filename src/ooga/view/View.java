package ooga.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Consumer;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ooga.cardtable.ICell;
import ooga.cardtable.IMove;
import ooga.controller.Controller;
import ooga.controller.Controller.GiveMove;
import ooga.data.style.ILayout;
import ooga.data.style.IStyle;
import ooga.view.gamescreen.GameScreen;
import ooga.view.highscores.HighScoresDisplay;
import ooga.view.highscores.HighScoresManager;
import ooga.view.menu.Dictionary;
import ooga.view.menu.Menu;

public class View implements ExternalAPI {

  private static final String APPLICATION_NAME = "Solitaire Confinement";
  private static final String APPLICATION_ICON = "/ooga/resources/cards.png";
  private static final ResourceBundle LANGUAGES = ResourceBundle
      .getBundle("ooga.resources.languages.supportedlanguages");
  private static final ResourceBundle SKINS = ResourceBundle
      .getBundle("ooga.resources.skins.supportedskins");
  private static final String MESSAGES = "ooga.resources.languages.messages.";
  private static final String DEFAULT_THEME = "Duke";
  private static final String DEFAULT_LANGUAGE = "English";
  private static final String MESSAGES_DICTIONARY_PATH = "ooga.resources.languages.messages";
  private static final String SCORE = "score";
  public static final String RESTART = "restart";
  public static final String MENU = "Menu";
  public static final String HIGH_SCORES = "High_Scores";
  public static final String SKINS_PACKAGE = "/ooga/resources/skins/";
  public static final String MAINMENU_CSS = "/mainmenu.css";
  public static final int FIRST = 1;
  private static final List<String> GAME_CSS = List.of("tab");

  @FunctionalInterface
  public interface SaveGame {

    void saveGame(int gameID, String fname);
  }

  @FunctionalInterface
  public interface ChangeValue {

    void setValue(String newValue);
  }

  private GiveMove getMove;
  private Restart restarter;

  private String myTheme;
  private String myLanguage;

  private Scene myScene;
  private Menu myMenu;
  private HighScoresManager myHighScoresManager;

  private IStyle myStyle;
  private int myGameIndex;
  private TabPane myTabPane;
  private Map<Integer, GameScreen> myGameIdToGame;
  private SaveGame mySaveGame;
  private Map<Integer, String> myGameIdToGameName;
  private Set<Tab> myGameTabs;
  private Stage myStage;
  private Map<Tab, Double> myTabToRatio;

  private static final double DEFAULT_WIDTH = 650;
  private static final double DEFAULT_HEIGHT = 500;

  @FunctionalInterface
  public
  interface Restart {

    void execute(int gameID);
  }

  /**
   * Constructs an instance of View
   *
   * @param giveMove lambda which will be called when a user makes a move on the game table
   * @param restart  runnable which will be executed when a user hits restart on game table
   * @param style    is updated to reflect user's language and theme preferences so they can be
   *                 reloaded
   */
  public View(Controller.GiveMove giveMove, Restart restart, IStyle style, SaveGame gameSave,
      Consumer<String> gameLoad) {
    myStage = new Stage();
    myTheme = DEFAULT_THEME;
    myTabToRatio = new HashMap<>();
    myLanguage = DEFAULT_LANGUAGE;
    myGameTabs = new HashSet<>();
    myGameIdToGameName = new HashMap<>();

    Dictionary.getInstance().addReference(MESSAGES_DICTIONARY_PATH);
    mySaveGame = gameSave;
    myHighScoresManager = new HighScoresManager();
    myGameIdToGame = new HashMap<>();
    myGameIndex = 0;

    restarter = restart;
    getMove = giveMove;
    getStyle(style);

    EventHandler<MouseEvent> handler = getHighScoreHandler();

    myMenu = new Menu(APPLICATION_NAME, LANGUAGES, SKINS, getThemeLambda(), getLanguageLambda(),
        myTheme, myLanguage, DEFAULT_HEIGHT, DEFAULT_WIDTH, handler, gameLoad);

    makeTabPane();
    displayScene();
  }

  /**
   * Sets the locations of all cell types and the framework for creating new cell locations if
   * applicable.
   *
   * @param layout the layout of the game
   * @param gameID the integer corresponding to the id of the game
   */
  @Override
  public void setLayout(int gameID, ILayout layout) {
    Button restartButton = makeRestartButton(gameID);

    GameScreen gameScreen = new GameScreen(gameID, getMove, layout, DEFAULT_WIDTH, myTheme,
        restartButton, myMenu.getGame(), Dictionary.getInstance().get(SCORE).getValue(), myLanguage,
        mySaveGame);
    myGameIdToGame.put(gameID, gameScreen);

    Tab newTab = new Tab();
    newTab.getStyleClass().addAll(GAME_CSS);
    newTab.textProperty()
        .bind(Dictionary.getInstance().get(myGameIdToGameName.get(gameID).toLowerCase()));
    newTab.setContent(gameScreen.getNode());
    myGameTabs.add(newTab);
    myTabToRatio.put(newTab, layout.getScreenRatio());
    myTabPane.getTabs().add(newTab);
    myTabPane.getSelectionModel().select(newTab);
    //myStage.minHeightProperty().bind(Bindings.multiply(myGameScreen.getDisplayTable().getPane().widthProperty(),layout.getScreenRatio()));
    //myStage.minWidthProperty().bind(Bindings.divide(myGameScreen.getDisplayTable().getPane().heightProperty(),layout.getScreenRatio()));

  }

  private Button makeRestartButton(int gameID) {
    Button restartButton = new Button();
    restartButton.textProperty().bind(Dictionary.getInstance().get(RESTART));
    restartButton.setOnAction(e -> restarter.execute(gameID));
    return restartButton;
  }

  public void displayMessage(int gameID, String key) {
    displayMessage(gameID, key, new ArrayList<>());
  }

  public void displayMessage(int gameID, String key, List<String> args) {
    ResourceBundle currentMessages = ResourceBundle.getBundle(MESSAGES + myLanguage);
    String displayMessage = translateAndFormat(key, args, currentMessages);
    myGameIdToGame.get(gameID).displayMessage(displayMessage);
  }

  /**
   * Translates a String key and its replacements args and formats them. If a translation or format
   * is not found, the original language or key is displayed.
   *
   * @param key             the String containing the formatting elements
   * @param args            the List of Strings containing elements to insert
   * @param currentMessages the ResourceBundle with which to translate
   * @return the properly translated and formatted String
   */
  private String translateAndFormat(String key, List<String> args, ResourceBundle currentMessages) {
    List<String> argsTranslated = new ArrayList<>();
    for (String s : args) {
      try {
        argsTranslated.add(currentMessages.getString(s));
      } catch (Exception e) {
        argsTranslated.add(s);
      }
    }
    String message = currentMessages.getString(key);
    String displayMessage;
    try {
      displayMessage = String.format(message, argsTranslated);
    } catch (Exception e) {
      displayMessage = message;
    }
    return displayMessage;
  }

  public void updateHighScores(String gameType, Collection<Double> scores) {
    myHighScoresManager.addScore(gameType, scores);
  }

  /**
   * setCellData() is called regularly by the Controller to pass the correct state of the board to
   * the front end from the back end. This is done by sending a list of cell objects which represent
   * groups of cards and their associated state (i.e. face up/down, staggered/even, card type)
   *
   * @param cellData the cell data to update
   * @param gameID   the integer corresponding to the id of the game
   */
  @Override
  public void setCellData(int gameID, Map<String, ICell> cellData) {
    myGameIdToGame.get(gameID).initializeTable(cellData);
  }


  @Override
  public void setUpdatesToCellData(int gameID, Map<String, ICell> cellData) {
    myGameIdToGame.get(gameID).updateTable(cellData);
  }


  public void listenForGameChoice(ChangeListener<String> listener) {
    myMenu.addChosenHandler(listener);
  }


  /**
   * Returns a boolean indicating whether a user has made a change since the Controller's last call
   * to getUserInput().
   */
  @Override
  public boolean isUserInput() {
    return false;
  }

  /**
   * getUserInput() is called regularly by the Controller to obtain the new move made by any
   * player.
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
   * report an error where the key is the key to a error message in a properties file and the
   * formats are the error-specific information
   *
   * @param key     the key corresponding to the error to be reported
   * @param formats
   */
  @Override
  public void reportError(String key, List<String> formats) {
    ResourceBundle currentMessages = ResourceBundle.getBundle(MESSAGES + myLanguage);
    String displayMessage = translateAndFormat(key, formats, currentMessages);
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
   * Sets score of players to be displayed
   *
   * @param playerScores maps playerID to total score
   */
  @Override
  public void setScores(int gameID, Map<Integer, Double> playerScores) {
    myGameIdToGame.get(gameID).updateScore(playerScores.get(FIRST));
  }


  /**
   * If triggered by player move, please call setCellData() first so that the most recent
   * arrangement of cards can be displayed prior to the game over screen. This function ends the
   * game, progressing to a "high score" or some other end game screen.
   *
   * @param playerOutcomes maps a player integer to their status at the end of a game
   * @param playerScores
   * @param highScores
   */
  @Override
  public void endGame(Map<Integer, Boolean> playerOutcomes, Map<Integer, Double> playerScores,
      Map<Integer, Integer> highScores) {
    // idk
  }

  /**
   * Similar to endGame, but rather than ending game removes a player from the game, with a message
   * indicating the updated status of that player. For example, in UNO a player may win while other
   * players continue on. This function allows the controller to signal that a player, player i, has
   * won the game, but the frontend will not return to the start menu or display high scores, but
   * rather continue operating as it had.
   *
   * @param playerOutcomes maps a player's ID number to their Boolean win/lose (true/false) status.
   *                       If a player's status is unchanged, do not include these player's ID
   *                       numbers in playerOutcomes. Only include the player's who have either won
   *                       or lost before the game is ended.
   * @param playerScores   maps a player's ID number to their Double score. If scoring is not
   *                       enabled for the current game,
   */
  @Override
  public void playerStatusUpdate(Map<Integer, Boolean> playerOutcomes,
      Map<Integer, Integer> playerScores) {

  }

  private void displayScene() {
    myScene = new Scene(myTabPane);
    myScene.getStylesheets().add(
        getClass().getResource(SKINS_PACKAGE + myTheme.toLowerCase() + MAINMENU_CSS)
            .toExternalForm()); //
    myStage.setScene(myScene);
    myStage.getIcons().add(new Image(APPLICATION_ICON));
    myStage.setTitle(APPLICATION_NAME);
    myStage.show();
  }

  private void makeTabPane() {
    myTabPane = new TabPane();
    Tab menuTab = new Tab();
    menuTab.getStyleClass().addAll(GAME_CSS);
    menuTab.textProperty().bind(Dictionary.getInstance().get(MENU));
    menuTab.setContent(myMenu.getScene());
    myTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
      @Override
      public void changed(ObservableValue<? extends Tab> observable, Tab oldValue,
          Tab newValue) {
        if (myGameTabs.contains(newValue)) {
          myStage.minHeightProperty().unbind();
          double ratio = myTabToRatio.get(newValue);
          myStage.setHeight(ratio * myStage.getWidth());
          myStage.minHeightProperty().bind(Bindings.multiply(ratio, myStage.widthProperty()));
        } else {
          myStage.minHeightProperty().unbind();
          myStage.setHeight(myStage.getWidth());
          myStage.minHeightProperty().bind(myStage.widthProperty());
        }
      }
    });

    myTabPane.getTabs().add(menuTab);
  }

  private EventHandler<MouseEvent> getHighScoreHandler() {
    return event -> {
      Tab highScoresTab = new Tab();
      highScoresTab.getStyleClass().addAll(GAME_CSS);
      highScoresTab.textProperty().bind(Dictionary.getInstance().get(HIGH_SCORES));
      highScoresTab.setContent(new HighScoresDisplay(myHighScoresManager).getNode());
      myTabPane.getTabs().add(highScoresTab);
    };
  }

  private void getStyle(IStyle style) {
    myStyle = style;
    if (myStyle.getTheme() != null) {
      myTheme = myStyle.getTheme();
    }
    if (myStyle.getLanguage() != null) {
      myLanguage = myStyle.getLanguage();
    }
  }

  private ChangeValue getLanguageLambda() {
    return (String language) -> {
      myLanguage = language;
      myStyle.setLanguage(language);
    };
  }

  private ChangeValue getThemeLambda() {
    return (String theme) -> {
      myTheme = theme;
      myStyle.setTheme(theme);
      myScene.getStylesheets().clear();
      myScene.getStylesheets().add(getClass().getResource(
          SKINS_PACKAGE + theme.toLowerCase() + MAINMENU_CSS).toExternalForm());
    };
  }

  public int createGame(String gameName) {
    myGameIdToGameName.put(myGameIndex, gameName);
    return myGameIndex++;
  }

}
