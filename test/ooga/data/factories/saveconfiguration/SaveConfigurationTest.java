package ooga.data.factories.saveconfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ooga.cardtable.Card;
import ooga.cardtable.Cell;
import ooga.cardtable.Color;
import ooga.cardtable.Deck;
import ooga.cardtable.ICard;
import ooga.cardtable.ICell;
import ooga.cardtable.IDeck;
import ooga.cardtable.IOffset;
import ooga.cardtable.ISuit;
import ooga.cardtable.IValue;
import ooga.cardtable.Offset;
import ooga.cardtable.Suit;
import ooga.cardtable.Value;
import ooga.data.factories.SaveConfigurationFactory;
import ooga.data.saveconfiguration.ISaveConfiguration;
import ooga.data.saveconfiguration.SaveConfiguration;
import ooga.data.saveconfiguration.SaveConfigurationWriter;
import org.junit.jupiter.api.Test;

/**
 * This tests the implementation of ISaveConfiguration as it pertains to reading and writing. This
 * does not cover its interactions with ITable and IPhaseMachine.
 *
 * @author Tyler Jang
 */
class SaveConfigurationTest {

  private static final String TEST_DIRECTORY = "test/ooga/data/factories/saveconfiguration/";

  /**
   * Tests that a save and a load produces the same cell map and the same basic fields.
   */
  @Test
  void createSave() {
    List<String> names = List.of("A", "2", "3", "4", "5", "6", "7", "8", "9", "0", "J", "Q", "K");
    List<String> vals = List
        .of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13");
    List<String> colors = List.of("black", "red", "red", "black");
    List<String> suits = List.of("c", "d", "h", "s");
    List<ICard> cards = new ArrayList<>();
    IDeck deck = new Deck("pack52", cards);
    for (int k = 0; k < suits.size(); k++) {
      for (int j = 0; j < names.size(); j++) {
        IValue val = new Value(vals.get(j) + suits.get(k), Integer.parseInt(vals.get(j)));
        ISuit suit = new Suit(suits.get(k), new Color(colors.get(k)));
        cards.add(new Card(names.get(j) + suits.get(k).toUpperCase(), suit, val));
      }
    }

    Map<String, ICell> cellMap = Map
        .of("a", new Cell("a"), "b", new Cell("b"), "c", new Cell("c"), "d", new Cell("d"));

    List<IOffset> offsetList = List.of(Offset.NONE, Offset.SOUTH, Offset.NORTHEAST);

    cellMap.get("b").addCard(Offset.NONE, deck.peekCardAtIndex(51).copy());
    cellMap.get("d").addCard(Offset.NONE, deck.peekCardAtIndex(0).copy());

    for (int k = 0; k < deck.size() / 4; k++) {
      int diff = deck.size() / 4;
      IOffset offset = offsetList.get(k % offsetList.size());
      cellMap.get("a").addCard(offset, deck.peekCardAtIndex(k).copy());
      cellMap.get("b").addCard(Offset.EAST, deck.peekCardAtIndex(k + diff).copy());
      cellMap.get("c").addCard(offset, deck.peekCardAtIndex(k + diff * 2).copy());
      cellMap.get("d").addCard(Offset.SOUTH, deck.peekCardAtIndex(k + diff * 3).copy());
    }
    String phaseName = "phase";
    String rulePath = "data/solitaire_rules.xml";
    String game = "solitaire";
    Double score = 0.0;

    Map<String, String> cellMapBuilder = new HashMap<>();
    for (Map.Entry<String, ICell> c : cellMap.entrySet()) {
      cellMapBuilder.put(c.getKey(), c.getValue().toStorageString());
    }
    ISaveConfiguration saveConfiguration = new SaveConfiguration(game, rulePath, phaseName, score,
        cellMapBuilder);

    SaveConfigurationWriter.writeSave(TEST_DIRECTORY + "save1.xml", saveConfiguration);
    ISaveConfiguration loaded = SaveConfigurationFactory
        .createSave(new File(TEST_DIRECTORY + "save1.xml"));

    assertEquals(cellMap, loaded.getCellMap());
    assertEquals(phaseName, loaded.getCurrentPhase());
    assertEquals(game, loaded.getGameName());
    assertEquals(score, loaded.getScore());
    assertEquals(rulePath, loaded.getRulePath());

    Map<String, ICell> cellMap2 = Map.of("a", cellMap.get("a").copy());
    cellMap2.get("a").addCell(Offset.NONE, cellMap.get("b").copy());
    cellMap2.get("a").addCell(Offset.EAST, cellMap.get("c").copy());
    cellMap2.get("a").addCell(Offset.SOUTH, cellMap.get("d").copy());

    Map<String, String> cellMapBuilder2 = new HashMap<>();
    for (Map.Entry<String, ICell> c : cellMap2.entrySet()) {
      cellMapBuilder2.put(c.getKey(), c.getValue().toStorageString());
    }
    ISaveConfiguration saveConfiguration2 = new SaveConfiguration(game.toUpperCase(), rulePath,
        phaseName.toUpperCase(), score + 10.0, cellMapBuilder2);

    SaveConfigurationWriter.writeSave(TEST_DIRECTORY + "save2.xml", saveConfiguration2);
    ISaveConfiguration loaded2 = SaveConfigurationFactory
        .createSave(new File(TEST_DIRECTORY + "save2.xml"));

    assertEquals(cellMap2, loaded2.getCellMap());
    assertEquals(phaseName.toUpperCase(), loaded2.getCurrentPhase());
    assertEquals(game.toUpperCase(), loaded2.getGameName());
    assertEquals(score + 10.0, loaded2.getScore());
    assertEquals(rulePath, loaded2.getRulePath());
  }
}