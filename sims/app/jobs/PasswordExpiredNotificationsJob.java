package jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import notifiers.Mailer;

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
public class PasswordExpiredNotificationsJob extends Job {

	@Inject static AccountingLogger accountingLogger;
	@Inject static AppNotificationService notificationService; 
	
	public void doJob() {
		if (PasswordPolicy.current() != null) {
			List<User> expiredUsers = PasswordPolicy.usersWithPasswordsExpiringToday();
			for (User user : expiredUsers) {
				accountingLogger.logPasswordExpired(user);
				notificationService.broadcastPasswordExpired(user);
				Mailer.passwordExpired(user);
			}
		}
	}
	
}
