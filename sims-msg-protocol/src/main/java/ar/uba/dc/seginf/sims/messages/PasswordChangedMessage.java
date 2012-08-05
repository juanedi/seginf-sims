package ar.uba.dc.seginf.sims.messages;

import java.util.Date;

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
    private Date passwordExpiration;
    
    public PasswordChangedMessage(final String username, final String hashType, final String password, 
    							  final Date passwordExpiration) {
		Validate.notEmpty(username);
		Validate.notNull(hashType);
		Validate.notNull(password);
		Validate.notNull(passwordExpiration);
		this.username = username;
		this.hashType = hashType;
		this.password = password;
		this.passwordExpiration = passwordExpiration;
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
    
    /** Date the new password expires */
    public Date getPasswordExpiration() {
    	return passwordExpiration;
    }
    
}
