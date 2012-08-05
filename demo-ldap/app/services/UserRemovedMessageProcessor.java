package services;

import models.User;

import org.apache.commons.lang.Validate;

import ar.uba.dc.seginf.sims.messages.UserRemovedMessage;
import ar.uba.dc.seginf.sims.util.MessageProcessor;

public class UserRemovedMessageProcessor implements MessageProcessor<UserRemovedMessage> {

	private LDAPService ldapService;
	
	
	/** construye el msg processor */
	public UserRemovedMessageProcessor(LDAPService ldapService) {
		Validate.notNull(ldapService);
		this.ldapService = ldapService;
	}


	@Override
	public void process(UserRemovedMessage msg) {
		User user = new User();
		user.username = msg.getUsername();
		ldapService.removeUser(user);
	}

}
