package ooga.data.factories;

import ooga.data.XMLException;
import ooga.data.style.IStyle;
import ooga.data.style.StyleData;


import java.io.File;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StyleFactoryTest {

    private static final String TEST_DIRECTORY = "test/ooga/data/factories/";

    @Test
    public void testCreateStyle() {
        //original test
        Map<String, String> wordSettings1 = Map.of("language", "english", "cards", "card_path", "table", "CS308");
        Map<String, Integer> numberSettings1 = Map.of("difficulty", 3, "sound", 1, "dark", 0);
        IStyle expectedStyle1 = new StyleData(TEST_DIRECTORY + "style_happy1.xml", wordSettings1, numberSettings1);
        IStyle actualStyle1a = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy1.xml"));
        IStyle actualStyle1b= StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy1.xml"), TEST_DIRECTORY + "style_happy1.xml");

        //reordered test
        IStyle expectedStyle2 = expectedStyle1;
        IStyle actualStyle2a = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy2.xml"));
        IStyle actualStyle2b= StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy2.xml"), TEST_DIRECTORY + "style_happy2.xml");

        //missing element test
        Runnable actualStyle3 = ()-> {
            IStyle actualStyle3a = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_sad3.xml"));
            IStyle actualStyle3b = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_sad3.xml"), TEST_DIRECTORY + "style_sad3.xml");
        };

        //default/empty test
        Map<String, String> wordSettings4 = Map.of("language", "english", "cards", "", "table", "");
        Map<String, Integer> numberSettings4 = Map.of("difficulty", 0, "sound", 0, "dark", 0);
        IStyle expectedStyle4 = new StyleData(TEST_DIRECTORY + "style_happy4.xml", wordSettings4, numberSettings4);
        IStyle actualStyle4a = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy4.xml"));
        IStyle actualStyle4b = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy4.xml"), TEST_DIRECTORY + "style_happy4.xml");

        //missing number element test
        Runnable actualStyle5 = ()-> {
            IStyle actualStyle5a = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_sad5.xml"));
            IStyle actualStyle5b = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_sad5.xml"), TEST_DIRECTORY + "style_sad5.xml");
        };

        assertEquals(expectedStyle1, actualStyle1a);
        assertEquals(expectedStyle1, actualStyle1b);
        assertEquals(expectedStyle2, actualStyle2a);
        assertEquals(expectedStyle2, actualStyle2b);
        assertThrows(XMLException.class, ()->actualStyle3.run());
        assertEquals(expectedStyle4, actualStyle4a);
        assertEquals(expectedStyle4, actualStyle4b);
        assertThrows(XMLException.class, ()->actualStyle5.run());
    }
}
