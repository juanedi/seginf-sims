package controllers.dto;

import java.util.List;

/**
 * Contiene la configuración de usuarios de la aplicaicón
 * 
 * 
 * @author Juan Edi
 * @since Jun 23, 2012
 */
public class AppUserConfiguration {

    public List<UserConfiguration> userConfigurations;
 
    public class UserConfiguration {
        
        public String username;
        public boolean enabled;
        public List<String> roles;
        
    }
    
}