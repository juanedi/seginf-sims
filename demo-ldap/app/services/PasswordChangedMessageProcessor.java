package services;

import models.User;

import org.apache.commons.lang.Validate;

import ar.uba.dc.seginf.sims.messages.PasswordChangedMessage;
import ar.uba.dc.seginf.sims.util.MessageProcessor;

public class PasswordChangedMessageProcessor implements MessageProcessor<PasswordChangedMessage> {

	private final LDAPService ldapService;
	
	/** construye el msg processor */
	public PasswordChangedMessageProcessor(LDAPService ldapService) {
		Validate.notNull(ldapService);
		this.ldapService = ldapService;
	}
	
	@Override
	public void process(PasswordChangedMessage msg) {
		User user = new User();
		user.username = msg.getUsername();
		user.password = msg.getPassword();
		ldapService.changePassword(user);
	}

}
