package ar.uba.dc.seginf.sims.messages;

import org.apache.commons.lang.Validate;

/**
 * Mensaje de usuario removido de aplicaci—n.
 * 
 * @author jedi
 *
 */
public class UserRemovedMessage implements Message {

	private String username;

	public UserRemovedMessage(String username) {
		Validate.notEmpty(username);
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
	
}
