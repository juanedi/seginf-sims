package controllers;

import play.mvc.Controller;
import play.mvc.With;

import models.User;
import play.data.validation.Required;
import play.mvc.Controller;

/**
 * Controller de pantalla de cambio de passwords.
 * 
 * 
 * @author Juan Edi
 * @since May 27, 2012
 */
public class Password extends SecureController {

    /** sirve pantalla de cambio de password */
    public static void info() {
        render();
    }
    
    
    public static void changePassword(	@Required final String password, 
            							@Required final String newPassword, 
            							@Required final String newPasswordConfirmation) {
    		
    	
    							User user = connectedUser();

    							if (validation.hasErrors()) {
    								flash.error("Todos los campos son obligatorios");
    								info();
    							}
    							
    							if (!newPassword.equals(newPasswordConfirmation)) {
    								flash.error("Los passwords no coinciden.");
    								info();            
    							}
    							if (!user.comparePassword(password)) {
    								flash.error("La password ingresada no es correcta.");
    								info();            
    							}
    							
    							user.setPassword(newPasswordConfirmation);
    							user.save();

    							flash.success("Se ha actualizado su clave");
    							Application.index();
    }

}
