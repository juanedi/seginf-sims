/*
 * Copyright (c) 2012 Zauber S.A.  -- All rights reserved
 */
package controllers;

import models.User;

/**
 * Forma de autenticación.
 * Busca usuarios en la base de datos.
 * 
 * 
 * @author Juan Edi
 * @since May 19, 2012
 */
public class Security extends controllers.Secure.Security {

    static boolean authenticate(final String username, final String password) {
        User user = User.find("byUsername", username).first();
        return user != null && user.comparePassword(password);
    }
    
}
