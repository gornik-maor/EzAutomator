/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.main;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Abwatts
 */
public class Confirmation extends Action {

    private String action, comment, keyContinue, keyTerminate, message;

    public Confirmation() {
        super("Confirmation", "", new ArrayList<>(Arrays.asList("")),  new ArrayList<>(Arrays.asList("")), "", 'E');
        this.action = "Confirmation";
        this.comment = this.keyContinue = this.keyTerminate = this.message = "";
    }

    public Confirmation(String comment, String keyContinue, String keyTerminate, String message) {
        super("Confirmation", "", new ArrayList<>(Arrays.asList("")),  new ArrayList<>(Arrays.asList(keyContinue, keyTerminate)), "", 'E');
        this.action = "Confirmation";
        this.comment = comment;
        this.keyContinue = keyContinue;
        this.keyTerminate = keyTerminate;
        this.message = message;
    }

    public String getKeyContinue() {
        return keyContinue;
    }

    public void setKeyContinue(String keyContinue) {
        this.keyContinue = keyContinue;
    }

    public String getKeyTerminate() {
        return keyTerminate;
    }

    public void setKeyTerminate(String keyTerminate) {
        this.keyTerminate = keyTerminate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
