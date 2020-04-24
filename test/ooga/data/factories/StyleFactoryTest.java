package ooga.data.factories;

import ooga.data.style.IStyle;
import ooga.data.style.StyleData;


import java.io.File;
import java.util.Map;

import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class StyleFactoryTest {

    private static final String TEST_DIRECTORY = "test/ooga/data/factories/";

    @Test
    public void testCreateStyle() {
        System.out.println("help me");
        Map<String, String> wordSettings1 = Map.of("language", "english", "cards", "card_path", "table", "CS308");
        Map<String, Integer> numberSettings1 = Map.of("difficulty", 3, "sound", 1, "dark", 0);
        IStyle expectedStyle1 = new StyleData(TEST_DIRECTORY + "style_happy1.xml", wordSettings1, numberSettings1);

        IStyle actualStyle1a = StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy1.xml"));
        IStyle actualStyle1b= StyleFactory.createStyle(new File(TEST_DIRECTORY + "style_happy1.xml", TEST_DIRECTORY + "style_happy1.xml"));

        assertEquals(expectedStyle1, actualStyle1a);
        assertEquals(expectedStyle1, actualStyle1b);
    }
}
