package ooga.controller;

import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This Test class governs the appropriate tests to ensure appropriate behavior and no exceptions printed based on bug 11.
 * See doc/progress4/bug_report11.md for more details.
 *
 * @author Tyler Jang
 */
class ControllerTest {

    /**
     * Launches the Controller-based application and tests whether an Exception is thrown.
     */
    @Test
    void start() {
        assertDoesNotThrow(() -> Application.launch(Controller.class, ""));
    }

    @Test
    void processMove() {
    }

    @Test
    void getAndUpdateScoreForGame() {
    }

    @Test
    void extractStyle() {
    }

    @Test
    void extractScores() {
    }

    @Test
    void updateHighScores() {
    }

    @Test
    void reportError() {
    }

    @Test
    void loadGame() {
    }

    @Test
    void startTable() {
    }
}