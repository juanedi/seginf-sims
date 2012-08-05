package services;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import models.User;

import org.apache.commons.lang.Validate;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;

/**
 * Operaciones contra el LDAP
 * 
 * 
 * @author Juan Edi
 * @since Jun 2, 2012
 */
public class LDAPService {

    private final LdapTemplate ldapTemplate;

    /** Creates the LDAPAuthenticator. */
    public LDAPService(LdapTemplate ldapTemplate) {
        Validate.notNull(ldapTemplate);
        this.ldapTemplate = ldapTemplate;
    }
    
    /** crea un usuario nuevo */
    public void createUser(final User user) {
        Name name = userDN(user);
        ldapTemplate.bind(name, null, attributes(user));
        
        for (String group : user.groups) {
            addToGroup(user, group);
        }
    }

    public void updateRoles(final User user) {
        List<String> userGroups = user.groups;
        List<String> allGroups = allGroups();
        for (String g : allGroups) {
            if (userGroups.contains(g)) {
                addToGroup(user, g);
            } else {
                removeFromGroup(user, g);
            }
        }
    }
    
    
    /** autentica el usuario */
    public boolean authenticate(final String username, final String password) {
        Filter filter = new AndFilter()
                            .and(new EqualsFilter("objectclass", "person"))
                            .and(new EqualsFilter("uid", username));
        
        return ldapTemplate.authenticate("ou=users", filter.encode(), password);
    }
    
    /** cambia la clave de un usuario */
    public void changePassword(final User user) {
        Name userDN = userDN(user);
        DirContextOperations ctx = ldapTemplate.lookupContext(userDN);
        ctx.setAttributeValue("userpassword", "{MD5}" + user.password); // la pass ya viene hasheada al momento de crear.
        ldapTemplate.modifyAttributes(ctx);
    }
    
    /** Listado de grupos del usuario. */
    public List<String> listGroups(final String username) {
        Validate.notEmpty(username);
        List<String> allGroups = allGroups();
        List<String> userGroups = new LinkedList<String>();
        for (String group : allGroups) {
            if (checkGroup(username, group)) {
                userGroups.add(group);
            }
        }
        return userGroups;
    }
    
    /** chequea si el usuario pertenece a un grupo */
    public boolean checkGroup(final String username, final String group) {
        Name groupDN = groupDN(group);
        try {
            DirContextOperations ctx = ldapTemplate.lookupContext(groupDN);
            SortedSet groupUsers = ctx.getAttributeSortedStringSet("memberuid");
            return groupUsers != null && groupUsers.contains(username);
        } catch (NameNotFoundException e) {
            // el grupo no existe :(
            return false;
        }
    }
    
    private List<String> allGroups() {
        DistinguishedName groupsDN = new DistinguishedName();
        groupsDN.add("ou", "groups");
        return ldapTemplate.search(groupsDN, new EqualsFilter("objectclass", "posixGroup").encode(), new AttributesMapper() {
            
            @Override
            public String mapFromAttributes(Attributes arg0) throws NamingException {
                return String.valueOf(arg0.get("cn").get());
            }
        });
    }
    
    /** agrega un usuario a un grupo. requiere que el grupo ya exista. */
    private void addToGroup(final User user, String group) {
        Name groupDN = groupDN(group);
        DirContextOperations ctx = ldapTemplate.lookupContext(groupDN);
        ctx.addAttributeValue("memberuid", user.username);
        ldapTemplate.modifyAttributes(ctx);
    }
    
    /** remueve un usuario de un grupo existente */
    private void removeFromGroup(final User user, final String group) {
        Name groupDN = groupDN(group);
        DirContextOperations ctx = ldapTemplate.lookupContext(groupDN);
        ctx.removeAttributeValue("memberuid", user.username);
        ldapTemplate.modifyAttributes(ctx);        
    }
    
    /** construye el nombre calificado del usuario */
    private static Name userDN(final User user) {
        DistinguishedName dn = new DistinguishedName();
        dn.add("ou", "users");
        dn.add("uid", user.username);
        return dn;
    }
    
    /** nombre distinguido del grupo */
    private static Name groupDN(final String group) {
        DistinguishedName groupDN = new DistinguishedName();
        groupDN.add("ou", "groups");
        groupDN.add("cn", group);
        return groupDN;
    }
    
    /** construye los atributos de un usuario nuevo */
    private static Attributes attributes(final User user) {
        BasicAttributes attrs = new BasicAttributes();
        
        BasicAttribute objectClass = new BasicAttribute("objectclass");
        objectClass.add("person");
        objectClass.add("inetOrgPerson");
        attrs.put(objectClass);
        attrs.put("cn", user.firstName + " " + user.lastName);
        attrs.put("sn", user.lastName);
        attrs.put("userpassword", "{MD5}" + user.password); //la pass ya viene hasheada al momento de crear.
        return attrs;
    }
    
}
