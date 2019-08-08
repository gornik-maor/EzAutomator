/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.main;

import java.util.ArrayList;
import java.util.Arrays;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Abwatts
 */
@XmlRootElement(name = "action")
@XmlAccessorType (XmlAccessType.FIELD)
public class Action {

    private String action, comment, message;
    private ArrayList<Integer> coordinates;
    private ArrayList<Integer> sendKeys;
    private String delay;
    private char type;
    
    private int keyContinue, keyTerminate;

    /**
     * Default constructor
     */
    public Action() {
        this.action = "";
        this.comment = "";
        this.coordinates = new ArrayList(Arrays.asList());
        this.sendKeys = new ArrayList(Arrays.asList());
        this.delay = "0 m/s";
        this.type = 'E';
    }

    public Action(String action, String comment, ArrayList coordinates, ArrayList sendKeys, String delay, char type) {
        this.action = action;
        this.comment = comment;
        this.coordinates = coordinates;
        this.sendKeys = sendKeys;

        if (delay.equals("")) {
            this.delay = delay + "0 m/s";
        } else {
            this.delay = delay + " m/s";
        }

        this.type = type;
    }
    
    // Confirmation
    public Action(String comment, int keyContinue, int keyTerminate, String message) {
        //super("Confirmation", "", new ArrayList<>(Arrays.asList("")), new ArrayList<>(Arrays.asList(keyContinue, keyTerminate)), "", 'E');
        this.action = "Confirmation";
        this.comment = comment;
        this.keyContinue = keyContinue;
        this.keyTerminate = keyTerminate;
        this.coordinates = new ArrayList<>(Arrays.asList(0));
        this.sendKeys = new ArrayList<>(Arrays.asList(keyContinue, keyTerminate));
        this.delay = "0 m/s";
        this.type = 'E';
        this.message = message;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<Integer> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Integer> coordinates) {
        this.coordinates = coordinates;
    }

    public ArrayList<Integer> getSendKeys() {
        return sendKeys;
    }

    public void setSendKeys(ArrayList<Integer> sendKeys) {
        this.sendKeys = sendKeys;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay + " m/s";
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }
    
    // Confirmation
    public int getKeyContinue() {
        return keyContinue;
    }
    
    public void setKeyContinue(int keyContinue) {
        this.keyContinue = keyContinue;
    }
    
    public int getKeyTerminate() {
        return keyTerminate;
    }
    
    public void setKeyTerminate(int keyTerminate) {
        this.keyTerminate = keyTerminate;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }

    public void turnIntoAction(Action actionIn) {
        if (!actionIn.getAction().equals("Confirmation")) {
            this.setAction(actionIn.action);
            
            if (!actionIn.getComment().isEmpty()) {
                this.setComment(actionIn.comment);
            }
            
            this.setCoordinates(actionIn.getCoordinates());
            
            if (!actionIn.getDelay().startsWith("0")) {
                this.setDelay(actionIn.getDelay());
            }
            
            this.setSendKeys(actionIn.getSendKeys());
            this.setType(actionIn.getType());
        }
    }
    
    public void turnIntoConfirmation(Action actionIn) {
        if (actionIn != null) {
            if (!actionIn.getMessage().isEmpty()) {
                this.setMessage(actionIn.getMessage());
            }
            
            if (!actionIn.getDelay().startsWith("0")) {
                this.setDelay(actionIn.getDelay());
            }
            
            this.setKeyContinue(actionIn.getKeyContinue());
            this.setKeyTerminate(actionIn.getKeyTerminate());
            this.setSendKeys(new ArrayList<>(Arrays.asList(actionIn.getKeyContinue(), actionIn.getKeyTerminate())));
        }
    }
}
