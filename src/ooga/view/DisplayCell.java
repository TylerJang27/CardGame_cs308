package ooga.view;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import ooga.cardtable.*;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ooga.cardtable.Offset;

public class DisplayCell {

    private Map<Offset, DisplayCell> myDisplayChildren = new HashMap<>();
    private Cell myCell;

    Group myGroup;

    private ImageView myImageView;
    private Image myFaceUp;
    private Image myFaceDown;

    private Point2D lastXY = null;

    public DisplayCell(Cell cell, Map<String, String> cardNameToFileName, Point2D location, double height, double width, double offset) {

        myCell = cell;
        myFaceDown = new Image(cardNameToFileName.get("faceDown"));
        myFaceUp = new Image(cardNameToFileName.get(myCell.getDeck().peek().getName()));

        if (myCell.getDeck().peek().isFaceUp()) {
            myImageView = new ImageView(myFaceUp);
        } else {
            myImageView = new ImageView(myFaceDown);
        }
        myImageView.setX(location.getX());
        myImageView.setY(location.getY());
        myImageView.setFitWidth(width);
        myImageView.setFitHeight(height);
        enableDrag(myImageView);

        myGroup = new Group();
        myGroup.getChildren().add(myImageView);

        Map<Offset, Point2D> offsetDirToAmount = Map.of(Offset.NONE, new Point2D(0,0), Offset.NORTH, new Point2D(0, -offset), Offset.SOUTH, new Point2D(0,offset), Offset.EAST, new Point2D(offset, 0),Offset.WEST, new Point2D(-offset,0), Offset.NORTHEAST, new Point2D(offset,-offset), Offset.SOUTHEAST, new Point2D(offset,offset), Offset.NORTHWEST, new Point2D(-offset,-offset), Offset.SOUTHWEST, new Point2D(-offset,offset));

        /*
        Map<IOffset,ICell> testChildren = myCell.getAllChildren(); // FIXME: NullPointerException?
        for (IOffset dir: myCell.getAllChildren().keySet()) {
            Cell childCell = (Cell) myCell.getAllChildren().get(dir);
            DisplayCell childDisplayCell = new DisplayCell(childCell, cardNameToFileName, location.add(offsetDirToAmount.get(dir)), height, width, offset);
            myDisplayChildren.put((Offset) dir, childDisplayCell);
            // TODO: adding groups
            myGroup.getChildren().add(childDisplayCell.getImageView());
        }
         */

    }

    public Group getGroup() {
        return myGroup;
    }

    public ImageView getImageView() {
        return myImageView;
    }

    public Cell getCell() {
        return myCell;
    }


    private void enableDrag(ImageView source) {
        source.setOnMouseDragged(event -> {
            event.setDragDetect(false);
            Node on = (Node)event.getTarget();
            on.toFront();
            if (lastXY == null) {
                lastXY = new javafx.geometry.Point2D(event.getSceneX(), event.getSceneY());
            }
            double dx = event.getSceneX() - lastXY.getX();
            double dy = event.getSceneY() - lastXY.getY();
            on.setTranslateX(on.getTranslateX()+dx);
            on.setTranslateY(on.getTranslateY()+dy);
            // TODO: translate all display children as well (enable drag on myGroup?)
            lastXY = new javafx.geometry.Point2D(event.getSceneX(), event.getSceneY());
            event.consume();
        });

        source.setOnDragDetected(event -> {
            Node on = (Node)event.getTarget();
            Dragboard db = on.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            WritableImage image = on.snapshot(new SnapshotParameters(), null);
            content.put(DataFormat.IMAGE, image);
            db.setContent(content);
            event.consume();
        });

        source.setOnMouseReleased(d -> {
            lastXY = null;
        });
    }

}
