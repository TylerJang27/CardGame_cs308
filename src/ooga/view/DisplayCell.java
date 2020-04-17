package ooga.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import ooga.cardtable.*;
import javafx.geometry.Point2D;
import java.util.HashMap;
import java.util.Map;

import ooga.cardtable.Offset;

public class DisplayCell {

    private Map<Offset, DisplayCell> myDisplayChildren = new HashMap<>();
    private ICell myCell;
    private Group myGroup = new Group();

    private ImageView myImageView;

    private Map<Offset, Point2D> offsetDirToAmount;

    private Point2D lastXY = null;

    private DisplayTable.MyDragInterface myDragLambda;
    private DisplayTable.MyClickInterface myClickLambda;

    public DisplayCell(DisplayTable.MyDragInterface dragLambda, DisplayTable.MyClickInterface clickLambda, ICell cell, Map<String, String> cardNameToFileName, Pair<NumberBinding, NumberBinding>location, NumberBinding height, NumberBinding width, double offset) {
        myDragLambda = dragLambda;
        myClickLambda = clickLambda;
        myCell = cell;

        if(myCell.getDeck().peek() != null) {
            if (myCell.getDeck().peek().isFaceUp()) {
                myImageView = new ImageView(new Image(cardNameToFileName.get(myCell.getDeck().peek().getName())));
            } else {
                myImageView = new ImageView(new Image(cardNameToFileName.get("faceDown")));
            }

/*
            try {
                Image faceUp = new Image(cardName + ".png");//cardNameToFileName.get(myCell.getDeck().peek().getName()));
            } catch (IllegalArgumentException e) {
                Image faceUp = new Image("0C" + ".png"); //TODO: REPLACE WITH A DEFAULT CARD SKIN
            }
*/
        } else {
            /*String cellName = myCell.getName();
                myFaceUp = new Image(cardNameToFileName.get(cellName));

             */
            myImageView = new ImageView(new Image(cardNameToFileName.get("celloutline")));
        }

        myImageView.layoutXProperty().bind(Bindings.divide(myImageView.fitWidthProperty(),-2));
        myImageView.layoutYProperty().bind(Bindings.divide(myImageView.fitHeightProperty(),-2));
        myImageView.translateXProperty().bind(location.getKey());
        myImageView.translateYProperty().bind(location.getValue());
        myImageView.fitWidthProperty().bind(width);
        myImageView.fitHeightProperty().bind(height);

        if (!myCell.isFixed()) { //TODO: Hi Tyler, definitely the right way to do it (I tested it by making only faceUp cards movable, np), looks like isFixed() always false
            enableDrag(myImageView);
            enableClick(myImageView);
        } else {
            //System.out.println("I'm not draggable or clickable");
        }

        myGroup.getChildren().add(myImageView);

        offsetDirToAmount = Map.of(Offset.NONE, new Point2D(0,0), Offset.NORTH, new Point2D(0, -offset), Offset.SOUTH, new Point2D(0,offset), Offset.EAST, new Point2D(offset, 0),Offset.WEST, new Point2D(-offset,0), Offset.NORTHEAST, new Point2D(offset,-offset), Offset.SOUTHEAST, new Point2D(offset,offset), Offset.NORTHWEST, new Point2D(-offset,-offset), Offset.SOUTHWEST, new Point2D(-offset,offset));

        for (IOffset dir: myCell.getAllChildren().keySet()) {
            Cell childCell = (Cell) myCell.getAllChildren().get(dir);
            if (dir == Offset.NONE) { // && childCell.getDeck().peek() == null
                continue;
            }
            Point2D offsetAmount = offsetDirToAmount.get(dir);
            Pair<NumberBinding, NumberBinding> childOffset = new Pair<>(myImageView.translateXProperty().add(offsetAmount.getX()),myImageView.translateYProperty().add(offsetAmount.getY()));
            DisplayCell childDisplayCell = new DisplayCell(myDragLambda, myClickLambda, childCell, cardNameToFileName, childOffset, height, width, offset);
            myDisplayChildren.put((Offset) dir, childDisplayCell);
            myGroup.getChildren().add(childDisplayCell.getImageView());
        }
    }

    public Map<Offset,DisplayCell> getAllChildren() {
        return myDisplayChildren;
    }

    public Group getGroup() {
        return myGroup;
    }

    public ImageView getImageView() {
        return myImageView;
    }

    public ICell getCell() {
        return myCell;
    }

    public Point2D getLastXY() {
        return lastXY;
    }

    public void setLastXY(Point2D newXY) {
        lastXY = newXY;
    }

    private void enableDrag(ImageView source) {
        source.setOnMouseDragged(event -> {
            event.setDragDetect(false);
            Node on = (Node)event.getTarget();
            source.translateXProperty().unbind();
            source.translateYProperty().unbind();
            moveAll(this, new Point2D(event.getSceneX(), event.getSceneY()));
            event.consume();
        });

        source.setOnMouseReleased(d -> {
            resetAll(this);
            myDragLambda.returnSelectedDisplayCell(this);
        });
    }

    private void enableClick(ImageView source) {
        source.setOnMouseClicked( click -> {
            myClickLambda.returnSelectedDisplayCell(this);
        });
    }

    private void resetAll(DisplayCell selectedCell) {
        selectedCell.setLastXY(null);
        for (Offset dir: selectedCell.getAllChildren().keySet()) {
            if (dir == Offset.NONE) {
                continue;
            }
            resetAll(selectedCell.getAllChildren().get(dir));
        }
    }

    private void moveAll(DisplayCell selectedCell, Point2D initDragToXY) {
        if (!selectedCell.getCell().isFixed()) {
            moveChildTo(selectedCell,initDragToXY);
        }
        for (Offset dir: selectedCell.getAllChildren().keySet()) {
            if (dir == Offset.NONE) {
                continue;
            }
            moveAll(selectedCell.getAllChildren().get(dir), initDragToXY.add(offsetDirToAmount.get(dir)));
        }
    }

    private void moveChildTo(DisplayCell childCell, Point2D dragToXY) {
        Point2D dragFromXY = childCell.getLastXY();
        if (dragFromXY == null) {
            dragFromXY = dragToXY;
            childCell.setLastXY(dragFromXY);
        }
        Point2D dxdy = dragToXY.subtract(dragFromXY);
        Node on = (Node) childCell.getImageView();
        on.translateXProperty().unbind();
        on.translateYProperty().unbind();
        on.setTranslateX(on.getTranslateX()+dxdy.getX());
        on.setTranslateY(on.getTranslateY()+dxdy.getY());
        on.toFront();
        childCell.setLastXY(dragToXY);
    }

}
