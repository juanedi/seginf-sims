/*
 * Copyright (c) 2012 Zauber S.A.  -- All rights reserved
 */
package services;

import models.User;
import ar.uba.dc.seginf.sims.messages.UserRolesChangedMessage;

/**
 * Actualiza roles de usuarios.
 * 
 * 
 * @author Juan Edi
 * @since Jul 3, 2012
 */
public class RolesChangedMessageProcessor extends TransactionalMessageProcessor<UserRolesChangedMessage> {

    /** @see TransactionalMessageProcessor#doProcess(Message) */
    @Override
    protected void doProcess(UserRolesChangedMessage msg) {
        User user = User.find("byUsername", msg.getUsername()).first();
        if (user != null) {
            user.isAdmin = msg.getRoles().contains("ADMIN");
            user.save();
        }
    }

}
