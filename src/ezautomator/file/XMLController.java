/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.file;

import ezautomator.main.Action;
import ezautomator.main.Actions;
import java.io.File;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Abwatts
 */
public class XMLController {

    public static Action XMLtoAction(String filename) throws Exception {
        File file = new File(filename);
        JAXBContext jaxbContext = JAXBContext.newInstance(Action.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (Action) jaxbUnmarshaller.unmarshal(file);
    }

    public static void ActionToXML(String filename, Action action) throws Exception {
        File file = new File(filename);
        JAXBContext jaxbContext = JAXBContext.newInstance(Action.class);

        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(action, file);
        jaxbMarshaller.marshal(action, System.out);
    }
    
    public static void ActionsToXML(String filename, Actions actions) throws JAXBException
    {
        JAXBContext jaxbContext = JAXBContext.newInstance(Actions.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        //Marshal the products list in console
        jaxbMarshaller.marshal(actions, System.out);

        //Marshal the products list in file
        jaxbMarshaller.marshal(actions, new File(filename));
    }
    
     public static Actions XMLtoActions(String filename) throws Exception {
        File file = new File(filename);
        JAXBContext jaxbContext = JAXBContext.newInstance(Actions.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (Actions) jaxbUnmarshaller.unmarshal(file);
    }


    public static Action loadAction(String fileDir) throws Exception {
        Action tempAction = XMLtoAction(fileDir); //"person.xml"
        System.out.println(tempAction);
        return tempAction;
    }
}
