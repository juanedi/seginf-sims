/*
 * Copyright (c) 2012 Zauber S.A.  -- All rights reserved
 */
package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.Validate;

import play.db.jpa.Model;
import play.libs.Crypto;
import play.libs.Crypto.HashType;

/**
 * Usuario a autenticarse contra la base de datos.
 * 
 * 
 * @author Juan Edi
 * @since May 19, 2012
 */
@Entity
@Table(name = "users")
public class User extends Model {

    public String username;
    public String password;
    public boolean isAdmin;
    
    /** Creates the User. */
    public User(final String username, final String password, final boolean isAdmin) {
        // TODO: Validar formatos.
        Validate.notEmpty(username);
        Validate.notEmpty(password);
        this.username = username;
        this.password = Crypto.passwordHash(password, HashType.SHA256);
        this.isAdmin = isAdmin;
    }
    
}
