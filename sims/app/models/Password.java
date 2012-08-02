package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;
import play.libs.Crypto;
import play.libs.Crypto.HashType;

/**
 * Lista de Password utilizadas por usuario.
 * 
 * 
 * @author Ruben Festini
 * @since Jul 30, 2012
 */
@Entity
@Table( name = "passwords")
public class Password extends Model {
    
    @ManyToOne()
    @JoinColumn(name="user_id", nullable = false)
    public User user;
    
    @Column(name = "password", nullable = false)
    public String password;
    
    @Column(name = "date_modified", nullable = false)
    public Date dateModified;

    public Password(User user,
     	    String password,
            Date dateModified) {

         this.user = user;
         this.password = Crypto.passwordHash(password, HashType.SHA512);
         this.dateModified = dateModified;
     }

    
}
