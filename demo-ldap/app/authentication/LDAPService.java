package authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.apache.commons.lang.Validate;
import org.springframework.ldap.core.AttributesMapper;
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

    private static AttributesMapper PASSWORD_GETTER = new PasswordAttributesMapper();
    private final LdapTemplate ldapTemplate;

    /** Creates the LDAPAuthenticator. */
    public LDAPService(LdapTemplate ldapTemplate) {
        Validate.notNull(ldapTemplate);
        this.ldapTemplate = ldapTemplate;
    }
    
    public boolean authenticate(final String username, final String password) {
        String real = getUserPassword(username);
        String pwHash = Crypto.passwordHash(password, HashType.MD5);
        return real != null && removePadding(real).equals(removePadding(pwHash));
    }
    
    /** Obtiene el hash del password del LDAP */
    private String getUserPassword(final String username) {
        Filter filter = new AndFilter()
        .and(new EqualsFilter("objectclass", "posixAccount"))
        .and(new EqualsFilter("uid", username));
        List<String> results = ldapTemplate.search("ou=users", filter.encode(), PASSWORD_GETTER);
        if (results.size() != 1) {
            return null;
        }
        return results.get(0);
    }
    
    private static String removePadding(final String hash) {
        if (hash.contains("=")) {
            return hash.substring(0, hash.indexOf("="));
        } else {
            return hash;
        }
    }
    
    private static class PasswordAttributesMapper implements AttributesMapper {

        /** @see AttributesMapper#mapFromAttributes(Attributes) */
        @Override
        public Object mapFromAttributes(Attributes attrs) throws NamingException {
            String pwAttr = new String(((byte[])attrs.get("userpassword").get()));
            
            // FIXME: esto le saca un "{MD5}" al principio de la pass
            // que devuelve el LDAP. Si las vamos a guardar en modo
            // "plain" y encriptadas a mano no hay que hacer esto.
            pwAttr = pwAttr.substring(5, pwAttr.length() - 1);
            return pwAttr;
        }
        
    }
    
}
