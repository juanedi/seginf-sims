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
    
    public static void postPasswordPolicy(@Required final String name,
           @Required final int passwordLength,
           @Required final boolean useLowerCaseLetters,
           @Required final boolean useUpperCaseLetters,
           @Required final boolean useSpecialCharsLetters,
           @Required final boolean useNumbers,
           @Required final boolean reqEveryType,
           @Required final int duration) {

        boolean hasErrors = false;

        if(validation.hasErrors()) {
            Set<String> missingFields = validation.errorsMap().keySet();
            flash.error("Falta completar los campos: %s", missingFields);
            hasErrors = true;
        }
        
        PasswordPolicy passPolicy = new PasswordPolicy(name,
        		                           passwordLength,
        		                           useLowerCaseLetters,
        		                           useUpperCaseLetters,
        		                           useSpecialCharsLetters,
        		                           useNumbers,
        		                           reqEveryType,
        		                           duration);
        
        passPolicy.save();
        
        if (hasErrors) {
            create();
        } else {
            flash.success("Política de claves creada exitosamente.");
            PasswordPolicies.list();    
        }
    }

}
