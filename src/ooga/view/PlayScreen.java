package ooga.view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ooga.data.rules.ILayout;

public class PlayScreen extends Application {

    private String myGameName;
    private ILayout myLayout;
    private Color myTableColor;

    private final int DEFAULT_SCENE_WIDTH = 800;
    private final int DEFAULT_SCENE_HEIGHT = 600;

    public PlayScreen() {
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


        ImageView source = new ImageView(new Image("acehearts.png"));
        source.setX(50);
        source.setY(200);
        source.setFitHeight(100);
        source.setFitWidth(80);

        ImageView target = new ImageView(new Image("twohearts.png"));
        target.setX(250);
        target.setY(200);
        target.setFitHeight(100);
        target.setFitWidth(80);

        source.setOnDragDetected(new EventHandler <MouseEvent>() {
            public void handle(MouseEvent event) {
                System.out.println("onDragDetected");
                Dragboard db = source.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putImage(source.getImage());
                db.setContent(content);

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

    public static void main(String[] args) {
        Application.launch(args);
    }
}

