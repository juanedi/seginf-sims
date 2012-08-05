package controllers;

import java.util.Date;

import javax.inject.Inject;

import models.App;
import models.PasswordPolicy;
import models.User;
import play.data.validation.Required;
import play.mvc.Controller;
import services.AccountingLogger;
import services.AppNotificationService;

/**
 * Controller de pantalla de cambio de passwords.
 * 
 * 
 * @author Juan Edi
 * @since May 27, 2012
 */
public class Password extends SecureController {
    
	@Inject static AccountingLogger accountingLogger;
    @Inject static AppNotificationService appNotificationService;

    /** sirve pantalla de cambio de password */
    public static void info() {
        render();
    }

    public static void changePassword(@Required final String oldPassword,
            @Required final String newPassword,
            @Required final String newPasswordConfirmation) {

    	try {
            User user = connectedUser();
            
            if (validation.hasErrors()) {
            	throw new IllegalArgumentException("Todos los campos son obligatorios");
            }

            if (!newPassword.equals(newPasswordConfirmation)) {
            	throw new IllegalArgumentException("Las passwords no coinciden.");
            }
            
            if (newPassword.equals(oldPassword)) {
            	throw new IllegalArgumentException("La nueva password es igual a la anterior.");
            }
            if (!user.comparePassword(oldPassword)) {
            	throw new IllegalArgumentException("La password ingresada no es correcta.");
            }
            
            PasswordPolicy currentPolicy = PasswordPolicy.current();
            if (currentPolicy != null) {
            	boolean compliesToPolicy = currentPolicy.validate(user, newPassword);
            	if (!compliesToPolicy) {
            		throw new IllegalArgumentException("La clave no cumple con la política.");
            	}
            }
            
            user.setPassword(newPasswordConfirmation);
            user.oldPasswords.add(new models.Password(user, oldPassword, new Date()));
            user.save();
            
            accountingLogger.logPasswordChange(user);
            appNotificationService.broadcastPasswordChanged(user);        
            
            flash.success("Se ha actualizado su clave");
            Application.index();    		
            
    	} catch (IllegalArgumentException e) {
    		flash.error(e.getMessage());
    		info();
    	} catch (Exception e) {
    		flash.error("Error desconocido.");
    		info();    		
    	}
    	
    }

}
