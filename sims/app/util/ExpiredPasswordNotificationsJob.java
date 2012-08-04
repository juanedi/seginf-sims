package util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;

import models.PasswordPolicy;
import models.User;

import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import services.AccountingLogger;
import services.AppNotificationService;

/**
 * Job diario que chequea claves vencidas y notifica a los usuarios.
 * 
 * @author jedi
 */
@Every("1d")
public class ExpiredPasswordNotificationsJob extends Job {

	@Inject static AccountingLogger accountingLogger;
	@Inject static AppNotificationService notificationService; 
	
	public void doJob() {
		if (PasswordPolicy.current() != null) {
			List<User> expiredUsers = PasswordPolicy.usersWithExpiredPasswords();
			for (User user : expiredUsers) {
				System.out.println("tu vieja");
				accountingLogger.logPasswordExpired(user);
				notificationService.broadcastPasswordExpired(user);
			}
		}
	}
	
}
