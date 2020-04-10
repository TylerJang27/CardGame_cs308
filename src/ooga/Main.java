package ooga;


import javafx.application.Application;
import javafx.stage.Stage;
import ooga.controller.Controller;
import ooga.data.StyleFactory;
import ooga.data.style.IStyle;

import java.io.File;
import ooga.view.View;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main extends Application{

    private static void testStyleXML() {
        File f = new File("data/default_style.xml");
        IStyle myStyle = StyleFactory.getStyle(f);
        System.out.println(myStyle.getCardSkinPath());
        System.out.println(myStyle.getLanguage());
        System.out.println(myStyle.getDifficulty());
        System.out.println(myStyle.getSound());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        View view = new View();
    }
}
