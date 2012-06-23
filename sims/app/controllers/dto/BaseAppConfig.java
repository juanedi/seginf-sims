package controllers.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import models.Role;

import org.apache.commons.lang.StringUtils;

import play.data.binding.As;
import play.data.validation.Required;
import play.mvc.Scope.Flash;

/**
 * Configuración inical de la aplicación.
 * 
 * 
 * @author Juan Edi
 * @since Jun 23, 2012
 */
public class BaseAppConfig {

    @Required public String rmqPass;
    @Required public String rmqPassConfirm;
    @Required public String roleList;
    @Required public String defaultRoleList;
    
    public boolean validate(Flash flash) {
        if (rmqPass == null || rmqPass.isEmpty()) {
            flash.error("Se deben completar la clave de RMQ");
            return false;
        }
        if (!rmqPass.equals(rmqPassConfirm)) {
            flash.error("Las claves de RMQ no coinciden");
            return false;            
        }
        
        Set<String> roles = getRoles();
        Set<String> defaultRoles = getDefaultRoles();
        
        if(roles.isEmpty()) {
            flash.error("No se definieron roles válidos para la aplicación");
            return false;
        }
        if(!roles.containsAll(defaultRoles)) {
            flash.error("Todos los roles marcados por defecto deben pertenecer a la primera lista");
            return false;
        }
        
        for (String role : roles) {
            if (!Role.NAME_PATTERN.matcher(role).matches()) {
                flash.error("%s no es un nombre de rol válido", role);
                return false;
            }
        }
        
        return true;
    }
    
    public Set<String> getRoles() {
        return roleSet(roleList);
    }

    public Set<String> getDefaultRoles() {
        return roleSet(defaultRoleList);
    }
    
    private static Set<String> roleSet(String rolesList) {
        if (rolesList == null) return Collections.<String>emptySet();
        String[] splitted = StringUtils.split(rolesList, "\n");
        Set<String> roles = new HashSet<String>();
        for(int i = 0; i < splitted.length; i++) {
            roles.add(splitted[i].trim());
        }
        return roles;
    }
}
