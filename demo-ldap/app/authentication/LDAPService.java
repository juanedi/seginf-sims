package authentication;

import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import models.User;

import org.apache.commons.lang.Validate;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;

import play.libs.Crypto;
import play.libs.Crypto.HashType;

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
        Name name = dName(user);
        ldapTemplate.bind(name, null, attributes(user));
        
        //TODO: agregarlo a grupos.
    }
    
    /** autentica el usuario */
    public boolean authenticate(final String username, final String password) {
        Filter filter = new AndFilter()
                            .and(new EqualsFilter("objectclass", "person"))
                            .and(new EqualsFilter("uid", username));
        
        return ldapTemplate.authenticate("ou=users", filter.encode(), password);
    }
    
    /** construye el nombre calificado del usuario */
    private static Name dName(final User user) {
        DistinguishedName dn = new DistinguishedName();
        dn.add("ou", "users");
        dn.add("uid", user.username);
        return dn;
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
