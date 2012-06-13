/*
 * Copyright (c) 2012 Zauber S.A.  -- All rights reserved
 */
package models;

import java.util.List;

/**
 * Representa un usuario LDAP.
 * 
 * 
 * @author Juan Edi
 * @since Jun 12, 2012
 */
public class User {

    public String username;
    public String password;
    
    public String firstName;
    public String lastName;

    public List<String> groups;
}
