package controllers;

import play.*;
import play.data.validation.Required;
import play.mvc.*;
import services.RMQService;

import java.util.*;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import models.*;

public class Application extends SecureController {

    @Inject static RMQService rmqService; 
    
    /** sirve página principal */
    public static void index() {
        render();
    }

    /** sirve pantalla de alta de aplicaciones */
    public static void create() {
        List<Hash> hashTypes = Arrays.asList(Hash.values());
        render(hashTypes);
    }
    
    @Check(Role.SIMS_CREATE_APP_ROLE)
    public static void postApp(@Required final String name,
                               @Required final Hash hash,
                               @Required final String roleList,
                               final String defaultRoleList) {
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
        
        Set<String> roles = roleSet(roleList);
        Set<String> defaultRoles = roleSet(defaultRoleList);
        if(roles.isEmpty()) {
            flash.error("No se definieron roles válidos para la aplicación", name);
            hasErrors = true;
        }
        if(!roles.containsAll(defaultRoles)) {
            flash.error("Todos los roles marcados por defecto deben pertenecer a la primera lista");
            hasErrors = true;
        }
        
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
        app.name = name;
        app.hashType = hash;
        app.roles = new LinkedList<Role>();
        for (String roleName : roles) {
            Role role = new Role();
            role.name = roleName;
            role.app = app;
            role.selectedByDefault = defaultRoles.contains(roleName);
            app.roles.add(role);
        }
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