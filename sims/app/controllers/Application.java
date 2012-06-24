package controllers;

import play.*;
import play.data.validation.Required;
import play.mvc.*;
import play.mvc.results.Status;
import services.RMQService;

import java.util.*;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import controllers.dto.BaseAppConfig;

import models.*;

public class Application extends SecureController {

    @Inject static RMQService rmqService; 
    
    /** sirve página principal */
    public static void index() {
        List<App> appsToConfigure = App.toConfigureBy(connectedUser());
        render(appsToConfigure);
    }

    /** sirve pantalla de alta de aplicaciones */
    @Check(Role.SIMS_CREATE_APP_ROLE)
    public static void create() {
        List<User> users = User.all().fetch();
        List<Hash> hashTypes = Arrays.asList(Hash.values());
        render(users, hashTypes);
    }
    
    /** pantalla de detalle de aplicación */
    public static void detail(final String appName) {
        User user = connectedUser();
        App app = App.forName(appName);
        if (!app.owner.username.equals(user.username)) {
            response.status = 403;
            return;
        }
        render(app);
    }
    
    public static void configureApp(@Required final String appName,
                                    final BaseAppConfig baseConfig) {
        User user = connectedUser();
        App app = App.forName(appName);
        Set<String> roles = baseConfig.getRoles();
        Set<String> defaultRoles = baseConfig.getDefaultRoles();
        
        if (!app.owner.username.equals(user.username)) {
            response.status = 403;
            return;
        }
        
        if (!baseConfig.validate(flash)) {
            detail(appName);
        } else {
            try {
                rmqService.changeUserPassword(appName, baseConfig.rmqPass);
                for (String roleName : roles) {
                    Role role = new Role();
                    role.name = roleName;
                    role.app = app;
                    role.selectedByDefault = defaultRoles.contains(roleName);
                    app.roles.add(role);
                }
                app.configured = true;
                app.save();
                flash.success("Aplicación " + appName + " configurada correctamente");
                index();
            } catch (Exception e) {
                flash.error("Error grave al guardar configuración de la aplicación.");
                detail(appName);
            }
        }
    }
    
    @Check(Role.SIMS_CREATE_APP_ROLE)
    public static void postApp(@Required final String name,
                               @Required final String ownerName,
                               @Required final Hash hash) {
        boolean hasErrors = false;
        if (validation.hasErrors()) {
            Set<String> missingFields = validation.errorsMap().keySet();
            flash.error("Falta completar los campos: %s", missingFields);
            hasErrors = true;
        }
        
        if (hash == null) {
            flash.error("Debe elegir un algoritmo de hash para almacenar las claves");
            hasErrors = true;
        }
        if (App.count("name = ?", name) > 0) {
            flash.error("Ya existe una aplicación con ese nombre");
            hasErrors = true;
        }

        if(!App.NAME_PATTERN.matcher(name).matches()) {
            flash.error("\"%s\" no es un nombre de aplicación inválido", name);
            hasErrors = true;
        }
        
        if (StringUtils.isEmpty(ownerName)) {
            flash.error("No se especificó usuario a cargo.");
            hasErrors = true;            
        }
        
        User owner = User.forUsername(ownerName);
        
        try {
            rmqService.setupApplication(name);
        } catch (Exception e) {
            flash.error("Error desconocido");
            hasErrors = true;
        }
        
        // si hay errores vuelvo a mostrar el formulario.
        if (hasErrors) {
            create();
            return;
        }
        
        App app = new App();
        app.owner = owner;
        app.name = name;
        app.hashType = hash;
        app.roles = new LinkedList<Role>();
        app.save();
        
        flash.success(String.format("Aplicación %s creada exitosamente", name));
        index();
    }
    
}