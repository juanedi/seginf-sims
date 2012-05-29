package services;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

import ar.uba.dc.seginf.sims.MessageType;
import ar.uba.dc.seginf.sims.messages.NewUserMessage;

import models.App;
import models.Role;
import models.User;

/**
 * Notifica a las aplicaciones vía RabbitMQ.
 * Envía los mensajes a un exchange con el nombre de la aplicación,
 * que distribuye los mensajes a distintas colas según el tipo de mensaje.
 * 
 * 
 * @author Juan Edi
 * @since May 29, 2012
 */
public class RMQAppNotificationService implements AppNotificationService {

    private final AmqpTemplate newUserTemplate;
    
    /** Creates the RMQAppNotificationService. */
    public RMQAppNotificationService(final AmqpTemplate newUserTemplate) {
        this.newUserTemplate = newUserTemplate;
    }
    
    /** @see AppNotificationService#notifyNewUser(User, App) */
    @Override
    public void notifyNewUser(User user, App app) {
        List<String> roles = roleNames(user.getRoles(app));
        NewUserMessage msg = new NewUserMessage(user.username, app.hashType.name(), 
                                                user.getHashedPassword(app.hashType), roles);
        newUserTemplate.convertAndSend(app.name, MessageType.NEW_USER.name(), msg, nullPostProcessor());
    }

    /** @see AppNotificationService#notifyPasswordChanged(User, App) */
    @Override
    public void notifyPasswordChanged(User user, App app) {
        throw new NotImplementedException();
    }

    /** @see AppNotificationService#notifyRolesChanged(User, App) */
    @Override
    public void notifyRolesChanged(User user, App app) {
        throw new NotImplementedException();
    }

    /** @see AppNotificationService#notifyUserRemove(User) */
    @Override
    public void notifyUserRemove(User user) {
        throw new NotImplementedException();
    }

    private static List<String> roleNames(final List<Role> roles) {
        List<String> ret = new LinkedList<String>();
        for (Role role : roles) {
            ret.add(role.name);
        }
        return ret;
    }
    
    /** retorna un post processor que no modifica el mensaje. */
    private MessagePostProcessor nullPostProcessor() {
        return new MessagePostProcessor() {
            
            @Override
            public Message postProcessMessage(Message arg0) throws AmqpException {
                return arg0;
            }
        };
    }
}
