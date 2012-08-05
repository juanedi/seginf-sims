package models;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.Validate;

import play.db.jpa.JPA;
import play.db.jpa.Model;
import play.libs.Crypto;
import play.libs.Crypto.HashType;

/**
 * Usuario del administrador de identidades.
 * 
 * 
 * @author Juan Edi
 * @since May 22, 2012
 */
@Entity
@Table( name = "users", 
        uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User extends Model {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(".+@.+\\.[a-z]+");;

    @Column(name = "username", nullable = false)
    public String username;
    
    @Column(name = "email", nullable = false)
    public String email;
    
    @Column(name = "first_name", nullable = false)
    public String firstName;

    @Column(name = "last_name", nullable = false)
    public String lastName;
    
    @Column(name = "password_md5", nullable = false)
    public String passwordMD5;

    @Column(name = "password_sha_1", nullable = false)
    public String passwordSHA1;
    
    @Column(name = "password_sha_256", nullable = false)
    public String passwordSHA256;
    
    @Column(name = "password_sha_512", nullable = false)
    public String passwordSHA512;

    @Column(name = "last_password_changed", nullable = false)
    public Date lastPasswordChanged;
    
    @ManyToMany
    @JoinTable( name = "user_apps", 
                joinColumns = @JoinColumn(name = "user_id"), 
                inverseJoinColumns = @JoinColumn(name = "app_id"))
    public List<App> apps;
    
    @ManyToMany
    @JoinTable( name = "user_roles", 
                joinColumns = @JoinColumn(name = "user_id"), 
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    public List<Role> roles;
    
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("dateModified DESC")
    public List<Password> oldPasswords;
    
    
    /** Creates the User. */
    public User() {
        this.apps = new LinkedList<App>();
        this.roles = new LinkedList<Role>();
        this.oldPasswords = new LinkedList<Password>();
    }
    
    /** setea el username */
    public void setUsername(final String username) {
        Validate.notEmpty(username);
        this.username = username;
    }
    
    /** setea el password */
    public void setPassword(final String password) {
        Validate.notEmpty(password);
        this.passwordMD5 = hashPassword(password, HashType.MD5);
        this.passwordSHA1 = hashPassword(password, HashType.SHA1);
        this.passwordSHA256 = hashPassword(password, HashType.SHA256);
        this.passwordSHA512 = hashPassword(password, HashType.SHA512);
        this.setlastPasswordChanged();
    }
    
    /** setea el email */
    public void setEmail(final String email) {
        validateEmail(email);
        this.email = email;
    }
    /** setea la fecha del último cambio de clave */

    private void setlastPasswordChanged() {
        this.lastPasswordChanged = new Date();
    }

    public Date getlastPasswordChanged() {
        return this.lastPasswordChanged;
    }
    
    /** devuelve el password para determinado tipo de hash */
    public String getHashedPassword(Hash hashType) {
        // razonablemente feo :)
        if (Hash.MD5.equals(hashType)) {
            return passwordMD5;
        }
        if (Hash.SHA1.equals(hashType)) {
            return passwordSHA1;
        }
        if (Hash.SHA256.equals(hashType)) {
            return passwordSHA256;
        }
        if (Hash.SHA512.equals(hashType)) {
            return passwordSHA512;
        }
        return null;
    }
    

    /** compara la password */
    public boolean comparePassword(final String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return this.passwordSHA512.equals(hashPassword(password, HashType.SHA512));
        
    }

    public boolean hasRole(final String appName, final String roleName) {
        long count = User.count("from User u join u.roles r" 
                             + " where u = ?1 and r.app.name = ?2 and r.name = ?3",
                             this, appName, roleName);
        return count > 0;
    }
    
    /** devuelve los roles que el usuario posee para cierta aplicación */
    public List<Role> getRoles(final String appName) {
        return User.find("select r from User u join u.roles r where u = ?1 and r.app.name = ?2", 
                this, appName)
           .fetch();
    }
    
    /** devuelve los roles que el usuario posee para cierta aplicación */
    public List<Role> getRoles(final App application) {
        return getRoles(application.name);
    }

    public static User forUsername(final String username) {
        return User.find("byUsername", username).first();
    }
    
    /*-------- ÚTIL --------*/

    /** hash de las passwords */
    private static String hashPassword(final String password, final HashType hashType) {
        return Crypto.passwordHash(password, hashType);
    }

    /** valida que se haya pasado un email no nulo y del formato correcto */
    private static void validateEmail(final String email) {
        if(email == null || !EMAIL_PATTERN.matcher(email).matches()) {
             throw new IllegalArgumentException("email inválido");
        }
    }

}
