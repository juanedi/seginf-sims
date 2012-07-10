package ar.uba.dc.seginf.sims.messages;

import org.apache.commons.lang.Validate;

/**
 * Mensaje de cambio de clave.
 * 
 * 
 * @author Ruben Festini
 * @since Jun 28, 2012
 */
public class PasswordChangedMessage implements Message {
	
    private String username;
    private String hashType;    
    private String password;
    
    public PasswordChangedMessage(final String username, final String hashType, final String password) {
		Validate.notEmpty(username);
		Validate.notNull(hashType);
		Validate.notNull(password);
		this.username = username;
		this.hashType = hashType;
		this.password = password;
}

    /** Returns the username. */
    public String getUsername() {
        return username;
    }

    /** Returns the hashType. */
    public String getHashType() {
        return hashType;
    }

    /** Returns the password. */
    public String getPassword() {
        return password;
    }
}
