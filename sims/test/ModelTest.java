import org.junit.*;
import java.util.*;

import play.db.jpa.JPA;
import play.test.*;
import models.*;

public class ModelTest extends UnitTest {

    private User user;
    private App app;
    private Role rol1;
    private Role rol2;

    /** init */
    @Before
    public void init() {
        user = new User();
        user.username = "juan";
        user.firstName = "Juan";
        user.lastName = "Edi";
        user.setPassword("pass");
        user.email = "juan@server.com";
        user.save();
        
        app = new App();
        app.name = "aplicacion";
        app.hashType = Hash.MD5;
        app.owner = user;
        app.save();
        
        rol1 = new Role();
        rol1.app = app;
        rol1.name = "unRol";
        rol1.save();
        
        rol2 = new Role();
        rol2.app = app;
        rol2.name = "otroRol";
        rol2.save();
        
        user.roles.add(rol1);
        user.save();
        
    }

    /** rollback :) */
    @After
    public void rollBack() {
        JPA.setRollbackOnly();
    }
    
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
        List<Role> userRoles = user.getRoles(app);
        
        assertEquals(1, userRoles.size());
        assertEquals(app.name, userRoles.get(0).app.name);
        assertEquals(rol1.name, userRoles.get(0).name);
    }

    /** Chequeo de roles */
    @Test 
    public void checkRoleTest() {
        // rol que tiene
        assertTrue(user.hasRole("aplicacion", "unRol"));
        
        // rol que no tiene
        assertFalse(user.hasRole("aplicacion", "otroRol"));
        
        // rol que no existe
        assertFalse(user.hasRole("aplicacion", "ningunRol"));
        
        // usuario que no existe
        assertFalse(user.hasRole("ningún usuario", "unRol"));
    }
    
    /** Aplicaciones a configurar por un usuario */
    @Test
    public void appsToConfigure() {
        List<App> appsToConfigureByUser = App.toConfigureBy(user);
        assertEquals(1, appsToConfigureByUser.size());
        assertEquals(app.name, appsToConfigureByUser.get(0).name);
    }
 
}
