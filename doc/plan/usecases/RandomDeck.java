package ooga.apiexamples;

import ooga.cardtable.ICell;
import ooga.cardtable.IDeck;
import ooga.cardtable.IOffset;

public class RandomDeck {
    private IDeck deck;
    private ICell myCell;

    for(int i = 0; i < deck.size(); i++) {
        deck.shuffle();
        myCell.addCard(IOffset, deck.getNextCard());
    }
}
