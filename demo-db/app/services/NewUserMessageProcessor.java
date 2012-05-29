package services;

import models.User;
import ar.uba.dc.seginf.sims.messages.NewUserMessage;

/**
 * Crea nuevos usuarios.
 * 
 * 
 * @author Juan Edi
 * @since May 29, 2012
 */
public class NewUserMessageProcessor {
    
    public void process(NewUserMessage message) {
        System.out.println("Llegó un mensaje :)");
    }
    
}
