package ooga;


import java.io.File;
import javafx.application.Application;
import ooga.controller.Controller;
import ooga.data.factories.StyleFactory;
import ooga.data.style.IStyle;

/**
 * Feel free to completely change this code or delete it entirely.
 */
public class Main {

  /**
   * Start of the program.
   */
  public static void main(String[] args) {
    //testLayoutXML();
    Application.launch(Controller.class, args);
  }

  private static void testStyleXML() {
    File f = new File("data/default_style.xml");
    IStyle myStyle = StyleFactory.createStyle(f);
    //System.out.println(myStyle.getCardSkinPath());
    //System.out.println(myStyle.getLanguage());
    //System.out.println(myStyle.getDifficulty());
    //System.out.println(myStyle.getSound());
  }

  private static void testLayoutXML() {
    File f = new File("data/colorsolitaire_layout.xml");
    //ILayout layout = LayoutFactory.getLayout(f);

  }
}
