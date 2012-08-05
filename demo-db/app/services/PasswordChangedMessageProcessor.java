package services;

import org.apache.commons.lang.Validate;

import models.User;
import ar.uba.dc.seginf.sims.messages.Message;
import ar.uba.dc.seginf.sims.messages.NewUserMessage;
import ar.uba.dc.seginf.sims.messages.PasswordChangedMessage;

/**
 * Cambia la clave de un usuario.
 * 
 * 
 * @author Ruben Festini
 * @since Jun 30, 2012
 */

public class PasswordChangedMessageProcessor  extends TransactionalMessageProcessor<PasswordChangedMessage> {
    /** @see services.TransactionalMessageProcessor#doProcess(Message) */
    @Override
    protected void doProcess(PasswordChangedMessage msg) {
        User user = User.find("byUsername", msg.getUsername()).first();
        user.setPassword(msg.getPassword());
        user.save();
    }
}
