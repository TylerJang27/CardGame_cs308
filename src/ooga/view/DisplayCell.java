package ooga.view;

import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import ooga.cardtable.Cell;

import javafx.geometry.Point2D;

public class DisplayCell {

    Cell myCell;

    ImageView myImageView;
    Image myFaceUp;
    Image myFaceDown;

    double cardHeight;
    double cardWidth;

    Point2D lastXY = null;

    public DisplayCell(Cell cell, String faceUp, String faceDown, Point2D location, double height, double width) {
        myCell = cell;
        myFaceDown = new Image(faceDown);
        myFaceUp = new Image(faceUp);
        cardHeight = height;
        cardWidth = width;
        if (cell.getDeck().peek().isFaceUp()) {
            myImageView = new ImageView(faceUp);
        } else {
            myImageView = new ImageView(faceDown);
        }
        myImageView.setX(location.getX());
        myImageView.setY(location.getY());
        myImageView.setFitWidth(width);
        myImageView.setFitHeight(height);
        enableDrag(myImageView);
    }

    public ImageView getImageView() {
        return myImageView;
    }

    private void enableDrag(ImageView source) {
        source.setOnMouseDragged(event -> {
            // Every time a drag occurs, move the node accordingly
            System.out.println("Move");
            // Allow drag to any position by disabling detect (hover) feature
            event.setDragDetect(false);
            // getTarget() returns event target (what was clicked on by mouse)
            Node on = (Node)event.getTarget();
            if (lastXY == null) {
                // getSceneX() returns x position of mouse within the scene where click occurred
                lastXY = new javafx.geometry.Point2D(event.getSceneX(), event.getSceneY());
            }
            // Compute change in position since click
            double dx = event.getSceneX() - lastXY.getX();
            double dy = event.getSceneY() - lastXY.getY();
            // Moves node "on", or what the mouse clicked on, by moving the origin
            on.setTranslateX(on.getTranslateX()+dx);
            on.setTranslateY(on.getTranslateY()+dy);
            // Resets last known XY position
            lastXY = new javafx.geometry.Point2D(event.getSceneX(), event.getSceneY());
            event.consume();
        });

        source.setOnDragDetected(event -> {
            // When a drag is first detected for this object
            System.out.println("Drag:"+event);
            // Get node dragged
            Node on = (Node)event.getTarget();
            // Make dragboard
            Dragboard db = on.startDragAndDrop(TransferMode.COPY);
            // Add correct image to cliboard content
            ClipboardContent content = new ClipboardContent();
            WritableImage image = on.snapshot(new SnapshotParameters(), null);
            content.put(DataFormat.IMAGE, image);
            db.setContent(content);
            event.consume();
        });

        // When mouse is released, reset lastXY for next drag event
        source.setOnMouseReleased(d -> {
            lastXY = null;
        });
    }



}
