package controllers;

import models.User;

/**
 * Autentica contra la base de datos. 
 * 
 * 
 * @author Juan Edi
 * @since May 26, 2012
 */
public class Security extends controllers.Secure.Security {

    static boolean authenticate(final String username, final String password) {
        User user = User.find("byUsername", username).first();
        return user != null && user.comparePassword(password);
    }
    
}
