package controllers;

import play.modules.spring.Spring;


/**
 * Autentica contra el LDAP
 * 
 * 
 * @author Juan Edi
 * @since May 26, 2012
 */
public class Security extends controllers.Secure.Security {

    static boolean authenticate(final String username, final String password) {
        return false;
    }
    
}
