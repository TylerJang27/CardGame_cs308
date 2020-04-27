package ooga.controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import ooga.cardtable.*;
import ooga.data.XMLException;
import ooga.data.factories.HighScoreFactory;
import ooga.data.factories.StyleFactory;
import ooga.data.highscore.HighScoreWriter;
import ooga.data.highscore.IHighScores;
import ooga.data.style.IStyle;
import ooga.data.style.StyleWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This Test class governs the appropriate tests to ensure appropriate behavior and no exceptions printed based on bug 11.
 * See doc/progress4/bug_report11.md for more details.
 * <p>
 * Uses TestFX helper methods from https://coursework.cs.duke.edu/compsci308_2020spring/example_testing/-/blob/master/test/util/DukeApplicationTest.java
 * <p>
 * Other start() methods taken from https://medium.com/@mglover/java-fx-testing-with-testfx-c3858b571320
 * <p>
 * ByteOutputStream and Console tests taken from https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
 *
 * @author Tyler Jang
 */
class ControllerTest extends ApplicationTest {

    @BeforeAll
    public static void setUpClass() {
        // explicitly use the most stable robot implementation to avoid some older versions
        //   https://stackoverflow.com/questions/52605298/simple-testfx-example-fails
        System.setProperty("testfx.robot", "glass");
    }

    @AfterEach
    public void tearDown() throws Exception {
        // remove stage of running app
        FxToolkit.cleanupStages();
        // clear any key or mouse presses left unreleased
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    //ALTERNATIVE HACKS
    public static void launchObject(Object app, String... appArgs) throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkitWrapper.setupApplication(app, appArgs);
    }

    /**
     * Launches the Controller-based application and tests whether an Exception is thrown.
     */
    @Test
    void start() {
        assertDoesNotThrow(() -> ApplicationTest.launch(Controller.class, ""));
    }

    /**
     * This method tests that a game is started and a move is attempted but declared invalid via a popup.
     *
     * @throws Exception if ControllerTest fails to launch
     */
    @Test
    void processMove() throws Exception {
        Controller c = new Controller();
        ControllerTest.launchObject(c, "");
        try {
            c.startTable("test");
            //NOTE: In order to enable this test to work as intended, the following change was made to Controller.attachView()
            //      the myTables.put(gameID, table) was moved to the beginning of the method. This has no functional changes, it simply changes the order
            //      of execution.
        } catch (NullPointerException e) {
            //view doesn't/shouldn't have access due to lambdas
        }
        assertThrows(IllegalStateException.class, () -> c.processMove(new Move(null, null, null), 0));
        //Testing this method fully is currently not possible using TestFx because the View establishes a relationship to each game.
        //These relationships lead to NullPointerExceptions that cannot proceed.
    }

    /**
     * Processes a score with the backend and checks to make sure that the high scores have been updated.
     *
     * @throws Exception if ControllerTest fails to launch
     */
    @Test
    void getAndUpdateScoreForGame() throws Exception {
        Controller c = new Controller();
        ControllerTest.launchObject(c, "");

        IHighScores expectedHigh = HighScoreFactory.createScores(new File("data/default_score_orig.xml"), "data/default_score.xml");

        IDeck deck = new Deck("full deck", buildDeckCards());
        ICell receiver = new Cell("a", new Deck());
        receiver.addCard(Offset.NONE, deck.getNextCard()); //KS
        ICell donor = new Cell("b", new Deck());
        donor.addCard(Offset.NONE, deck.getBottomCard()); //AC
        donor.addCard(Offset.SOUTH, deck.getNextCard()); //QS
        donor.addCard(Offset.SOUTH, deck.getNextCard()); //JS
        ICell mover = donor.getAllChildren().get(Offset.SOUTH); //QS, JS
        try {
            c.startTable("test");
            //NOTE: In order to enable this test to work as intended, the following change was made to Controller.attachView()
            //      the myTables.put(gameID, table) was moved to the beginning of the method. This has no functional changes, it simply changes the order
            //      of execution.
        } catch (NullPointerException e) {
            //view doesn't/shouldn't have access due to lambdas
        }
        assertDoesNotThrow(() -> c.getAndUpdateScoreForGame(new Move(donor, mover, receiver), 0));

        //tests that high scores can correctly be read from defaults
        //for additional testing see extractScores().
        assertEquals(expectedHigh.getScore("test").size(), 0);
    }

    /**
     * Tests that the style can successfully be generated from a file name (additional tests can be found in StyleTest.java).
     * Asserts that no exception is thrown during this process.
     * This test runs correctly contingent on there not being a data/default_style.xml file.
     */
    @Test
    void extractStyle() {
        IStyle base = StyleFactory.createStyle(new File("data/default_style_orig.xml"));
        StyleWriter.writeStyle("data/default_style.xml", base);
        Controller c = new Controller();
        IStyle[] extracted = {base};
        assertDoesNotThrow(() -> extracted[0] = c.extractStyle());
        IStyle extractedStyle = extracted[0];
        assertEquals(base.getSound(), extractedStyle.getSound());
        assertEquals(base.getDifficulty(), extractedStyle.getDifficulty());
        assertEquals(base.getDarkMode(), extractedStyle.getDarkMode());
        assertEquals(base.getCardSkinPath(), extractedStyle.getCardSkinPath());
        assertEquals(base.getTheme(), extractedStyle.getTheme());
        assertEquals(base.getLanguage(), extractedStyle.getLanguage());
    }

    /**
     * Tests that the high scores can be successfully generated from a file name (additional tests can be found in StyleTest.java).
     * Asserts taht no exception is thrown during this process.
     * This test runs correctly contingent on there not being a data/default_score.xml file.
     */
    @Test
    void extractScores() {
        IHighScores base = HighScoreFactory.createScores(new File("data/default_score_orig.xml"));
        HighScoreWriter.writeScores("data/default_score.xml", base);
        Controller c = new Controller();
        IHighScores[] extracted = {base};
        assertDoesNotThrow(() -> extracted[0] = c.extractScores());
        IHighScores extractScores = extracted[0];
        assertEquals(base.getScore("solitaire").size(), extractScores.getScore("solitaire").size());
        assertEquals(base.getScore("war").size(), extractScores.getScore("war").size());
        assertEquals(base.getScore("memory").size(), extractScores.getScore("memory").size());
    }

    /**
     * Tests that when a high score is submitted, it gets saved properly.
     *
     * @throws Exception if ControllerTest fails to launch
     */
    @Test
    void updateHighScores() throws Exception {
        Controller c = new Controller();
        ControllerTest.launchObject(c, "");

        assertDoesNotThrow(() -> HighScoreFactory.createScores(new File("data/default_score_orig.xml"), "data/default_score.xml"));
        c.updateHighScores("test", 365.0);
        IHighScores updatedHigh = HighScoreFactory.createScores(new File("data/default_score.xml"));
        assertEquals(((PriorityQueue<Double>) (updatedHigh.getScore("test"))).poll(), 365.0);
        assertEquals(updatedHigh.getScore("test2").size(), 0);
    }

    /**
     * Tests reportError for any error except an IllegalStateException caused by the creation of a popup.
     * Also tests that no stack trace is printed.
     *
     * @throws Exception if ControllerTest fails to launch
     */
    @Test
    void reportError() throws Exception {
        Controller c = new Controller();
        ControllerTest.launchObject(c, "");

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setErr(new java.io.PrintStream(out));

        assertThrows(IllegalStateException.class, () -> c.reportError(new XMLException("InvalidFile")));
        assertThrows(IllegalStateException.class, () -> c.reportError(new XMLException("bad error message")));
        //Checks that now stack trace is printed
        assertEquals(out.toString().length(), 0);
    }

    /**
     * Tests that additional games can load when they should. If an invalid game is loaded, the View attempts to create a popup, throwing
     * an IllegalStateException. If the file is correctly loaded, a NullPointerException will be thrown when the View attempts to attach the load.
     * For additional testing, see SaveConfigurationTest.java
     *
     * @throws Exception if ControllerTest fails to launch
     */
    @Test
    void loadGame() throws Exception {
        Controller c = new Controller();
        ControllerTest.launchObject(c, "");
        assertThrows(IllegalStateException.class, () -> c.loadGame("data/"));
        assertThrows(NullPointerException.class, () -> c.loadGame("data/saves/rps_save_1.xml"));
        assertThrows(IllegalStateException.class, () -> c.loadGame("data/solitaire/solitaire_rules.xml"));
    }

    /**
     * Creates a table based on two different files, one which is valid and one which is not.
     * Tests which errors should be thrown and which should be caught by valid/invalid files.
     */
    @Test
    void startTable() throws Exception {
        Controller c = new Controller();
        ControllerTest.launchObject(c, "");

        //NOTE: In order to enable this test to work as intended, the following change was made to Controller.attachView()
        //      the myTables.put(gameID, table) was moved to the beginning of the method. This has no functional changes, it simply changes the order
        //      of execution.

        //view doesn't/shouldn't have access due to lambdas
        assertThrows(NullPointerException.class, () -> c.startTable("test"));
        assertThrows(IllegalStateException.class, () -> c.startTable("invalidgamename"));
        assertThrows(NullPointerException.class, () -> c.getAndUpdateScoreForGame(new Move(null, null, null), 1));
    }

    /**
     * A helper method for use across testing that builds a full deck of 52 cards.
     *
     * @return a List of ICards containing the standard 52 deck.
     */
    private static List<ICard> buildDeckCards() {
        List<String> names = List.of("A", "2", "3", "4", "5", "6", "7", "8", "9", "0", "J", "Q", "K");
        List<String> vals = List
                .of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13");
        List<String> colors = List.of("black", "red", "red", "black");
        List<String> suits = List.of("c", "d", "h", "s");
        List<ICard> cards = new ArrayList<>();
        for (int k = 0; k < suits.size(); k++) {
            for (int j = 0; j < names.size(); j++) {
                IValue val = new Value(vals.get(j) + suits.get(k), Integer.parseInt(vals.get(j)));
                ISuit suit = new Suit(suits.get(k), new Color(colors.get(k)));
                ICard c = new Card(names.get(j) + suits.get(k).toUpperCase(), suit, val);
                c.flip();
                cards.add(c);
            }
        }
        return cards;
    }
}