package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import services.EventSearchService;

import controllers.dto.EventSearchResult;

import models.App;
import models.Event;
import models.EventType;
import models.User;

/**
 * Controller para pantalla de logs.
 * 
 * 
 * @author Juan Edi
 * @since May 27, 2012
 */
public class Accounting extends SecureController {

	static final String DATE_FORMAT = "dd-MM-yyyy";
	@Inject static EventSearchService searchService;
	
    /** Sirve pantalla principal de consulta de logs */
    public static void search() {
    	List<EventType> eventTypes = Arrays.asList(EventType.values());
    	List<User> users = User.all().fetch();
    	List<App> apps = App.all().fetch();
        render(eventTypes, users, apps);
    }
    
    /** efectúa la búsqueda de los eventos de auditoría */
    public static void results(String eventType, 
    						   String user, 
    						   String relatedUsers, 
    						   String relatedApps,
    						   String since, String to) {

    	EventSearchResult ret = new EventSearchResult();

    	EventType t = null;
    	User u = null;
    	List<User> ru = null;
    	List<App> ra = null;
    	Date dateSince = null;
    	Date dateTo = null;
    	
    	if (param(eventType) != null) {
    		try {
    			t = EventType.valueOf(eventType);
    		} catch (IllegalArgumentException e) {
    			ret.errors.add("Tipo de evento inválido");
    		}
    	}
    	
    	if (param(user) != null) {
    		u = User.forUsername(user);
    		if (u == null) {
    			ret.errors.add(user + " no es un usuario válido.");
    		}
    	}
    	
    	if (param(relatedUsers) != null) {
    		ru = new LinkedList<User>();
    		for (String string : relatedUsers.split(",")) {
    			User relatedUser = User.forUsername(string);
    			if (relatedUser == null) {
    				ret.errors.add(user + " no es un usuario válido.");
    			} else {
    				ru.add(relatedUser);
    			}
    		}
    	}
    	
    	if (param(relatedApps) != null) {
    		ra = new LinkedList<App>();
    		for (String string : relatedApps.split(",")) {
    			App relatedApp = App.forName(string);
    			if (relatedApp == null) {
    				ret.errors.add(user + " no es una aplicación válida.");
    			} else {
    				ra.add(relatedApp);
    			}
    		}
    	}
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    	
    	if (param(since) != null) {
    		try {
				dateSince = dateFormat.parse(since);
    		} catch (ParseException e) {
    			ret.errors.add(since + " no es una fecha válida.");
    		}
    	}

    	if (param(to) != null) {
    		try {
    			dateTo = dateFormat.parse(to);
    		} catch (ParseException e) {
    			ret.errors.add(to + " no es una fecha válida.");
    		}
    	}
    	
    	List<Event> results = searchService.search(t, u, ra, ru, dateSince, dateTo);
    	for (Event event : results) {
			ret.addEvent(event);
		}
    	
    	renderJSON(ret);
    }
    
    /** Se usa esto porque se interpretan los null del JS como el string "null" */
    private static String param(final String p) {
    	if (p == null || "null".equals(p)) {
    		return null;
    	} else {
    		return p;
    	}
    }
    
}
