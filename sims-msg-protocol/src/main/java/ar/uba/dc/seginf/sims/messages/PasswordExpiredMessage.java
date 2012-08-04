package ar.uba.dc.seginf.sims.messages;

import org.apache.commons.lang.Validate;

/**
 * Mensaje que indica que la clave de un usuario expir—.
 * 
 * @author jedi
 *
 */
public class PasswordExpiredMessage implements Message {

    private String username;
    private String hashType;    
    private String password;
    
    /** construye un nuevo mensaje de clave expirada */
	public PasswordExpiredMessage(String username, String hashType, String password) {
		Validate.notEmpty(username);
		Validate.notEmpty(username);
		Validate.notEmpty(username);
		this.username = username;
		this.hashType = hashType;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getHashType() {
		return hashType;
	}

	public String getPassword() {
		return password;
	}

	
}
