/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.file;

import com.jfoenix.controls.JFXTextField;
import ezautomator.alert.AlertController;
import ezautomator.main.Action;
import ezautomator.main.Actions;
import ezautomator.main.EzAutomator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.xml.bind.JAXBException;

/**
 * FXML Controller class
 *
 * @author gornicma
 */
public class NameXMLController implements Initializable {

    @FXML
    private JFXTextField scriptTxt;

    @FXML
    private ImageView closeBtn;

    @FXML
    private StackPane root;

    @FXML
    private ImageView icon;

    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnPath;

    @FXML
    void closeApp(MouseEvent event) {
        closeForm();
    }

    @FXML
    void closeBtnChangeHover(MouseEvent event) {
        closeBtn.setImage(new Image("/ezautomator/icons/close-hover.png"));
    }

    @FXML
    void closeBtnChangeLeave(MouseEvent event) {
        closeBtn.setImage(new Image("/ezautomator/icons/close.png"));
    }

    @FXML
    void OnPathSelection(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("eXtensible Markup Language", "*.xml");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().add(extFilter);

        // Saving the script
        Actions actions = new Actions();
        actionTable.getItems().forEach(actions.getActions()::add);

        //
        //Show save file dialog
        File file = fileChooser.showSaveDialog(getCurrStage());

        if (file != null) {
            writeActionsToXML(actions, file.toString());
            closeForm();
        }
    }

    @FXML
    void onConfirmation(MouseEvent event) {
        if (!scriptTxt.getText().isEmpty() && !actionTable.getItems().isEmpty()) {
//            Actions actions = new Actions();
//            actionTable.getItems().forEach(actions.getActions()::add);
//
//            if (!new File(path + "\\" + scriptTxt.getText() + ".xml").exists()) {
//                writeActionsToXML(actions);
//                closeForm();
//            } else {
//                if (new AlertController().loadAlert().showDialog("Yes", "No", "A script with that name already exists! Replace it?", "warning",
//                        getCurrStage(), getCurrStage(), 0.5)) {
//                    writeActionsToXML(actions);
//                    closeForm();
//                } else {
//                    scriptTxt.setText("");
//                }
//            }
        } else {
            new AlertController().loadAlert().showDialog("Ok", "Cancel", "Please enter a script name!", "error",
                    getCurrStage(), getCurrStage(), 0.5);
        }
    }

    TableView<Action> actionTable;
    private double diffX, diffY;
    private Stage tStage;
    private String path;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        closeBtn.requestFocus();
        /**
         * Allowing the user to drag the form
         */
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Calculating the difference between the cursor and the stage to later add it to allow the cursor to stay at the same
                // position without teleporting it to a different place
                diffX = getCurrStage().getX() - event.getScreenX();
                diffY = getCurrStage().getY() - event.getScreenY();
            }
        });

        // Ensuring that the stage moves a fixed number (that we calculated when the user first clicked the stage) of spaces
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Helps my cursor stay at the same position when dragging the window instead of teleporting to the top most-left corner
                getCurrStage().setX(event.getScreenX() + diffX);
                getCurrStage().setY(event.getScreenY() + diffY);
            }
        });
    }

    /**
     * Retrieving the current stage instance reference
     *
     * @return
     */
    private Stage getCurrStage() {
        Stage currStage = (Stage) root.getScene().getWindow();
        return currStage;
    }

    private void closeForm() {
        tStage.setOpacity(1);
        getCurrStage().close();
    }

    public NameXMLController loadResources() {
        try {

            FXMLLoader fxmlLoader
                    = new FXMLLoader(getClass().getResource("/ezautomator/file/NameXML.fxml"));
            StackPane namePane = fxmlLoader.load();
            Stage nameStage = new Stage();
            nameStage.initStyle(StageStyle.UNDECORATED);
            nameStage.initModality(Modality.APPLICATION_MODAL);
            nameStage.getIcons().add(new Image("/ezautomator/icons/icon.png"));
            nameStage.setTitle("EzAutomator");
            nameStage.setScene(new Scene(namePane));
            return fxmlLoader.getController();

        } catch (IOException ex) {
            Logger.getLogger(AlertController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void loadForm(TableView<Action> actionTable, Stage tStage, double opValue) {
        if (tStage != null) {
            this.tStage = tStage;
            tStage.setOpacity(opValue);
        }

        if (!actionTable.getItems().isEmpty()) {
            this.actionTable = actionTable;
        }

        getCurrStage().show();
    }

    private void writeActionsToXML(Actions actions, String file) {
        if (!actions.getActions().isEmpty()) {
            try {
//                XMLController.ActionsToXML(path + "\\" + scriptTxt.getText() + ".xml", actions);
                XMLController.ActionsToXML(file, actions);
                new AlertController().loadAlert().showDialog("Ok", "Cancel", "Script was saved successfully!", "exclamation",
                        getCurrStage(), getCurrStage(), 0.5);

            } catch (JAXBException ex) {
                new AlertController().loadAlert().showDialog("Ok", "Cancel", "Oh no! Something went wrong.", "error",
                        getCurrStage(), getCurrStage(), 0.5);
            }
        }
    }
}
