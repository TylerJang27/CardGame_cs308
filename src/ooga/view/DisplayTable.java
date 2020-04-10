package ooga.view;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import ooga.cardtable.*;
import ooga.data.rules.ILayout;
import ooga.data.rules.Layout;
import ooga.data.style.ICoordinate;

import java.lang.reflect.Array;
import java.util.*;

public class DisplayTable {

    private Pane myPane;
    private ILayout myLayout;

    private Color myTableColor;
    private String myGameName;

    private double myScreenWidth;
    private double myCardHeight;
    private double myCardWidth;
    private double myCardOffset;
    private Map<String, String> myCardNameToFileName;
    private Map<String, ICoordinate> myCellNameToLocation;
    private List<Cell> myCellData;


    public DisplayTable(Layout layout, double screenwidth) {
        System.out.println("layout: "+layout.getCellLayout().keySet());

        myScreenWidth = screenwidth;
        myPane = new Pane();

        String tableColor = "0x0000FF";
        myTableColor = Color.web(tableColor);

        myCardHeight = layout.getCardHeightRatio()*screenwidth;
        myCardWidth = layout.getCardWidthRatio()*screenwidth;
        myCardOffset = layout.getUpOffsetRatio()*screenwidth;

        myCardNameToFileName = new HashMap<>();
        List<String> cardDeck = Arrays.asList( "faceDown", "AC", "2C", "3C","4C","5C","6C","7C","8C","9C","0C","JC","QC","KC","AD","2D","3D","4D","5D","6D","7D","8D","9D","0D","JD","QD","KD","AH","2H","3H","4H","5H","6H","7H","8H","9H","0H","JH","QH","KH","AS","2S","3S","4S", "5S", "6S", "7S", "8S","9S", "0S", "JS", "QS", "KS");
        for (String card: cardDeck) {
            myCardNameToFileName.put(card, card+".png");
        }
        myCellNameToLocation = layout.getCellLayout();
        /*
        for(String key : layout.getCellLayout().keySet()){
            Button b = new Button(key);
            double xVal = 3 * layout.getCellLayout().get(key).getX();
            double yVal = 3 * layout.getCellLayout().get(key).getY();
            myPane.getChildren().add(b);
            b.setLayoutX(xVal);
            b.setLayoutY(yVal);
        }
         */
    }

    public Pane getPane() {
        return myPane;
    }


    public Pane updateCells(Map<String,ICell> cellData) {
        // TODO: for now, I assume update receives all of the cells, not just ones which needed to be changed
        //myPane = new Pane();
        List<DisplayCell> displayCellData = makeDisplayCells(cellData);
        drawDisplayCells(displayCellData);
        return myPane;
    }


    private List<DisplayCell> makeDisplayCells(Map<String,ICell> cellData) {
        List<DisplayCell> displayCellData = new ArrayList<>();
        for (String c: cellData.keySet()) {
            displayCellData.add(makeDisplayCell(c,(Cell)cellData.get(c)));
        }
        return displayCellData;
    }

    private DisplayCell makeDisplayCell(String key, Cell cell) {
        ICoordinate icoord = myCellNameToLocation.get(key);
        double x = icoord.getX()*myScreenWidth/100.0;
        double y = icoord.getY()*myScreenWidth/100.0;
        System.out.println("key: "+key);
        return new DisplayCell(cell, myCardNameToFileName, new Point2D(x,y), myCardHeight, myCardWidth, myCardOffset);
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
        //System.out.println("Victory Royale");
        for(Node n : myPane.getChildren()){
            //System.out.println(n);
            System.out.println(n.getTranslateX());
           // System.out.println(n.getLayoutY());
           // System.out.println(n.getTranslateX());
           // System.out.println(n.getTranslateY());
        }
        for (IOffset dir: rootDispCell.getCell().getAllChildren().keySet()) {
            if (dir == Offset.NONE) {
                continue;
            }
            drawDisplayCell(rootDispCell.getAllChildren().get((Offset) dir));
        }
    }

}



