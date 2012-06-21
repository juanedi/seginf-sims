package controllers;

import play.*;
import play.data.validation.Required;
import play.mvc.*;
import play.mvc.results.Status;
import services.RMQService;

import java.util.*;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

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
                                    @Required final String rmqPass,
                                    @Required final String rmqPassConfirm,
                                    @Required final String roleList,
                                    final String defaultRoleList) {
        User user = connectedUser();
        App app = App.forName(appName);
        Set<String> roles = roleSet(roleList);
        Set<String> defaultRoles = roleSet(defaultRoleList);
        
        if (!app.owner.username.equals(user.username)) {
            response.status = 403;
            return;
        }
        
        boolean fail = false;
        
        if(roles.isEmpty()) {
            flash.error("No se definieron roles válidos para la aplicación", appName);
            fail = true;
        }
        if(!roles.containsAll(defaultRoles)) {
            flash.error("Todos los roles marcados por defecto deben pertenecer a la primera lista");
            fail = true;
        }
        
        if (!rmqPass.equals(rmqPassConfirm)) {
            flash.error("Las claves no concuerdan");
            fail = true;            
        }
        
        if (validation.hasErrors()) {
            Set<String> missingFields = validation.errorsMap().keySet();
            flash.error("Falta completar los campos: %s", missingFields);
            fail = true;
        }
        
        
        if (fail) {
            detail(appName);
        } else {
            try {
                rmqService.changeUserPassword(appName, rmqPass);
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
    
    private static Set<String> roleSet(String rolesList) {
        String[] splitted = StringUtils.split(rolesList, "\n");
        Set<String> roles = new HashSet<String>();
        for(int i = 0; i < splitted.length; i++) {
            roles.add(splitted[i].trim());
        }
        return roles;
    }
}