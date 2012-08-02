package controllers;

import java.util.Date;
import java.util.Set;

import models.Role;
import models.User;
import models.Password;
import notifiers.Mailer;

import org.apache.commons.lang.RandomStringUtils;

import play.data.validation.Required;

/**
 * Controller para alta de usuarios.
 * 
 * 
 * @author Juan Edi
 * @since May 27, 2012
 */
@Check(Role.SIMS_CREATE_USER_ROLE)
public class Users extends SecureController {

    public static void create() {
        render();
    }
    
    public static void postUser(@Required final String username,
                                @Required final String firstName,
                                @Required final String lastName,
                                @Required final String email) {
        boolean hasErrors = false;

        if(validation.hasErrors()) {
            Set<String> missingFields = validation.errorsMap().keySet();
            flash.error("Falta completar los campos: %s", missingFields);
            hasErrors = true;
        }
        
        if (User.forUsername(username) != null) {
            flash.error("El usuario %s ya existe.", username);
            hasErrors = true;
        }
        
        User user = new User();
        user.username = username;
        user.firstName = firstName;
        user.lastName = lastName;
        user.email = email;
        
        String randomPassword = RandomStringUtils.randomAlphanumeric(10);
        user.setPassword(randomPassword);
        user.passwords.add(new Password(user,randomPassword,new Date()));
        
        
        Mailer.welcome(user, randomPassword);
        
        user.save();
        
        if (hasErrors) {
            create();
        } else {
            flash.success("Usuario creado exitosamente.");
            Application.index();    
        }
    }
}
