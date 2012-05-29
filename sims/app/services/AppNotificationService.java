package services;

import models.App;
import models.User;

/**
 * Servicio de notificaciones a las distintas apps.
 * 
 * 
 * @author Juan Edi
 * @since May 29, 2012
 */
public interface AppNotificationService {

    void notifyNewUser(User user, App app);
    
    void notifyPasswordChanged(User user, App app);
    
    void notifyRolesChanged(User user, App app);
    
    void notifyUserRemove(User user);
}
