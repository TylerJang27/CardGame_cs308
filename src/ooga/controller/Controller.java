package ooga.controller;

import javafx.application.Application;
import javafx.stage.Stage;

public class Controller extends Application {

    private Frontend myFrontEnd;

    public Controller() { super(); }

    @Override
    public void start(Stage mainStage) {

        myFrontEnd = new Frontend();
/*
        myMainStage = mainStage;
        myStyle = readStyleXML(defaultStyleXML);
        myLanguage = myStyle.getLanguage();
        // TODO: Address possible language change
        initializeResouces(myLanguage);
        myFrontEnd = new FrontEnd(myStyle, myResources);
        myScene = FrontEnd.getScene();

 */
    }
}
