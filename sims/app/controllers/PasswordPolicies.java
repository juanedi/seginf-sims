package controllers;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import notifiers.Mailer;

import org.apache.commons.lang.RandomStringUtils;

import play.data.validation.Required;

import services.AppNotificationService;
import models.App;
import models.PasswordPolicy;
import models.Role;
import models.User;

/**
 * Controller para creación de políticas de claves.
 * 
 * 
 * @author Ruben Festini
 * @since Jul 19, 2012
 */

public class PasswordPolicies extends SecureController {
	
    @Inject static AppNotificationService appNotificationService;

    /** sirve pantalla de cambio de password */
    public static void list() {
        List<PasswordPolicy> passPolicy = PasswordPolicy.all().fetch();
        render(passPolicy);
    }
    
    public static void create() {
        render();
    }    
    
    @Check(Role.SIMS_PASSWORD_POLICY_ROLE)
    public static void postPasswordPolicy(@Required final String name,
           @Required final Integer passwordLength,
           @Required final boolean useLowerCaseLetters,
           @Required final boolean useUpperCaseLetters,
           @Required final boolean useSpecialCharsLetters,
           @Required final boolean useNumbers,
           @Required final Integer duration) {

        try {
        	
        	if(validation.hasErrors()) {
        		Set<String> missingFields = validation.errorsMap().keySet();
        		throw new IllegalArgumentException(String.format("Falta completar los campos: %s", missingFields));
        	}
        	
        	if (PasswordPolicy.find("byName", name).first() != null) {
        		throw new IllegalArgumentException("Ya existe una política con ese nombre");
        	}
        	
        	if (passwordLength <= 0) {
        		throw new IllegalArgumentException("La longitud de clave debe ser positiva");
        	}
        	
        	if (duration <= 0) {
        		throw new IllegalArgumentException("La duración de clave debe ser positiva");
        	}
        	
        	PasswordPolicy passPolicy = new PasswordPolicy(name,
        			passwordLength,
        			useLowerCaseLetters,
        			useUpperCaseLetters,
        			useSpecialCharsLetters,
        			useNumbers,
        			duration);
        	
        	passPolicy.save();
        	
            flash.success("Política de claves creada exitosamente.");
            PasswordPolicies.list();
        	
        } catch (IllegalArgumentException e) {
        	flash.error(e.getMessage());
        	create();
        }
        
    }

}
