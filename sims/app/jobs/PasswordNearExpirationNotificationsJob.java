package jobs;

import java.util.LinkedList;
import java.util.List;

import models.PasswordPolicy;
import models.User;
import notifiers.Mailer;
import play.jobs.Every;
import play.jobs.Job;

/**
 * Job que informa a un usuario cuando la clave está pronta a vencer (si faltan 7 o 15 días)
 * 
 * @author jedi
 *
 */
@Every("1d")
public class PasswordNearExpirationNotificationsJob extends Job {

	public void doJob() {
		if (PasswordPolicy.current() != null) {
			notifyUsersWithPasswordExpiring(7);
			notifyUsersWithPasswordExpiring(15);
		}
	}
	
	/** notifica los usuarios cuya clave vence en N días */
	private void notifyUsersWithPasswordExpiring(int days) {
		List<User> toNotify = PasswordPolicy.usersWithPasswordsNearExpiration(days);
		for (User user : toNotify) {
			Mailer.passwordNearExpiration(user, days);
		}
	}
}
