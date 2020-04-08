package ooga.controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ooga.data.IStyle;

import java.util.ResourceBundle;

public class Controller extends Application {

    private static final double WINDOW_WIDTH = 800;
    private static final double WINDOW_HEIGHT = 600;

    public static final String DEFAULT_RESOURCES_PACKAGE = Controller.class.getPackageName() + ".resources.";
    public static final String DEFAULT_LANGUAGE = "English";

    // temp
    private String defaultStyleXML;
    private FrontEnd myFrontEnd;

    private Stage myMainStage;
    private Pane myLayout;
    private ResourceBundle myResources;
    private TabPane myWorkspaces;
    private IStyle myStyle;
    private String myLanguage;
    private Scene myScene;

    public Controller() { super(); }

    @Override
    public void start(Stage mainStage) {

        myMainStage = mainStage;
        myStyle = readStyleXML(defaultStyleXML);
        myLanguage = myStyle.getLanguage();
        // TODO: Address possible language change
        initializeResouces(myLanguage);
        myFrontEnd = new FrontEnd(myStyle, myResources);
        myScene = FrontEnd.getScene();

        // Read resources
        // Call something else to read style
        // Create frontend (creates scene)
        // Pass in myResources
        // Call for scene to put on stage
        // Show the stage


        // Initialize the opening screen
        // Call the initialization of frontend
        // Give frontend style datapack
        // Listen for updates from frontend
    }

    private IStyle readStyleXML(String styleXML) {
        return null;
    }

    private void initializeResouces(String language) {

    }

}
