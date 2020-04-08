package ooga.controller;

import javafx.application.Application;
import javafx.stage.Stage;
import ooga.cardtable.ICell;
import ooga.cardtable.IGameState;
import ooga.cardtable.IMove;
import ooga.view.View;

import java.util.Map;

public class Controller extends Application {

    private View myView;

    public Controller() { super(); }

    @Override
    public void start(Stage mainStage) {
        myView = new View();
        initializeHandlers(myView);
    }

    private void initializeHandlers(View v) {
        v.setHandlers((String game) -> createEngine(game), (String rules) -> setHouseRules(rules),
        (int diff) -> setDifficulty(diff), (IMove move) -> processMove(move), (String cell) -> getCell(cell));
    }

    private void createEngine(String gameName) {

    }

    private void setHouseRules(String ruleName) {

    }

    private void setDifficulty(int difficulty) {

    }

    private IGameState processMove(IMove move) {

    }

    private ICell getCell(String cellName) {

    }

    void setCellData(Map<String, ICell> cellData);
    void setScores(Map<Integer, Double> playerScores);
    void endGame(Map<Integer, Boolean> playerOutcomes, Map<Integer, Double> playerScores, Map<Integer, Integer> highScores);
    void playerStatusUpdate(Map<Integer, Boolean> playerOutcomes, Map<Integer, Integer> playerScores);
    boolean isUserInput();
    IMove getUserInput();
    void setStyle(Style style);
    void setLayout(Layout layout);



}
