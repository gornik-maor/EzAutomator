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
    private ArrayList coordinates;
    private int delay;
    
    /**
     * Default constructor
     */
    public Action() {
        this.action = "";
        this.comment = "";
        this.coordinates = new ArrayList(Arrays.asList());
        this.delay = 0;
    }
    
    public Action(String action, String comment, ArrayList coordinates, int delay) {
        this.action = action;
        this.comment = comment;
        this.coordinates = coordinates;
        this.delay = delay;
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

    public ArrayList getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList coordinates) {
        this.coordinates = coordinates;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
