package controllers;

import javax.inject.Inject;

import authentication.LDAPService;

import play.modules.spring.Spring;


/**
 * Autentica contra el LDAP
 * 
 * 
 * @author Juan Edi
 * @since May 26, 2012
 */
public class Security extends controllers.Secure.Security {

    @Inject private static LDAPService auth;
    
    static boolean authenticate(final String username, final String password) {
        return auth.authenticate(username, password);
    }
    
}
