package services;

import models.App;
import models.PasswordPolicy;
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
	
	void logUserCreated(User currentUser, User newUser);
	
	void logAppCreated(User currentUser, App app);

	void logAppAccessChanged(User currentUser, User user, App app);

	void logRoleChanged(User currentUser, User user, Role role);
	
	void logPasswordPolicyCreated(User currentUser, PasswordPolicy policy);
	
	void logPasswordPolicyActivated(User currentUser, PasswordPolicy policy);
}
