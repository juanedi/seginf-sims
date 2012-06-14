package services;

import models.User;

import org.apache.commons.lang.Validate;

import ar.uba.dc.seginf.sims.messages.NewUserMessage;
import ar.uba.dc.seginf.sims.util.MessageProcessor;

/**
 * Crea nuevos usuarios.
 * 
 * 
 * @author Juan Edi
 * @since May 29, 2012
 */
public class NewUserMessageProcessor implements MessageProcessor<NewUserMessage> {

    private final LDAPService ldapService;
    
    public NewUserMessageProcessor(final LDAPService ldapService) {
        this.ldapService = ldapService;
    }
    
    /** @see services.MessageProcessor#process(ar.uba.dc.seginf.sims.messages.Message) */
    @Override
    public void process(NewUserMessage msg) {
        // TODO: Validar que el usuario no exista y que todos los grupos sean válidos.
        // FIXME: MANEJO DE ERROR DE COMUNICACIÓN CON LDAP.
        Validate.isTrue("MD5".equals(msg.getHashType()));
        User user = new User();
        user.username = msg.getUsername();
        user.firstName = msg.getFirstName();
        user.lastName = msg.getLastName();
        user.password = msg.getPassword();
        user.groups = msg.getRoles();
        ldapService.createUser(user);
    }
    
}
