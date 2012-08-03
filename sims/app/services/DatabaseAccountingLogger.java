package services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import models.App;
import models.Event;
import models.EventType;
import models.PasswordPolicy;
import models.Role;
import models.User;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;

/**
 * Implementación de {@link AccountingLogger} 
 * que guarda entradas en la base de datos.
 * 
 * @author Juan Edi.
 *
 */
public class DatabaseAccountingLogger implements AccountingLogger {

	private final DateProvider dateProvider;
	
	public DatabaseAccountingLogger(final DateProvider dateProvider) {
		Validate.notNull(dateProvider);
		this.dateProvider = dateProvider;
	}
	
	@Override
	public void logPasswordChange(User user) {
		log(EventType.PASSWORD_CHANGE, user, App.sims(), "El usuario " + user.username + " modificó su clave");
	}

	@Override
	public void logUserCreated(User currentUser, User newUser) {
		String description = "Usuario creado: " + newUser.username;
		log(EventType.USER_CREATED, currentUser, App.sims(), description, newUser);
	}

	@Override
	public void logAppCreated(User currentUser, App app) {
		String description = "Nueva aplicación: " + app.name;
		log(EventType.APP_CREATED, currentUser, Arrays.asList(app, App.sims()), description);
	}

	@Override
	public void logAppAccessChanged(User currentUser, User user, App app) {
		String description;
		if (user.apps.contains(app)) {
			description = "Acceso concedido a " + user.username + " para aplicación " + app.name;
		} else {
			description = "Acceso revocado a " + user.username + " para aplicación " + app.name;
		}
		
		log(EventType.APP_ACCESS_CHANGED, currentUser, app, description, user);
	}

	@Override
	public void logRoleChanged(User currentUser, User user, Role role) {
		String msg;
		if (user.roles.contains(role)) {
			msg = "Rol " + role.name + " concedido a " + user.username + " para aplicación " + role.app.name;
		} else {
			msg = "Rol " + role.name + " revocado de " + user.username + " para aplicación " + role.app.name;
		}
		
		log(EventType.ROLE_CHANGED, currentUser, role.app, msg, user);
	}
	
	@Override
	public void logPasswordPolicyCreated(User currentUser, PasswordPolicy policy) {
		log(EventType.PASSWORD_POLICY_CREATED, currentUser, Collections.<App>emptyList(), 
			"Nueva política de passwords creada: " + policy.name);
	}

	@Override
	public void logPasswordPolicyActivated(User currentUser, PasswordPolicy policy) {
		log(EventType.PASSWORD_POLICY_CHANGE, currentUser, Collections.<App>emptyList(), 
			"Política de passwords activada: " + policy.name);
	}

	
	private void log(EventType type, User responsible, App app, String description, User ... additionalRelatedUsers) {
		log(type,responsible, Collections.<App>singletonList(app), description, additionalRelatedUsers);
	}
	
	private void log(EventType type, User responsible, List<App> apps, String description, User ... additionalRelatedUsers) {
		Event event = new Event(dateProvider.getDate(), type, responsible, apps, 
				  				Arrays.asList(additionalRelatedUsers), description);
		event.save();
	}
}
