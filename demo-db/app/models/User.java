/*
 * Copyright (c) 2012 Zauber S.A.  -- All rights reserved
 */
package models;

import java.util.List;

import javax.persistence.Column;
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

    @Column (name = "username", nullable = false)
    public String username;
    
    @Column (name = "password", nullable = false)
    public String password;
    
    @Column(name = "is_admin", nullable = false)
    public Boolean isAdmin;
    
    @Column(name = "pass_expired", nullable = false)
    public Boolean passwordExpired;
    
    /** Creates the User. */
    public User(final String username, final String password, final Boolean isAdmin) {
        Validate.notEmpty(username);
        Validate.notNull(password);
        Validate.notNull(isAdmin);
        this.username = username;
        this.isAdmin = isAdmin;
        setPassword(password);
    }
    
    /** setea la password que viene hasheada de la administración central*/
    public void setPassword(final String password) {
        Validate.notNull(password);
        this.password = password;
        this.passwordExpired = false;
    }
    
    /** compara la password */
    public boolean comparePassword(final String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return this.password.equals(hashPassword(password));
        
    }

    public void passwordExpired() {
    	this.passwordExpired = true;
    }
    
    /** hash de las passwords */
    private static String hashPassword(final String password) {
        return Crypto.passwordHash(password, HashType.SHA256);
    }
 
}
