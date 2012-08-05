package services;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

import ar.uba.dc.seginf.sims.MessageType;
import ar.uba.dc.seginf.sims.messages.NewUserMessage;
import ar.uba.dc.seginf.sims.messages.PasswordExpiredMessage;
import ar.uba.dc.seginf.sims.messages.UserRolesChangedMessage;
import ar.uba.dc.seginf.sims.messages.PasswordChangedMessage;

import models.App;
import models.PasswordPolicy;
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
    private final AmqpTemplate userRolesChangedTemplate;
    private final AmqpTemplate passwordChangedTemplate;
    private final AmqpTemplate passwordExpiredTemplate;
    
    /** Creates the RMQAppNotificationService. */
    public RMQAppNotificationService(final AmqpTemplate newUserTemplate,
                                     final AmqpTemplate userRolesChagedTemplate,
                                     final AmqpTemplate passwordChangedTemplate,
                                     final AmqpTemplate passwordExpiredTemplate) {
        Validate.notNull(newUserTemplate);
        Validate.notNull(userRolesChagedTemplate);
        Validate.notNull(passwordChangedTemplate);
        Validate.notNull(passwordExpiredTemplate);

        this.newUserTemplate = newUserTemplate;
        this.userRolesChangedTemplate = userRolesChagedTemplate;
        this.passwordChangedTemplate = passwordChangedTemplate;
        this.passwordExpiredTemplate = passwordExpiredTemplate;
    }
    
    /** @see AppNotificationService#notifyNewUser(User, App) */
    @Override
    public void notifyNewUser(User user, App app) {
        List<String> roles = roleNames(user.getRoles(app));
        NewUserMessage msg = new NewUserMessage(user.username, user.firstName, user.lastName,
                                                app.hashType.name(), user.getHashedPassword(app.hashType),
                                                PasswordPolicy.current().expirationDateForNewPassword(), roles);
        newUserTemplate.convertAndSend(app.name, MessageType.NEW_USER.name(), msg, nullPostProcessor());
    }

    /** @see AppNotificationService#broadcastPasswordChanged(User, App) */
    @Override
    public void broadcastPasswordChanged(User user) {
    	// TODO: Esto se podría hacer via un exchange de RMQ.
        for (App app : user.apps) {
            if (!app.name.equals(App.SIMS_APP_NAME)) {
            	PasswordChangedMessage msg = new PasswordChangedMessage(user.username, app.hashType.name(), 
        																user.getHashedPassword(app.hashType),
        																PasswordPolicy.current().expirationDateForNewPassword());    
            	passwordChangedTemplate.convertAndSend(app.name, MessageType.CHANGE_PASSWORD.name(), msg, nullPostProcessor());
            }
        }
    }

	@Override
	public void broadcastPasswordExpired(User user) {
    	// TODO: Esto se podría hacer via un exchange de RMQ.
        for (App app : user.apps) {
            if (!app.name.equals(App.SIMS_APP_NAME)) {
            	PasswordExpiredMessage msg = new PasswordExpiredMessage(user.username, app.hashType.name(), 
        																user.getHashedPassword(app.hashType));    
            	passwordExpiredTemplate.convertAndSend(app.name, MessageType.PASSWORD_EXPIRED.name(), msg, nullPostProcessor());
            }
        }
	}
    
    /** @see AppNotificationService#notifyRolesChanged(User, App) */
    @Override
    public void notifyRolesChanged(User user, App app) {
        List<String> roles = roleNames(user.getRoles(app));
        UserRolesChangedMessage msg = new UserRolesChangedMessage(user.username, roles);
        userRolesChangedTemplate.convertAndSend(app.name, MessageType.CHANGE_ROLES.name(), msg, nullPostProcessor());
    }

    /** @see AppNotificationService#notifyUserRemove(User, App) */
    @Override
    public void notifyUserRemove(User user, App app) {
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
