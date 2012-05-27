package controllers;

import models.App;
import models.User;

/**
 * Autentica contra la base de datos. 
 * 
 * 
 * @author Juan Edi
 * @since May 26, 2012
 */
public class Security extends controllers.Secure.Security {

    /** Autentica un usuario */
    static boolean authenticate(final String username, final String password) {
        User user = getUser(username);
        return user != null && user.comparePassword(password);
    }
 
    /** Verifica si un usuario tiene cierto rol */
    static boolean check(final String profile) {
        boolean ret = false;
        User user = getUser(connected());
        
        if (user != null && profile != null) {
            return user.hasRole(App.SIMS_APP_NAME, profile);
        }
        
        return ret;
    }

    /** Obtiene el usuario de la base de datos */
    static private User getUser(final String username) {
        User user = null;
        if (username != null) {
            user = User.find("byUsername", username).first();
        }
        return user;
    }
}
