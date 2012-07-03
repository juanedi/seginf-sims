package services;

import models.User;

import org.apache.commons.lang.Validate;
import org.springframework.amqp.core.MessagePostProcessor;

import play.Logger;

import ar.uba.dc.seginf.sims.messages.UserRolesChangedMessage;
import ar.uba.dc.seginf.sims.util.MessageProcessor;

/**
 * Actualiza roles de los usuarios
 * 
 * 
 * @author Juan Edi
 * @since Jul 3, 2012
 */
public class RolesChangedMessageProcessor implements MessageProcessor<UserRolesChangedMessage> {

    private final LDAPService ldapService;
    
    /** Creates the RolesChangedMessageProcessor. */
    public RolesChangedMessageProcessor(final LDAPService ldapService) {
        Validate.notNull(ldapService);
        this.ldapService = ldapService;
    }
    
    /** @see MessageProcessor#process(Message) */
    @Override
    public void process(UserRolesChangedMessage msg) {
        User user = new User();
        user.username = msg.getUsername();
        user.groups = msg.getRoles();
        ldapService.updateRoles(user);
    }

}
