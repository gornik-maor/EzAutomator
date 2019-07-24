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
public class Action {

    private String action;
    private String comment;
    private ArrayList<Integer> coordinates;
    private ArrayList<Integer> sendKeys;
    private String delay;
    private char type;

    /**
     * Default constructor
     */
    public Action() {
        this.action = "";
        this.comment = "";
        this.coordinates = new ArrayList(Arrays.asList());
        this.sendKeys = new ArrayList(Arrays.asList());
        this.delay = "0";
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
}
