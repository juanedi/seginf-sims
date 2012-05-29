package ar.uba.dc.seginf.sims.messages;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * Mensaje de nuevo usuario.
 * 
 * 
 * @author Juan Edi
 * @since May 28, 2012
 */
public class NewUserMessage implements Message {

    private String username;
    private String hashType;
    private String password;
    private List<String> roles;
    
    /** Creates the NewUserMessage. */
    public NewUserMessage(final String username, final String hashType, 
                          final String password, final List<String> roles) {
        Validate.notNull(username);
        Validate.notNull(hashType);
        Validate.notNull(password);
        Validate.notNull(roles);
        this.username = username;
        this.hashType = hashType;
        this.password = password;
        this.roles = roles;
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

    /** Returns the roles. */
    public List<String> getRoles() {
        return Collections.unmodifiableList(roles);
    }
    
    
}
