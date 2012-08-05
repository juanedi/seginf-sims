package services;

import models.User;

import org.apache.commons.lang.Validate;

import ar.uba.dc.seginf.sims.messages.PasswordExpiredMessage;
import ar.uba.dc.seginf.sims.util.MessageProcessor;

public class PasswordExpiredMessageProcessor implements MessageProcessor<PasswordExpiredMessage> {

	private final LDAPService ldapService;
	
	/** contruye el msg processor */
	public PasswordExpiredMessageProcessor(LDAPService ldapService) {
		Validate.notNull(ldapService);
		this.ldapService = ldapService;
	}

	@Override
	public void process(PasswordExpiredMessage msg) {
		User user = new User();
		user.username = msg.getUsername();
		ldapService.invalidatePassword(user);
	}

}
