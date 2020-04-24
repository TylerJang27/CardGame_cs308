package ooga.view.gamescreen;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import ooga.cardtable.*;
import javafx.geometry.Point2D;
import java.util.HashMap;
import java.util.Map;

import ooga.cardtable.Offset;

public class DisplayCell {

    private static final String DEFAULT_SKIN_TYPE = "classic";

    private Map<Offset, DisplayCell> myDisplayChildren = new HashMap<>();
    private ICell myCell;

    private ImageView myImageView;

    private Map<Offset, Point2D> faceUpOffsetDirToAmount;
    private Map<Offset, Point2D> faceDownOffsetDirToAmount;

    private Point2D lastXY = null;

    private DisplayTable.MyDragInterface myDragLambda;
    private DisplayTable.MyClickInterface myClickLambda;

    public DisplayCell(DisplayTable.MyDragInterface dragLambda, DisplayTable.MyClickInterface clickLambda, ICell cell, String skinType, Pair<NumberBinding, NumberBinding>location, NumberBinding height, NumberBinding width, double faceDownOffset, double faceUpOffset) {
        myDragLambda = dragLambda;
        myClickLambda = clickLambda;
        myCell = cell;

        if(myCell.getDeck().peek() != null) {
            System.out.println("an image!");
            if (myCell.getDeck().peek().isFaceUp()) {
                try {
                    myImageView = new ImageView(new Image("/ooga/resources/decks/standard/" + skinType + "/" + myCell.getDeck().peek().getName() + ".png"));
                } catch (Exception e) {
                    myImageView = new ImageView(new Image("/ooga/resources/decks/standard/" + DEFAULT_SKIN_TYPE + "/" + myCell.getDeck().peek().getName() + ".png"));
                }
            } else {
                myImageView = new ImageView(new Image("/ooga/resources/decks/standard/"+skinType+"/faceDown.png"));
                myImageView.getStyleClass().add("cardskin");
            }

        } else {
            myImageView = new ImageView(new Image("/ooga/resources/em.jpg"));
        }

        myImageView.layoutXProperty().bind(Bindings.divide(myImageView.fitWidthProperty(),-2));
        myImageView.layoutYProperty().bind(Bindings.divide(myImageView.fitHeightProperty(),-2));
        myImageView.translateXProperty().bind(location.getKey());
        myImageView.translateYProperty().bind(location.getValue());
        myImageView.fitWidthProperty().bind(width);
        myImageView.fitHeightProperty().bind(height);

        if(myCell.getDeck().peek() != null && !myCell.isFixed()) {
            enableDrag(myImageView);
            enableClick(myImageView);
        }

        faceDownOffsetDirToAmount = Map.of(Offset.NONE, new Point2D(0,0), Offset.NORTH, new Point2D(0, -faceDownOffset), Offset.SOUTH, new Point2D(0,faceDownOffset), Offset.EAST, new Point2D(faceDownOffset, 0),Offset.WEST, new Point2D(-faceDownOffset,0), Offset.NORTHEAST, new Point2D(faceDownOffset,-faceDownOffset), Offset.SOUTHEAST, new Point2D(faceDownOffset,faceDownOffset), Offset.NORTHWEST, new Point2D(-faceDownOffset,-faceDownOffset), Offset.SOUTHWEST, new Point2D(-faceDownOffset,faceDownOffset));
        faceUpOffsetDirToAmount = Map.of(Offset.NONE, new Point2D(0,0), Offset.NORTH, new Point2D(0, -faceUpOffset), Offset.SOUTH, new Point2D(0,faceUpOffset), Offset.EAST, new Point2D(faceUpOffset, 0),Offset.WEST, new Point2D(-faceUpOffset,0), Offset.NORTHEAST, new Point2D(faceUpOffset,-faceUpOffset), Offset.SOUTHEAST, new Point2D(faceUpOffset,faceUpOffset), Offset.NORTHWEST, new Point2D(-faceUpOffset,-faceUpOffset), Offset.SOUTHWEST, new Point2D(-faceUpOffset,faceUpOffset));


        for (IOffset dir: myCell.getAllChildren().keySet()) {
            Cell childCell = (Cell) myCell.getAllChildren().get(dir);
            if (dir == Offset.NONE) {
                continue;
            }
            Point2D offsetAmount;
            if (myCell.getDeck().peek().isFaceUp()) {
                offsetAmount = faceUpOffsetDirToAmount.get(dir);
            } else {
                offsetAmount = faceDownOffsetDirToAmount.get(dir);
            }
            Pair<NumberBinding, NumberBinding> childOffset = new Pair<>(myImageView.translateXProperty().add(offsetAmount.getX()),myImageView.translateYProperty().add(offsetAmount.getY()));
            DisplayCell childDisplayCell = new DisplayCell(myDragLambda, myClickLambda, childCell, skinType, childOffset, height, width, faceDownOffset, faceUpOffset);
            myDisplayChildren.put((Offset) dir, childDisplayCell);
        }
    }

    public Map<Offset,DisplayCell> getAllChildren() {
        return myDisplayChildren;
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
            if (this.myCell.getDeck().peek().isFaceUp()) {
                moveAll(selectedCell.getAllChildren().get(dir), initDragToXY.add(faceUpOffsetDirToAmount.get(dir)));
            } else {
                moveAll(selectedCell.getAllChildren().get(dir), initDragToXY.add(faceDownOffsetDirToAmount.get(dir)));
            }
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
