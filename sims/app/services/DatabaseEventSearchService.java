package services;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import play.db.jpa.JPA;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import models.App;
import models.Event;
import models.EventType;
import models.User;

/**
 * Busca eventos de audioría en la base de datos.
 * 
 * @author Juan Edi
 *
 */
public class DatabaseEventSearchService implements EventSearchService {

	@Override
	public List<Event> search(EventType type, User responsible, List<App> applications,
							  List<User> users, Date since, Date to) {
		EntityManager em = JPA.em();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Event> query = cb.createQuery(Event.class);
		Root<Event> e = query.from(Event.class);
		
		//seleccionamos los eventos
		query.select(e);

		List<Predicate> predicates = new LinkedList<Predicate>();
		
		if (type != null) {
			// que sean de determinado tipo
			predicates.add(cb.equal(e.get("type"), type));
		}
		
		if (responsible != null) {
			// que pertenezcan a determinado usuario
			predicates.add(cb.equal(e.get("responsible"), responsible));
		}
		
		if (applications != null) {
			// relacionados con ciertas aplicaciones
			Join<Event, App> eventApps = e.join("relatedApps");
			predicates.add(eventApps.in(applications));
		}
		
		
		if (users != null) {
			// relacionados con ciertos usuarios
			Join<Event, User> eventUsers = e.join("relatedUsers");
			predicates.add(eventUsers.in(users));
		}
		
		if (since != null) {
			// desde cierta fecha
			Expression<Date> ex = e.get("date");
			predicates.add(cb.greaterThanOrEqualTo(ex, since));
		}
		
		if (to != null) {
			// hasta cierta fecha
			Expression<Date> ex = e.get("date");
			predicates.add(cb.lessThanOrEqualTo(ex, to));
		}

		// seteo los predicados
		query.where(predicates.toArray(new Predicate[predicates.size()]));

		return em.createQuery(query).getResultList();
	}

}
