package services;

import java.util.Date;
import java.util.List;

import models.App;
import models.Event;
import models.EventType;
import models.User;

/**
 * Servicio de consulta de eventos de auditoría.
 * 
 * @author Juan Edi.
 *
 */
public interface EventSearchService {

	/**
	 * Búsqueda de eventos.
	 * 
	 * @param type			Tipo de evento. Null si se quiere cualquier tipo.
	 * @param responsible	Usuario que originó el vento. Null si no se quiere filtrar.
	 * @param applications	Aplicaciones relacionadas. Null si no se quiere filtrar por aplicación.
	 * @param users			Usuarios relacionados. Null si no se quiere filtrar por usuarios.
	 * @param since			Fecha a partir de buscar. Null si no se desea limita.
	 * @param to			Fecha hasta la cual buscar. Null si no se desea limitar.
	 * 
	 * @return	Lista de eventos encontrados.
	 */
	List<Event> search(EventType type, User responsible, List<App> applications, 
					   List<User> users, Date since, Date to);
	
}
