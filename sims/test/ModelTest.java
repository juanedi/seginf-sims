import org.junit.*;
import java.util.*;

import play.test.*;
import models.*;

public class ModelTest extends UnitTest {

    /** Un email válido pasa la validación */
    @Test
    public void testValidEmail() {
        new User().email = "juan@server.com";
    }
    
    /** Un email inválido no pasa la validación */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmail() {
        new User().email = "lala";
    }
    
    /** Prueba la consulta de roles por aplicación de un usuario */
    @Test
    public void retrieveGrantedRolesTest() {
        User user = new User();
        user.username = "juan";
        user.password = "pass";
        user.email = "juan@server.com";
        user.save();
        
        App app = new App();
        app.name = App.SIMS_APP_NAME;
        app.save();
        
        Role adminRole = new Role();
        adminRole.app = app;
        adminRole.name = Role.SIMS_CREATE_APP_ROLE;
        adminRole.save();
        
        Role auditRole = new Role();
        auditRole.app = app;
        auditRole.name = Role.SIMS_AUDIT_ROLE;
        auditRole.save();
        
        user.roles.add(adminRole);
        user.save();
        
        List<Role> userRoles = user.getRoles(app);
        
        assertEquals(1, userRoles.size());
        assertEquals(app.name, userRoles.get(0).app.name);
        assertEquals(Role.SIMS_CREATE_APP_ROLE, userRoles.get(0).name);
    }

}
