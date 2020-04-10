package ooga;


import javafx.application.Application;
import javafx.stage.Stage;
import ooga.controller.Controller;
import ooga.data.LayoutFactory;
import ooga.data.StyleFactory;
import ooga.data.rules.ILayout;
import ooga.data.style.IStyle;

import java.io.File;
import ooga.view.View;

/**
 * Feel free to completely change this code or delete it entirely. 
 */

public class Main {
    /**
     * Start of the program.
     */
    public static void main (String[] args) {
        //testLayoutXML();
        Application.launch(Controller.class, args);
    }

    private static void testStyleXML() {
        File f = new File("data/default_style.xml");
        IStyle myStyle = StyleFactory.getStyle(f);
        System.out.println(myStyle.getCardSkinPath());
        System.out.println(myStyle.getLanguage());
        System.out.println(myStyle.getDifficulty());
        System.out.println(myStyle.getSound());
    }

    private static void testLayoutXML() {
        File f = new File("data/solitaire_layout.xml");
        ILayout layout = LayoutFactory.getLayout(f);

    }
}
