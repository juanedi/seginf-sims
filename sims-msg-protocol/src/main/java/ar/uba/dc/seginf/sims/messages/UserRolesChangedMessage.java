package ar.uba.dc.seginf.sims.messages;

import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * Mensaje que indica que cambiaron los roles de un usuario para cierta aplicación.
 * 
 * 
 * @author Juan Edi
 * @since Jul 3, 2012
 */
public class UserRolesChangedMessage implements Message {

    private String username;
    private List<String> roles;
    
    /** Creates the UserRolesChangedMessage. */
    public UserRolesChangedMessage(String username, List<String> roles) {
        Validate.notEmpty(username);
        Validate.notNull(roles);
        this.username = username;
        this.roles = roles;
    }
    public String getUsername() {
        return username;
    }
    public List<String> getRoles() {
        return roles;
    }
    
}
