package models;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.Validate;

import play.db.jpa.Model;

/**
 * Evento de auditoría.
 * 
 * @author Juan Edi
 */
@Entity
@Table(name = "accouting_events")
public class Event extends Model {
	
	@Column(name = "date", nullable = false)
	public Date date;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	public EventType type;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
	public User responsible;
	
    @ManyToMany
    @JoinTable( name = "events_apps", 
                joinColumns = @JoinColumn(name = "event_id"), 
                inverseJoinColumns = @JoinColumn(name = "app_id"))
	public List<App> relatedApps;

    @ManyToMany
    @JoinTable( name = "users_apps", 
                joinColumns = @JoinColumn(name = "user_id"), 
                inverseJoinColumns = @JoinColumn(name = "app_id"))
	public List<User> relatedUsers;
	
    @Column(name = "description", nullable = false)
    public String description;

    /** constructor default */
    public Event() {
    	// Hibernate
    }
    
    /** construye un evento nuevo */
	public Event(final Date date, final EventType type, final User responsible, 
				 final List<App> relatedApps, final List<User> relatedUsers, final String description) {
		Validate.notNull(date);
		Validate.notNull(type);
		Validate.notNull(responsible);
		Validate.notNull(relatedApps);
		Validate.notNull(relatedUsers);
		Validate.notEmpty(description);
		this.date = date;
		this.type = type;
		this.responsible = responsible;
		this.relatedApps = new LinkedList<App>(relatedApps);
		
		this.relatedUsers = new LinkedList<User>();
		if (!relatedUsers.contains(responsible)) {
			this.relatedUsers.add(responsible);
		}
		this.relatedUsers.addAll(relatedUsers);
		
		this.description = description;
	}
	
}
