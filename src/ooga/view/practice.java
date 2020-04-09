package ooga.view;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class practice extends Application {
    Point2D lastXY = null;

    // TODO: displaycell has location, imageview of faceup filename, imageview of facedown filename, and CELL object
    // TODO: function that takes in one cell and converts to one display cell, for rendering/updates
    // TODO: function which calls convertToDisplay(), then adds the imageview object to screen....
    // TODO: problem will be when need to drag a display cell which points to other display cells
    // TODO: ^^ can't assume all children move every time, need to ask back end?
    // TODO: how to guarantee selected card during drag is rendered OVER rather than UNDER other cards??

    public void start(Stage primaryStage) {
        Pane mainPane = new Pane();
        Scene scene = new Scene(mainPane, 500, 500, true);
        primaryStage.setScene(scene);
        primaryStage.show();

        ImageView cellImgView1 = getImageView("acehearts.png", 100, 200);
        enableDrag(cellImgView1);

        ImageView cellImgView2 = getImageView("twohearts.png", 200, 200);
        enableDrag(cellImgView2);

        mainPane.getChildren().add(cellImgView1);
        mainPane.getChildren().add(cellImgView2);
    }

    private ImageView getImageView(String filename, int x, int y) { // TODO: should be a getter method in display cell
        ImageView source = new ImageView(new Image(filename));
        source.setX(x);
        source.setY(y);
        source.setFitWidth(80);
        source.setFitHeight(100);
        return source;
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
                lastXY = new Point2D(event.getSceneX(), event.getSceneY());
            }
            // Compute change in position since click
            double dx = event.getSceneX() - lastXY.getX();
            double dy = event.getSceneY() - lastXY.getY();
            // Moves node "on", or what the mouse clicked on, by moving the origin
            on.setTranslateX(on.getTranslateX()+dx);
            on.setTranslateY(on.getTranslateY()+dy);
            // Resets last known XY position
            lastXY = new Point2D(event.getSceneX(), event.getSceneY());
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

    public static void main(String[] args) {
        launch(args);
    }
}