package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    /** Nombre de la aplicación del administrador de identidades */
    public static final String SIMS_APP_NAME = "SIMS";
    
    @Column(name = "name", nullable = false)
    public String name;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "app", cascade = CascadeType.ALL)
    public List<Role> roles;
}
