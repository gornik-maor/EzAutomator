package ezautomator.main;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Abwatts
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private ChoiceBox<String> actionsBox;

    @FXML
    private ToggleButton btnExecutions;

    @FXML
    private Button btnAdd;

    @FXML
    private ImageView closeBtn;

    @FXML
    private RadioButton rdoRunF;

    @FXML
    private StackPane root;

    @FXML
    private ImageView btnRemove;

    @FXML
    private TableView<String> actionTable;

    @FXML
    private JFXTextField txtComment;

    @FXML
    private Button btnRun;
    
    String IMAGE1 = "ezautomator\\icons\\close.png";
    String IMAGE2 = "ezautomator\\icons\\close-hover.png";

    @FXML
    void closeApp() {
        Stage tempStage = (Stage) closeBtn.getScene().getWindow();
        tempStage.close();
    }

    @FXML
    void closeBtnChangeHover() {
        closeBtn.setStyle("-fx-image: url(\""+ IMAGE2 + "\");");
    }

    @FXML
    void closeBtnChangeLeave() {
        closeBtn.setStyle("-fx-image: url(\""+ IMAGE1 + "\");");

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!EzAutomator.isSplashLoaded) {
            //loadSpalshScreen();
            populateActions();
        }

    }

    /**
     * Initializing the actions table
     *
     * @return
     */
//    public ObservableList<Action> getActions() {
//        
//    }
    /**
     * Populating the choice box
     */
    public void populateActions() {
//        actionsBox.getItems().addAll("Click on specified coordinates", "Send Keys");
        actionsBox.getItems().addAll("Click on specified coordinates", "Send Keys");
    }

    /**
     * Loading the welcome screen
     */
    private void loadSpalshScreen() {
        try {
            StackPane loginPane = FXMLLoader.load(getClass().getResource("/ezautomator/login/SplashFXML.FXML"));
            root.getChildren().setAll(loginPane);

            // Creating fade in transition 
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), loginPane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);
            fadeIn.play();

            // Creating fade out transition 
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), loginPane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);

            fadeIn.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // Playing the fade out transition
                    fadeOut.play();
                }
            });

            fadeOut.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // Setting the scene back to the original
                    EzAutomator.isSplashLoaded = true;

                    try {
                        StackPane mainPane = FXMLLoader.load(getClass().getResource("/ezautomator/main/FXMLDocument.fxml"));
                        root.getChildren().setAll(mainPane);

                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
