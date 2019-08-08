/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.main;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Abwatts
 */
@XmlRootElement(name = "actions")
@XmlAccessorType(XmlAccessType.FIELD)
public class Actions {

    @XmlElement(name = "action")
    private List<Action> actions = new ArrayList<>();

    public List<Action> getActions() {
//        actions.forEach((Action action) -> {
//            if (action.getAction().equals("Confirmation")) {
//                Confirmation confirmation = (Confirmation) action;
//            }
//        });

        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
