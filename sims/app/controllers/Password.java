package controllers;

import java.util.List;

import javax.inject.Inject;

import controllers.dto.AppUserConfiguration.UserConfiguration;

import play.mvc.Controller;
import play.mvc.With;

import models.App;
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

    public static void changePassword(@Required final String password,
            @Required final String newPassword,
            @Required final String newPasswordConfirmation) {

        User user = connectedUser();
        
        if (validation.hasErrors()) {
            flash.error("Todos los campos son obligatorios");
            info();
        }

        if (!newPassword.equals(newPasswordConfirmation)) {
            flash.error("Las passwords no coinciden.");
            info();
        }
        if (newPassword.equals(password)) {
            flash.error("La nueva password es igual a la anterior.");
            info();
        }
        if (!user.comparePassword(password)) {
            flash.error("La password ingresada no es correcta.");
            info();
        }

        user.setPassword(newPasswordConfirmation);
        user.save();
        accountingLogger.logPasswordChange(user);
        
        for (App app : user.apps) {
            //horrible
            if (!app.name.equals("sims"))
                appNotificationService.notifyPasswordChanged(user, app);        
        }
        
        flash.success("Se ha actualizado su clave");
        Application.index();
    }

}
