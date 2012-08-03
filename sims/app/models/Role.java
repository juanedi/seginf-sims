package models;

import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

/**
 * Rol definido para una aplicación.
 * 
 * 
 * @author Juan Edi
 * @since May 23, 2012
 */
@Entity
@Table(name = "roles")
public class Role extends Model {

    public static final Pattern NAME_PATTERN = Pattern.compile("[\\w]+");
    
    /** Rol para quienes pueden crear aplicaciones */
    public static final String SIMS_CREATE_APP_ROLE = "CREATE_APP";
    
    /** Rol para quienes pueden crear usuarios */
    public static final String SIMS_CREATE_USER_ROLE = "CREATE_USER";

    /** Rol del perfil de auditoría del administrador de identidades */
    public static final String SIMS_PASSWORD_POLICY_ROLE = "PASSWORD_POLICY";
    
    /** Rol del perfil de auditoría del administrador de identidades */
    public static final String SIMS_AUDIT_ROLE = "AUDIT";
    
    /** 
     * Rol que se le asigna a un usuario sobre una aplicación para 
     * que pueda modificar los roles de los otros usuarios de la misma 
     */
    public static final String APP_ADMIN_ROLE = "SIMS_APP_MANAGER";
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "app_id")
    public App app;
    
    @Column(name = "name", nullable = false)
    public String name;

    @Column(name = "selected_by_default", nullable = false)
    public Boolean selectedByDefault = false;
    
    public static List<Role> defaultForApp(App app) {
        return Role.find("app = ?1 and selectedByDefault = true", app).fetch();
    }
    
    public static Role find(final App app, final String name) {
        return Role.find("app = ?1 and name = ?2", app, name).first();
    }
}
