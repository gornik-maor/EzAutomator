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

    /**
     * Constructor containing all required properties of action
     * @param action
     * @param comment
     * @param coordinates
     * @param sendKeys
     * @param delay
     * @param type 
     */
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
    
    /**
     * Constructor containing all required properties of confirmation
     * @param comment
     * @param keyContinue
     * @param keyTerminate
     * @param message 
     */
    public Action(String comment, int keyContinue, int keyTerminate, String message) {
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

    /**
     * Getter for action
     * @return Action
     */
    public String getAction() {
        return action;
    }

    /**
     * Setter for action
     * @param action 
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Getter for comment
     * @return Comment
     */
    public String getComment() {
        return comment;
    }

    /** 
     * Setter for comment
     * @param comment 
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Getter for list containing screen coordinates
     * @return List of screen coordinates
     */
    public ArrayList<Integer> getCoordinates() {
        return coordinates;
    }

    /**
     * Setter for list containing screen coordinates
     * @param coordinates 
     */
    public void setCoordinates(ArrayList<Integer> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Getter for list containing keys to send
     * @return List of screen coordinates
     */
    public ArrayList<Integer> getSendKeys() {
        return sendKeys;
    }

    /**
     * Setter for list containing keys to send
     * @param sendKeys 
     */
    public void setSendKeys(ArrayList<Integer> sendKeys) {
        this.sendKeys = sendKeys;
    }

    /**
     * Getter for specified action delay
     * @return Delay
     */
    public String getDelay() {
        return delay;
    }

    /**
     * Setter for action delay
     * @param delay 
     */
    public void setDelay(String delay) {
        this.delay = delay + " m/s";
    }

    /**
     * Getter for action type
     * @return type
     */
    public char getType() {
        return type;
    }

    /**
     * Setter for action type
     * @param type 
     */
    public void setType(char type) {
        this.type = type;
    }
    
    /**
     * Getter for specified continuation key for confirmation
     * @return Selected key
     */
    public int getKeyContinue() {
        return keyContinue;
    }
    
    /**
     * Setter for specified continuation key for confirmation
     * @param keyContinue 
     */
    public void setKeyContinue(int keyContinue) {
        this.keyContinue = keyContinue;
    }
    
    /**
     * Getter for specified termination key for confirmation
     * @return Selected key
     */
    public int getKeyTerminate() {
        return keyTerminate;
    }
    
    /**
     * Setter for specified termination key for confirmation
     * @param keyTerminate 
     */
    public void setKeyTerminate(int keyTerminate) {
        this.keyTerminate = keyTerminate;
    }
    
    /**
     * Getter for entered confirmation message
     * @return Entered message by the user
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Setter for confirmation message
     * @param message 
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Turning one action into a different one without linking them to the
     * same memory location
     * @param actionIn 
     */
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
    
    /**
     * Turning one confirmation into a different one without linking them to the
     * same memory location
     * @param actionIn 
     */
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
