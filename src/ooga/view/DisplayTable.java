package ooga.view;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ooga.cardtable.*;
import ooga.data.rules.ILayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisplayTable {

    private Pane myPane;
    private ILayout myLayout;

    private Color myTableColor;
    private String myGameName;

    private double myCardHeight;
    private double myCardWidth;
    private double myCardOffset;
    private Map<String, String> myCardNameToFileName;
    private Map<String, Point2D> myCardNameToLocation;
    private List<Cell> myCellData;

    public DisplayTable() {
        myPane = new Pane();

        myGameName =  "Practice Game";
        String tableColor = "0x0000FF";
        myTableColor = Color.web(tableColor);

        myCardHeight = 100;
        myCardWidth = 80;
        myCardOffset = 20;
        myCardNameToFileName = Map.of("Unknown Card", "acehearts.png", "faceDown", "twohearts.png");
        myCardNameToLocation = Map.of("Unknown Card", new Point2D(100,200));
        myCellData = List.of(getDummyCell());

        List<DisplayCell> displayCellData = makeDisplayCells(myCellData);
        drawDisplayCells(displayCellData);

    }

    public Pane getPane() {
        return myPane;
    }

    private List<DisplayCell> makeDisplayCells(List<Cell> cellData) {
        List<DisplayCell> displayCellData = new ArrayList<>();
        for (Cell c: cellData) {
            displayCellData.add(makeDisplayCell(c));
        }
        return displayCellData;
    }

    private DisplayCell makeDisplayCell(Cell cell) {
        return new DisplayCell(cell, myCardNameToFileName, myCardNameToLocation.get(cell.getDeck().peek().getName()), myCardHeight, myCardWidth, myCardOffset);
    }

    private void drawDisplayCells(List<DisplayCell> DisplayCellData) {
        for (DisplayCell dc: DisplayCellData) {
            drawDisplayCell(dc);
        }
    }

    private void drawDisplayCell(DisplayCell rootDispCell) {
        if (rootDispCell.getGroup().getChildren() == null) {
            return;
        }
        myPane.getChildren().addAll(rootDispCell.getGroup().getChildren());
        for (IOffset dir: rootDispCell.getCell().getAllChildren().keySet()) {
            if (dir == Offset.NONE) {
                continue;
            }
            drawDisplayCell(rootDispCell.getAllChildren().get((Offset) dir));
        }
    }

    // TODO: remove
    private Cell getDummyCell() {
        Card testCard = new Card(); // automatically facedown, unknown card
        List<ICard> testCards = List.of(testCard);
        Deck testDeck = new Deck("testDeck", testCards);
        Cell testCell = new Cell("testCell", testDeck);

        Card testAddCard = new Card(); // automatically facedown, unknown cord
        testCell.addCard(Offset.SOUTH,testAddCard);
        Card testAddAnotherCard = new Card();
        testCell.getAllChildren().get(Offset.SOUTH).addCard(Offset.SOUTH,testAddAnotherCard);
        return testCell;
    }

}

