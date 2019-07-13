/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.alert;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Abwatts
 */
public class AlertController implements Initializable {

    @FXML
    private Button btnTwo;

    @FXML
    private ImageView closeBtn;

    @FXML
    private Text displayLbl;

    @FXML
    private Button btnOne;

    @FXML
    void closeApp(MouseEvent event) {
        // Close and return false
        result = false;
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
    void onBtnOneMousePressed(MouseEvent event) {
        switch (btnOneID) {
            case 0:
                result = false;
                break;
            case 1:
                result = true;
                break;
        }
        closeForm();
    }

    @FXML
    void onBtnTwoMousePressed(MouseEvent event) {
        switch (btnTwoID) {
            case 0:
                result = false;
                break;
            case 1:
                result = true;
                break;
        }

        closeForm();
    }

    private boolean result;

    private int btnOneID;

    private int btnTwoID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setMessage(String message) {
        if (!message.isEmpty()) {
            displayLbl.setText(message);
        }
    }

    public void setBtnOneTxt(String text) {
        if (!text.isEmpty()) {
            btnOne.setText(text);
        }
    }

    public void setBtnTwoTxt(String text) {
        if (!text.isEmpty()) {
            btnTwo.setText(text);
        }
    }

    public boolean getResult() {
        return result;
    }

    public void setYesNo(int btnOne, int btnTwo) {
        if (btnOne == 0 && btnTwo == 0) {
            btnOneID = btnOne;
            btnTwoID = btnTwo;
        }
    }

    private void closeForm() {
        Stage currStage = (Stage) closeBtn.getScene().getWindow();
        currStage.close();
    }

}
