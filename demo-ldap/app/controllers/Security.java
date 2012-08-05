package controllers;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;


import play.modules.spring.Spring;
import services.LDAPService;


/**
 * Autentica contra el LDAP
 * 
 * 
 * @author Juan Edi
 * @since May 26, 2012
 */
public class Security extends controllers.Secure.Security {

    @Inject private static LDAPService ldapService;
    
    static boolean authenticate(final String username, final String password) {
        return !StringUtils.isEmpty(password) && ldapService.authenticate(username, password);
    }
    
}
