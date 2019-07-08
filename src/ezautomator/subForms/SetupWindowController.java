/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.subForms;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author gornicma
 */
public class SetupWindowController implements Initializable {

    @FXML
    private Button captureBtn;

    @FXML
    private Button btnProceed;

    @FXML
    private ImageView closeBtn;

    @FXML
    private JFXTextField xValueTxt;

    @FXML
    private Button genBtn;

    @FXML
    private JFXTextField fKeyTxt;

    @FXML
    private JFXTextField sKeyTxt;

    @FXML
    private JFXTextField yValueTxt;

    @FXML
    private Pane paneClick;

    @FXML
    private StackPane root;

    @FXML
    private Pane paneKeys;

    private double diffX;

    private double diffY;

    @FXML
    void closeApp(MouseEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    void closeBtnChangeHover(MouseEvent event) {
        closeBtn.setImage(new Image("/ezautomator/icons/close-hover.png"));
    }

    @FXML
    void closeBtnChangeLeave(MouseEvent event) {
        closeBtn.setImage(new Image("/ezautomator/icons/close.png"));
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        paneKeys.setDisable(true);
        
        /**
         * Form movement implementation
         */
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) root.getScene().getWindow();
                // Calculating the difference between the cursor and the stage to later add it to allow the cursor to stay at the same
                // position without teleporting it to a different place
                diffX = stage.getX() - event.getScreenX();
                diffY = stage.getY() - event.getScreenY();
            }
        });

        // Ensuring that the stage moves a fixed number (that we calculated when the user first clicked the stage) of spaces
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) root.getScene().getWindow();
                // Helps my cursor stay at the same position when dragging the window instead of teleporting to the top most-left corner
                stage.setX(event.getScreenX() + diffX);
                stage.setY(event.getScreenY() + diffY);
            }
        });

    }

    public void disablePane(int ID) {
        switch (ID) {
            case 1:
                paneKeys.setDisable(true);
                break;
            case 2:
                paneClick.setDisable(true);
                break;
            default:
                System.out.println("Invalid ID");
                break;
        }
    }
}
