package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.apache.commons.lang.Validate;

import play.db.jpa.Model;

/**
 * Evento de auditoría.
 * 
 * @author Juan Edi
 */
public class Event extends Model {

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	public EventType type;
	
	@Column(name = "date", nullable = false)
	public Date date;
	
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
	public Event(final EventType type, final List<App> relatedApps, 
				 final List<User> relatedUsers, final String description) {
		Validate.notNull(type);
		Validate.notNull(relatedApps);
		Validate.notNull(relatedUsers);
		Validate.notEmpty(description);
		this.type = type;
		this.date = new Date();
		this.relatedApps = relatedApps;
		this.relatedUsers = relatedUsers;
		this.description = description;
	}
	
}
