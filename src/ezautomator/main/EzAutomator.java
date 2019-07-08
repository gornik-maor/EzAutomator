package ezautomator.main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Abwatts
 */
public class EzAutomator extends Application {

    public static Boolean isSplashLoaded = false;
    public static Boolean isMaximized = false;
    double diffX;
    double diffY;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ezautomator/main/FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setTitle("EzAutomator");
        stage.show();

        /**
         * Events for main window Click first, then drag.
         */
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Calculating the difference between the cursor and the stage to later add it to allow the cursor to stay at the same
                // position without teleporting it to a different place
                diffX = stage.getX() - event.getScreenX();
                diffY = stage.getY() - event.getScreenY();

//                System.out.println("Event: " + event.getScreenX() + " " + event.getScreenY());
//                System.out.println("Stage: " + stage.getX() + " " + stage.getY());
//                System.out.println("DiffX: " + diffX);
//                System.out.println("DiffY: " + diffY);
            }
        });

        // Ensuring that the stage moves a fixed number (that we calculated when the user first clicked the stage) of spaces
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Helps my cursor stay at the same position when dragging the window instead of teleporting to the top most-left corner
                stage.setX(event.getScreenX() + diffX);
                stage.setY(event.getScreenY() + diffY);

//                System.out.println("Dragged Event: " + event.getScreenX() + " " + event.getScreenY());
//                System.out.println("Dragged Stage: " + stage.getX() + " " + stage.getY());
//                System.out.println("New diffX: " + event.getScreenX() + diffX);
//                System.out.println("New diffY: " + event.getScreenY() + diffY);
                    
//                  System.out.println("X: " + event.getScreenX() + " + " + diffX + " = " + (event.getScreenX() + diffX));
//                  System.out.println("Y: " + event.getScreenY() + " + " + diffY + " = " + (event.getScreenY() + diffY));
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
