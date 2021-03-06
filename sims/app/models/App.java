package models;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.db.jpa.Model;

/**
 * Aplicación que utiliza el administrador de identidades.
 * Tiene su propio esquema de roles definido.
 * 
 * El administrador de indentidades mismo se considera una aplicación,
 * ya que comparte su base de usuarios es la misma que el resto de las aplicaciones. 
 * 
 * @author Juan Edi
 * @since May 23, 2012
 */
@Entity
@Table( name = "apps", 
        uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class App extends Model {

    public static final Pattern NAME_PATTERN = Pattern.compile("[\\w]+");

    /** Nombre de la aplicación del administrador de identidades */
    public static final String SIMS_APP_NAME = "SIMS";
    
    @Column(name = "name", nullable = false)
    public String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "hash_type", nullable = false)
    public Hash hashType;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "owner", nullable = false)
    public User owner;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "app", cascade = CascadeType.ALL)
    public List<Role> roles = new LinkedList<Role>();

    @Column(name = "configured", nullable = false)
    public Boolean configured = false;
    
    public static App forName(final String appName) {
        return App.find("byName", appName).first();
    }
    
    /** aplicaciones todavía no configuradas a cargo de un usuario */
    public static List<App> toConfigureBy(final User user) {
        return App.find("configured = 0 and owner = ?1 and name != ?2", user, SIMS_APP_NAME).fetch();
    }

    /** el administador de identidades */
    public static App sims() {
    	return App.forName(SIMS_APP_NAME);
    }
}
