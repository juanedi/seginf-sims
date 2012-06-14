import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import models.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;

import play.libs.Crypto;
import play.libs.Crypto.HashType;
import play.modules.spring.Spring;
import play.test.UnitTest;
import services.LDAPService;

/**
 * TODO Descripcion de la clase. Los comentarios van en castellano.
 * 
 * 
 * @author Juan Edi
 * @since Jun 3, 2012
 */
//@Ignore
public class LdapTest extends UnitTest {

    private LDAPService ldap;
    
    @Before
    public final void init() {
        ldap = Spring.getBeanOfType(LDAPService.class);
    }
    
    @Test
    public final void createUserTest() {
        String plainPass = "prueba";
//        
//        User user = new User();
//        user.username = "lnahabedian";
//        user.password = Crypto.passwordHash(plainPass, HashType.MD5);
//        user.firstName = "Leandro";
//        user.lastName = "Nahabedian";
//
//        user.groups.add("base");
//        user.groups.add("admin");
//        
//        ldap.createUser(user);
        
        assertTrue(ldap.authenticate("lnahabedian", plainPass));
        
        assertTrue(ldap.checkGroup("lnahabedian", "base"));
        assertTrue(ldap.checkGroup("lnahabedian", "admin"));
        assertFalse(ldap.checkGroup("lnahabedian", "audit"));
        assertFalse(ldap.checkGroup("lnahabedian", "lalala"));
    }
//
//    @Test
//    public final void authTest() {
//    }
    
}
