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

    private double myScreenWidth;
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

    public DisplayTable(View.TriggerMove moveLambda, Layout layout, double screenwidth) {
        System.out.println(layout.getScreenRatio());

        myScreenWidth = screenwidth;
        myPane = new Pane();

        myCardHeight = Bindings.multiply(layout.getCardHeightRatio(),myPane.heightProperty());
        myCardWidth = Bindings.multiply(layout.getCardWidthRatio(),myPane.widthProperty());
        myCardOffset = layout.getUpOffsetRatio()*screenwidth;

        myCardNameToFileName = layout.getCardImagePaths();

        //myCellNameToLocation = layout.getCellLayout();
        myCellNameToLocation = new HashMap<>();
        Map<String, ICoordinate> locations = layout.getCellLayout();
        for(String key : locations.keySet()){
            System.out.println("key is: " + key);
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

    private boolean checkMove() {
        DisplayCell intersectedCell = checkIntersections();
        if (intersectedCell != myMovedDisplayCell) { //TODO: TYLER FUDGED WITH THIS
            myMover = myMovedDisplayCell.getCell();
            myDonor = myMovedDisplayCell.getCell().findHead();
            myRecipient = intersectedCell.getCell().findLeaf();
            System.out.println("recipient ahoy:" + myRecipient);
            myMove = new Move(myDonor, myMover, myRecipient);
        }
        return intersectedCell != myMovedDisplayCell;
    }





    private DisplayCell checkIntersections() {
        boolean isIntersection = false;
        ImageView movedImage = myMovedDisplayCell.getImageView();
        for (DisplayCell dc: myDisplayCellData) {
            ImageView otherImage = dc.getImageView();
            //if (!myMovedDisplayCell.getCell().getName().equals(dc.getCell().getName())) {
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
        myDisplayCellData.clear(); //fixme added by Maverick
        List<DisplayCell> displayCellData = makeDisplayCells(cellData);
        drawDisplayCells(displayCellData);
        for(Node node : myPane.getChildren()){
            System.out.println("" + node + node.getTranslateX() + node.getTranslateY());
        }
        return myPane;
    }

    private List<DisplayCell> makeDisplayCells(Map<String,ICell> cellData) {
        List<DisplayCell> displayCellData = new ArrayList<>();
        for (String c: cellData.keySet()) {
            displayCellData.add(makeDisplayCell(c,cellData.get(c))); // TODO
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
        if (rootDispCell.getGroup().getChildren() == null) {
            return;
        }
        myDisplayCellData.add(rootDispCell);
        myPane.getChildren().addAll(rootDispCell.getGroup().getChildren());
        for (IOffset dir: rootDispCell.getCell().getAllChildren().keySet()) {
            if (dir == Offset.NONE) {
                continue;
            }
            drawDisplayCell(rootDispCell.getAllChildren().get((Offset) dir));
        }
    }

}



