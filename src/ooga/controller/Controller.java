package ooga.controller;

import javafx.application.Application;
import javafx.stage.Stage;
import ooga.cardtable.*;
import ooga.data.factories.PhaseMachineFactory;
import ooga.data.factories.LayoutFactory;
import ooga.data.rules.IPhaseMachine;
import ooga.data.style.IStyle;
import ooga.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Andrew Krier, Tyler Jang
 */
public class Controller extends Application {

    // TODO: Put the file here
    private static final String DEFAULT_STYLE_FILE = "data/default_style.xml";
    private static final String DEFAULT_RULE_FILE = "data/solitaire_rules.xml";

    private View myView;
    private IMove myCurrentMove;
    private ITable myTable;
    private IStyle myStyle;
    private File myStyleFile;
    private File myRuleFile;
    private File myLayoutFile;
    private IPhaseMachine myCurrentPhaseMachine;
    private Map<String, ICell> myCellMap;

    public Controller() { super(); }

    /**
     * Called at the beginning of the application
     * Initializes view and gives it handlers for notifying controller
     * and a style data class for boot up information
     * @param mainStage
     */
    @Override
    public void start(Stage mainStage) {
        myView = new View();
        initializeHandlers(myView);
        myStyleFile = new File(DEFAULT_STYLE_FILE);
        //myStyle = StyleFactory.getStyle(myStyleFile);
        myView.setStyle(myStyle);
    }

    //TODO: REPLACE WITH LOGIC REGARDING METHODS AT THE BOTTOM
    private void initializeHandlers(View v) {
        //input is string gamename
        v.listenForGameChoice((a,b,gameName) -> startTable(gameName));
    //() -> newMove());
        /*v.setHandlers((String game) -> createEngine(game), //Consumer
                (String rules) -> setHouseRules(rules), //Consumer
                (int diff) -> setDifficulty(diff), //Consumer
                (IMove move) -> processMove(move), //Function
                (String cell) -> getCell(cell)); //Function/Supplier
                */
        /*View.setHandlers(Consumer engineStart, Consumer ruleSet,  ....);
            myFunction = move;


        MOUSE.setOnClickAndDrag(event -> move.execute(new Move(event.getX, event.getY)));
            */
    }

    private void startTable(String gameName) {
        // TODO: process gamename string to a file path
        System.out.println(gameName);
        // TODO: Give game name somehow, figure out who's building the phase machine
        myRuleFile = new File(DEFAULT_RULE_FILE);
        myCurrentPhaseMachine = PhaseMachineFactory.getPhaseMachine(myRuleFile);
        myTable = new Table(myCurrentPhaseMachine);
        myCellMap = myTable.getCellData();
        myView.setCellData(new ArrayList(myCellMap.values()));
        File f = new File(myCurrentPhaseMachine.getSettings().getLayout());
        myView.setLayout(LayoutFactory.getLayout(f));
    }

    private void newMove() {
        myCurrentMove = getMove();
        myTable.update(myCurrentMove);
        //myView.setCellData(Map.copyOf(myTable.getCellData()));
    }

    private IMove getMove() {
        return myView.getUserInput();
    }

    private void createEngine(String gameName) {

    }

    private void setHouseRules(String ruleName) {

    }

    private void setDifficulty(int difficulty) {

    }

    private boolean isMove() {
        return myView.isUserInput();
    }

    private IGameState processMove(IMove move) {

        return null;
    }

    private ICell getCell(String cellName) {
        return null;
    }

    /**void setCellData(Map<String, ICell> cellData);
    void setScores(Map<Integer, Double> playerScores);
    void endGame(Map<Integer, Boolean> playerOutcomes, Map<Integer, Double> playerScores, Map<Integer, Integer> highScores);
    void playerStatusUpdate(Map<Integer, Boolean> playerOutcomes, Map<Integer, Integer> playerScores);
    boolean isUserInput();
    IMove getUserInput();
    void setStyle(Style style);
    void setLayout(Layout layout);
    **/

}
