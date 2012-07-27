package services;

import models.App;
import models.Role;
import models.User;

/** 
 * Servicio de registro de auditoría.
 * 
 * @author Juan Edi
 *
 */
public interface AccountingLogger {

	void logPasswordChange(User user);
	
	void logUserCreated(User newUser);
	
	void logAppCreated(App app);

	void logAppAccessChanged(User user, App app);

	void logRoleChanged(User user, Role role);
	
	void logPasswordPolicyChanged();
}
