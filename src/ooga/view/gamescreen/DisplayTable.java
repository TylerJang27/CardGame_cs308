package ooga.view.gamescreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import ooga.cardtable.ICell;
import ooga.cardtable.IMove;
import ooga.cardtable.IOffset;
import ooga.cardtable.Move;
import ooga.cardtable.Offset;
import ooga.controller.Controller.GiveMove;
import ooga.data.style.ICoordinate;
import ooga.data.style.ILayout;

public class DisplayTable {

  private static final double DECIMAL_TO_PERCENT = 100;

  private Pane myPane;

  private NumberBinding myCardHeight;
  private NumberBinding myCardWidth;
  private double faceUpCardOffset;
  private double faceDownCardOffset;
  private Map<String, Pair<NumberBinding, NumberBinding>> myCellNameToLocation;

  @FunctionalInterface
  interface MyDragInterface {

    void returnSelectedDisplayCell(DisplayCell selectedCell);
  }

  @FunctionalInterface
  interface MyClickInterface {

    void returnSelectedDisplayCell(DisplayCell selectedCell);
  }

  List<DisplayCell> myDisplayCellData = new ArrayList<>();

  MyDragInterface getDraggedCell;
  MyClickInterface getClickedCell;

  DisplayCell myMovedDisplayCell;
  ICell myMover;
  ICell myDonor;
  ICell myRecipient;
  IMove myMove;

  String mySkinType;

  public DisplayTable(int gameID, GiveMove moveLambda, ILayout layout, double screenWidth,
      String skinType) {
    mySkinType = skinType;
    myPane = new Pane();

    myCardHeight = Bindings.multiply(layout.getCardHeightRatio(), myPane.widthProperty());
    myCardWidth = Bindings.multiply(layout.getCardWidthRatio(), myPane.widthProperty());
    faceUpCardOffset = layout.getUpOffsetRatio() * screenWidth;
    faceDownCardOffset = layout.getDownOffsetRatio() * screenWidth;

    myCellNameToLocation = new HashMap<>();
    Map<String, ICoordinate> locations = layout.getCellLayout();
    for (String key : locations.keySet()) {
      NumberBinding x = Bindings
          .divide(Bindings.multiply(myPane.widthProperty(), locations.get(key).getX()),
              DECIMAL_TO_PERCENT);
      NumberBinding y = Bindings
          .divide(Bindings.multiply(myPane.widthProperty(), locations.get(key).getY()),
              DECIMAL_TO_PERCENT);
      myCellNameToLocation.put(key, new Pair<>(x, y));
    }

    initializeDraggedCell(gameID, moveLambda);

    initializeClickedCell(gameID, moveLambda);

  }

  private void initializeDraggedCell(int gameID, GiveMove moveLambda) {
    getDraggedCell = (DisplayCell selectedCell) -> {
      myMovedDisplayCell = selectedCell;
      if (checkMove()) {
        moveLambda.sendMove(myMove, gameID);
      }
    };
  }

  private void initializeClickedCell(int gameID, GiveMove moveLambda) {
    getClickedCell = (DisplayCell selectedCell) -> {
      IMove clickMove = new Move(selectedCell.getCell(), selectedCell.getCell(),
          selectedCell.getCell());
      moveLambda.sendMove(clickMove, gameID);
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
    for (DisplayCell dc : myDisplayCellData) {
      ImageView otherImage = dc.getImageView();
      if (!myMovedDisplayCell.getCell().findHead().getName()
          .equals(dc.getCell().findHead().getName())) {
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

  public void updateCells(Map<String, ICell> cellData) {
    myPane.getChildren().clear();
    myDisplayCellData.clear();
    List<DisplayCell> displayCellData = makeDisplayCells(cellData);
    drawDisplayCells(displayCellData);
  }

  public void updateTheseCells(Map<String, ICell> cellData) {
    clearTheseCells(cellData);
    List<DisplayCell> displayCellData = makeDisplayCells(cellData);
    drawDisplayCells(displayCellData);
  }

  private void clearTheseCells(Map<String, ICell> cellData) {
    List<DisplayCell> copyDisplayCellData = new ArrayList<>(myDisplayCellData);
    for (ICell c : cellData.values()) {
      for (DisplayCell dc : copyDisplayCellData) {
        if (c.getName().equals(dc.getCell().getName())) {
          clearDisplayCell(dc);
          break;
        }
      }
    }
    copyDisplayCellData.clear();
  }

  private void clearDisplayCell(DisplayCell dc) {
    myDisplayCellData.remove(dc);
    if (dc.getImageView() == null) {
      // do nothing
    } else {
      dc.getImageView().setImage(null);
      myPane.getChildren().remove(dc.getImageView());
    }
    for (IOffset dir : dc.getCell().getAllChildren().keySet()) {
      if (dir == Offset.NONE) {
        continue;
      }
      clearDisplayCell(dc.getAllChildren().get(dir));
    }
  }

  private List<DisplayCell> makeDisplayCells(Map<String, ICell> cellData) {
    List<DisplayCell> displayCellData = new ArrayList<>();
    for (String c : cellData.keySet()) {
      displayCellData.add(makeDisplayCell(c, cellData.get(c)));
    }
    return displayCellData;
  }

  private DisplayCell makeDisplayCell(String key, ICell cell) {
    Pair<NumberBinding, NumberBinding> location = myCellNameToLocation.get(key);
    return new DisplayCell(getDraggedCell, getClickedCell, cell, mySkinType, location, myCardHeight,
        myCardWidth, faceDownCardOffset, faceUpCardOffset);
  }

  private void drawDisplayCells(List<DisplayCell> DisplayCellData) {
    for (DisplayCell dc : DisplayCellData) {
      drawDisplayCell(dc);
    }
  }

  private void drawDisplayCell(DisplayCell rootDispCell) {
    if (rootDispCell == null) {
      return;
    }
    myDisplayCellData.add(rootDispCell);
    myPane.getChildren().add(rootDispCell.getImageView());
    for (IOffset dir : rootDispCell.getCell().getAllChildren().keySet()) {
      if (dir == Offset.NONE) {
        continue;
      }
      drawDisplayCell(rootDispCell.getAllChildren().get(dir));
    }
  }

}



