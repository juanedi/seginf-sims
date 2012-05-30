package services;

import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.db.jpa.Transactional;
import models.User;
import ar.uba.dc.seginf.sims.messages.NewUserMessage;

/**
 * Crea nuevos usuarios.
 * 
 * 
 * @author Juan Edi
 * @since May 29, 2012
 */
public class NewUserMessageProcessor extends TransactionalMessageProcessor<NewUserMessage> {

    /** @see services.TransactionalMessageProcessor#doProcess(Message) */
    @Override
    protected void doProcess(NewUserMessage msg) {
        boolean isAdmin = msg.getRoles().contains("ADMIN");
        User user = new User(msg.getUsername(), msg.getPassword(), isAdmin);
        user.save();
    }
    
}
