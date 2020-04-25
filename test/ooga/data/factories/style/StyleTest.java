package ooga.data.factories.style;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.Map;
import ooga.data.XMLException;
import ooga.data.factories.StyleFactory;
import ooga.data.style.IStyle;
import ooga.data.style.StyleData;
import org.junit.jupiter.api.Test;

/**
 * This class tests the IStyle interface as it is used with StyleData, StyleFactory, and
 * StyleWriter. This class ensures that appropriate exceptions are thrown, that equivalent IStyle
 * implementations are equal, and that writing and reading from XML files produce equivalent
 * results.
 *
 * @author Tyler Jang
 */
public class StyleTest {

  private static final String TEST_DIRECTORY = "test/ooga/data/factories/style/";

  /**
   * Tests the createStyle() method in StyleFactory to ensure that IStyle implementations are
   * created correctly.
   */
  @Test
  public void testCreateStyle() {
    //original test
    Map<String, String> wordSettings1 = Map
        .of("language", "english", "cards", "card_path", "table", "CS308");
    Map<String, Integer> numberSettings1 = Map.of("difficulty", 3, "sound", 1, "dark", 0);
    IStyle expectedStyle1 = new StyleData(TEST_DIRECTORY + "style_happy1.xml", wordSettings1,
        numberSettings1);
    IStyle actualStyle1a = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy1.xml"));
    IStyle actualStyle1b = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy1.xml"),
        TEST_DIRECTORY + "style_happy1.xml");

    //reordered test
    IStyle expectedStyle2 = expectedStyle1;
    IStyle actualStyle2a = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy2.xml"));
    IStyle actualStyle2b = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy2.xml"),
        TEST_DIRECTORY + "style_happy2.xml");

    //missing element test
    Runnable actualStyle3 = () -> {
      IStyle actualStyle3a = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_sad3.xml"));
      IStyle actualStyle3b = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_sad3.xml"),
          TEST_DIRECTORY + "style_sad3.xml");
    };

    //default/empty test
    Map<String, String> wordSettings4 = Map.of("language", "english", "cards", "", "table", "");
    Map<String, Integer> numberSettings4 = Map.of("difficulty", 0, "sound", 0, "dark", 0);
    IStyle expectedStyle4 = new StyleData(TEST_DIRECTORY + "style_happy4.xml", wordSettings4,
        numberSettings4);
    IStyle actualStyle4a = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy4.xml"));
    IStyle actualStyle4b = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy4.xml"),
        TEST_DIRECTORY + "style_happy4.xml");

    //missing number element test
    Runnable actualStyle5 = () -> {
      IStyle actualStyle5a = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_sad5.xml"));
      IStyle actualStyle5b = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_sad5.xml"),
          TEST_DIRECTORY + "style_sad5.xml");
    };

    assertEquals(expectedStyle1, actualStyle1a);
    assertEquals(expectedStyle1, actualStyle1b);
    assertEquals(expectedStyle2, actualStyle2a);
    assertEquals(expectedStyle2, actualStyle2b);
    assertThrows(XMLException.class, () -> actualStyle3.run());
    assertEquals(expectedStyle4, actualStyle4a);
    assertEquals(expectedStyle4, actualStyle4b);
    assertThrows(XMLException.class, () -> actualStyle5.run());
  }

  /**
   * Tests that the accessors of IStyle produce the correct outputs after writing to an XML file.
   */
  @Test
  public void testStyleAccessor() {
    Map<String, String> wordSettings1 = Map
        .of("language", "english", "cards", "card_path", "table", "CS308");
    Map<String, Integer> numberSettings1 = Map.of("difficulty", 3, "sound", 1, "dark", 0);

    IStyle expectedStyle1 = new StyleData(TEST_DIRECTORY + "style_happy1.xml", wordSettings1,
        numberSettings1);
    IStyle actualStyle1a = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy1.xml"));
    IStyle actualStyle1b = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy1.xml"),
        TEST_DIRECTORY + "style_happy1.xml");

    //reordered test
    IStyle actualStyle2a = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy2.xml"));
    IStyle actualStyle2b = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy2.xml"),
        TEST_DIRECTORY + "style_happy2.xml");

    assertEquals(expectedStyle1.getLanguage(), "english");
    assertEquals(expectedStyle1.getTheme(), "CS308");
    assertEquals(expectedStyle1.getCardSkinPath(), "card_path");
    assertEquals(expectedStyle1.getDarkMode(), false);
    assertEquals(expectedStyle1.getDifficulty(), 3);
    assertEquals(expectedStyle1.getSound(), true);

    assertEquals(actualStyle1a.getLanguage(), "english");
    assertEquals(actualStyle1a.getTheme(), "CS308");
    assertEquals(actualStyle1a.getCardSkinPath(), "card_path");
    assertEquals(actualStyle1a.getDarkMode(), false);
    assertEquals(actualStyle1a.getDifficulty(), 3);
    assertEquals(actualStyle1a.getSound(), true);

    assertEquals(actualStyle1b.getLanguage(), "english");
    assertEquals(actualStyle1b.getTheme(), "CS308");
    assertEquals(actualStyle1b.getCardSkinPath(), "card_path");
    assertEquals(actualStyle1b.getDarkMode(), false);
    assertEquals(actualStyle1b.getDifficulty(), 3);
    assertEquals(actualStyle1b.getSound(), true);

    assertEquals(actualStyle2a.getLanguage(), "english");
    assertEquals(actualStyle2a.getTheme(), "CS308");
    assertEquals(actualStyle2a.getCardSkinPath(), "card_path");
    assertEquals(actualStyle2a.getDarkMode(), false);
    assertEquals(actualStyle2a.getDifficulty(), 3);
    assertEquals(actualStyle2a.getSound(), true);

    assertEquals(actualStyle2b.getLanguage(), "english");
    assertEquals(actualStyle2b.getTheme(), "CS308");
    assertEquals(actualStyle2b.getCardSkinPath(), "card_path");
    assertEquals(actualStyle2b.getDarkMode(), false);
    assertEquals(actualStyle2b.getDifficulty(), 3);
    assertEquals(actualStyle2b.getSound(), true);
  }

  /**
   * Tests that StyleWriter works correctly and produces the correct XML that can then be read to
   * produce an equivalent result.
   */
  @Test
  public void testStyleWriter() {
    Map<String, String> wordSettings1 = Map
        .of("language", "english", "cards", "card_path", "table", "CS308");
    Map<String, Integer> numberSettings1 = Map.of("difficulty", 3, "sound", 1, "dark", 0);
    IStyle expectedStyle1 = new StyleData(TEST_DIRECTORY + "style_written1.xml", wordSettings1,
        numberSettings1);

    Map<String, String> wordSettings4 = Map.of("language", "english", "cards", "", "table", "");
    Map<String, Integer> numberSettings4 = Map.of("difficulty", 0, "sound", 0, "dark", 0);
    IStyle expectedStyle4 = new StyleData(TEST_DIRECTORY + "style_written2.xml", wordSettings4,
        numberSettings4);
    IStyle actualStyle6 = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy4.xml"),
        TEST_DIRECTORY + "style_written3.xml");

    expectedStyle1.saveSettings();
    expectedStyle4.saveSettings();
    actualStyle6.saveSettings();

    IStyle writtenStyle1 = StyleFactory
        .createStyle(new File(TEST_DIRECTORY + "style_written1.xml"));
    IStyle writtenStyle2 = StyleFactory
        .createStyle(new File(TEST_DIRECTORY + "style_written2.xml"));
    IStyle writtenStyle3 = StyleFactory
        .createStyle(new File(TEST_DIRECTORY + "style_written3.xml"));

    assertEquals(expectedStyle1, writtenStyle1);
    assertEquals(expectedStyle4, writtenStyle2);
    assertEquals(actualStyle6, writtenStyle3);
    assertNotEquals(writtenStyle1, writtenStyle2);
    assertNotEquals(writtenStyle1, writtenStyle3);
    assertEquals(writtenStyle2, writtenStyle3);
  }
}
