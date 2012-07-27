package services;

import java.util.Collections;

import org.apache.commons.lang.NotImplementedException;

import models.App;
import models.Event;
import models.EventType;
import models.Role;
import models.User;

/**
 * Implementación de {@link AccountingLogger} 
 * que guarda entradas en la base de datos.
 * 
 * @author Juan Edi.
 *
 */
public class DatabaseAccountingLogger implements AccountingLogger {

	@Override
	public void logPasswordChange(User user) {
		new Event(EventType.PASSWORD_CHANGE, 
				  Collections.<App>singletonList(App.sims()),
				  Collections.<User>singletonList(user), 
				  "El usuario " + user.username + " modificó su clave.").save();
	}

	@Override
	public void logUserCreated(User newUser) {
		new Event(EventType.USER_CREATED, 
				  Collections.<App>singletonList(App.sims()),
				  Collections.<User>singletonList(newUser), 
				  "Nuevo usuario: " + newUser.username + ".").save();
	}

	@Override
	public void logAppCreated(App app) {
		new Event(EventType.APP_CREATED, 
				  Collections.<App>singletonList(App.sims()),
				  Collections.<User>emptyList(), 
				  "Nueva aplicación: " + app.name + ".").save();
	}

	@Override
	public void logAppAccessChanged(User user, App app) {
		String msg;
		if (user.apps.contains(app)) {
			msg = "Acceso concedido a " + user.username + " para aplicación " + app.name;
		} else {
			msg = "Acceso revocado a " + user.username + " para aplicación " + app.name;
		}
		
		new Event(EventType.APP_ACCESS_CHANGED, 
				  Collections.<App>singletonList(app),
				  Collections.<User>singletonList(user), 
				  msg).save();
	}

	@Override
	public void logRoleChanged(User user, Role role) {
		String msg;
		if (user.roles.contains(role)) {
			msg = "Rol " + role.name + " concedido a " + user.username + " para aplicación " + role.app.name;
		} else {
			msg = "Rol " + role.name + " revocado de " + user.username + " para aplicación " + role.app.name;
		}
		
		new Event(EventType.ROLE_CHANGED, 
				  Collections.<App>singletonList(role.app),
				  Collections.<User>singletonList(user), 
				  msg).save();
	}

	@Override
	public void logPasswordPolicyChanged() {
		//FIXME: completar cuando esté terminado el tema de políticas de claves!
		throw new NotImplementedException();
	}

}
