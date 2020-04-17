package ooga.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import ooga.cardtable.*;
import ooga.data.rules.ILayout;
import ooga.data.rules.Layout;
import ooga.data.style.ICoordinate;
import java.util.*;

public class DisplayTable {

    private Pane myPane;

    private NumberBinding myCardHeight;
    private NumberBinding myCardWidth;
    private double myCardOffset;
    private Map<String, String> myCardNameToFileName;
    private Map<String, Pair<NumberBinding, NumberBinding>> myCellNameToLocation;

    @FunctionalInterface
    interface MyDragInterface {
        public void returnSelectedDisplayCell(DisplayCell selectedCell);
    }

    @FunctionalInterface
    interface MyClickInterface {
        public void returnSelectedDisplayCell(DisplayCell selectedCell);
    }

    List<DisplayCell> myDisplayCellData = new ArrayList<>();

    MyDragInterface getDraggedCell;
    MyClickInterface getClickedCell;

    DisplayCell myMovedDisplayCell;
    ICell myMover;
    ICell myDonor;
    ICell myRecipient;
    IMove myMove;

    public DisplayTable(View.TriggerMove moveLambda, Layout layout, double screenWidth) {

        myPane = new Pane();

        myCardHeight = Bindings.multiply(layout.getCardHeightRatio(),myPane.heightProperty());
        myCardWidth = Bindings.multiply(layout.getCardWidthRatio(),myPane.widthProperty());
        myCardOffset = layout.getUpOffsetRatio()*screenWidth;

        myCardNameToFileName = layout.getCardImagePaths();

        myCellNameToLocation = new HashMap<>();
        Map<String, ICoordinate> locations = layout.getCellLayout();
        for(String key : locations.keySet()){
            NumberBinding x = Bindings.divide(Bindings.multiply(myPane.widthProperty(),locations.get(key).getX()),100);
            NumberBinding y = Bindings.divide(Bindings.multiply(myPane.heightProperty(),locations.get(key).getY()),100);
            myCellNameToLocation.put(key,new Pair<>(x,y));
        }

        getDraggedCell = (DisplayCell selectedCell) -> {
            myMovedDisplayCell = selectedCell;
            if(checkMove()) {
                moveLambda.giveIMove(myMove);
            }
        };

        getClickedCell = (DisplayCell selectedCell) -> {
            IMove clickMove = new Move(selectedCell.getCell(), selectedCell.getCell(), selectedCell.getCell());
            moveLambda.giveIMove(clickMove);
        };

    }

    private boolean checkMove() {
        DisplayCell intersectedCell = checkIntersections();
        if (intersectedCell != myMovedDisplayCell) {
            myMover = myMovedDisplayCell.getCell();
            myDonor = myMovedDisplayCell.getCell().findHead();
            myRecipient = intersectedCell.getCell().findLeaf();
            myMove = new Move(myDonor, myMover, myRecipient);
        }
        return intersectedCell != myMovedDisplayCell;
    }


    private DisplayCell checkIntersections() {
        boolean isIntersection = false;
        ImageView movedImage = myMovedDisplayCell.getImageView();
        for (DisplayCell dc: myDisplayCellData) {
            ImageView otherImage = dc.getImageView();
            if (!myMovedDisplayCell.getCell().findHead().getName().equals(dc.getCell().findHead().getName())) {
                isIntersection = checkIntersection(movedImage, otherImage);
            }
            if (isIntersection) {
                return dc;
            }
        }
        return myMovedDisplayCell;
    }

    private boolean checkIntersection(ImageView a, ImageView b) {
        return a != null && b != null && a.getBoundsInParent().intersects(b.getBoundsInParent());
    }

    public Pane getPane() {
        return myPane;
    }

    public Pane updateCells(Map<String,ICell> cellData) {
        myPane.getChildren().clear();
        myDisplayCellData.clear();
        List<DisplayCell> displayCellData = makeDisplayCells(cellData);
        drawDisplayCells(displayCellData);
        return myPane;
    }

    public Pane updateTheseCells(Map<String,ICell> cellData) {
        clearTheseCells(cellData); // removes given cells + all children from pane and active list of display cells
        List<DisplayCell> displayCellData = makeDisplayCells(cellData); // converts cells to display cells
        drawDisplayCells(displayCellData); // draws display cells just created by adding them to pane and list of active cells
        return myPane;
    }

    private void clearTheseCells(Map<String,ICell> cellData) {
        List<DisplayCell> copyDisplayCellData = new ArrayList<>();
        copyDisplayCellData.addAll(myDisplayCellData);
        for (ICell c : cellData.values()) { // for every cell that needs to change
            for (DisplayCell dc : copyDisplayCellData) { // find its current display cell
                if (c.getName().equals(dc.getCell().getName())) {
                    clearDisplayCell(dc);
                }
            }
        }
    }

    private void clearDisplayCell(DisplayCell dc) {
        myDisplayCellData.remove(dc);  // remove the display cell + all its children from the list of active display cells
        myPane.getChildren().remove(dc); // remove the display cell +  all its children from the screen
        for (IOffset dir: dc.getCell().getAllChildren().keySet()) {
            if (dir == Offset.NONE) {
                continue;
            }
            clearDisplayCell(dc.getAllChildren().get(dir));
        }
        //System.out.println("Removed "+dc.getCell().getName());, correctly removes everybody
    }

    private List<DisplayCell> makeDisplayCells(Map<String,ICell> cellData) {
        List<DisplayCell> displayCellData = new ArrayList<>();
        for (String c: cellData.keySet()) {
            displayCellData.add(makeDisplayCell(c,cellData.get(c)));
        }
        return displayCellData;
    }

    private DisplayCell makeDisplayCell(String key, ICell cell) {
        Pair<NumberBinding, NumberBinding> location = myCellNameToLocation.get(key);
        return new DisplayCell(getDraggedCell, getClickedCell, cell, myCardNameToFileName, location, myCardHeight, myCardWidth, myCardOffset);
    }

    private void drawDisplayCells(List<DisplayCell> DisplayCellData) {
        for (DisplayCell dc: DisplayCellData) {
            drawDisplayCell(dc);
        }
    }

    private void drawDisplayCell(DisplayCell rootDispCell) {
        if (rootDispCell == null) {
            return;
        }
        myDisplayCellData.add(rootDispCell);
        myPane.getChildren().add(rootDispCell.getImageView());
        for (IOffset dir: rootDispCell.getCell().getAllChildren().keySet()) {
            if (dir == Offset.NONE) {
                continue;
            }
            drawDisplayCell(rootDispCell.getAllChildren().get((Offset) dir));
        }
    }

}



