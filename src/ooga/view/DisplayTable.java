package ooga.view;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ooga.data.rules.ILayout;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.LinkedList;

public class DisplayTable extends Application {

    private String myGameName;
    private ILayout myLayout;
    private Color myTableColor;

    private final int DEFAULT_SCENE_WIDTH = 800;
    private final int DEFAULT_SCENE_HEIGHT = 600;

    public DisplayTable() {
        //ILayout inLayout
        //myLayout = inLayout;
        myGameName =  "Practice Game";
        String tableColor = "0x0000FF";
        myTableColor = Color.web(tableColor);
    }

    @Override public void start(Stage stage) {

        stage.setTitle(myGameName);

        Group root = new Group();
        Scene scene = new Scene(root, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);
        scene.setFill(myTableColor);

        Image ace = new Image("acehearts.png",80,100,true,true);
        ImageView source = new ImageView(ace);
        source.setX(50);
        source.setY(200);

        Image two = new Image("twohearts.png",80,100,true,true);
        ImageView target = new ImageView(two);
        target.setX(250);
        target.setY(200);

        source.setOnDragDetected(new EventHandler <MouseEvent>() {
            public void handle(MouseEvent event) {
                System.out.println("onDragDetected");
                Dragboard db = source.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putImage(ace);
                System.out.println(content.getImage().getHeight());

                db.setContent(content);
                db.setDragView(ace);
                System.out.println(db.getDragView().getHeight());
                //db.setDragView(source.getImage());

                event.consume();
            }
        });

        target.setOnDragOver(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                System.out.println("onDragOver");

                /* accept it only if it is  not dragged from the same node
                 * and if it has a string data */
                if (event.getGestureSource() != target &&
                        event.getDragboard().hasImage()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }

                event.consume();
            }
        });

        target.setOnDragEntered(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture entered the target */
                System.out.println("onDragEntered");
                /* show to the user that it is an actual gesture target */
                if (event.getGestureSource() != target &&
                        event.getDragboard().hasString()) {
                    //target.setFill(Color.GREEN);
                }
                event.consume();
            }
        });

        target.setOnDragExited(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away from area */
                //target.setFill(Color.BLACK);
                event.consume();
            }
        });

        target.setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                System.out.println("onDragDropped");
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasImage()) {
                    target.setImage(db.getImage());
                    success = true;
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        });

        source.setOnDragDone(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture ended */
                System.out.println("onDragDone");
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {
                    root.getChildren().remove(source);
                }

                event.consume();
            }
        });



        root.getChildren().add(source);
        root.getChildren().add(target);
        stage.setScene(scene);
        stage.show();
    }

    public static ClipboardContent makeClipboardContent(MouseEvent event, Node child, String text) {
        ClipboardContent cb = new ClipboardContent();
        if (text != null) {
            cb.put(DataFormat.PLAIN_TEXT, text);
        }
        if (!event.isShiftDown()) {
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            Bounds b = child.getBoundsInParent();
            double f = 10;
            params.setViewport(new Rectangle2D(b.getMinX()-f, b.getMinY()-f, b.getWidth()+f+f, b.getHeight()+f+f));

            WritableImage image = child.snapshot(params, null);
            cb.put(DataFormat.IMAGE, image);

            try {
                File tmpFile = File.createTempFile("snapshot", ".png");
                LinkedList<File> list = new LinkedList<File>();
                ImageIO.write(SwingFXUtils.fromFXImage(image, null),
                        "png", tmpFile);
                list.add(tmpFile);
                cb.put(DataFormat.FILES, list);
            } catch (Exception e) {

            }
        }
        return cb;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}

