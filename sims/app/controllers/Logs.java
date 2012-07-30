package controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import models.App;
import models.EventType;
import models.User;

/**
 * Controller para pantalla de logs.
 * 
 * 
 * @author Juan Edi
 * @since May 27, 2012
 */
public class Logs extends SecureController {

    /** Sirve pantalla principal de consulta de logs */
    public static void logs() {
    	List<EventType> eventTypes = Arrays.asList(EventType.values());
    	List<User> users = User.all().fetch();
    	List<App> apps = App.all().fetch();
        render(eventTypes, users, apps);
    }
    
    /** efectúa la búsqueda de los eventos de auditoría */
    public static void search(final EventType eventType, 
    						  final String user, 
    						  final List<String> relatedUsers, 
    						  final List<String> relatedApps,
    						  final Date since, Date to) {
    	System.out.println(eventType);
    	System.out.println(user);
    	System.out.println(relatedUsers);
    	System.out.println(relatedApps);
    	System.out.println(since);
    	System.out.println(to);
    }
}
