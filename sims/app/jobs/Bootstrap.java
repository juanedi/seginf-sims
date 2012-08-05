package jobs;
import java.util.Calendar;
import java.util.Date;

import models.App;
import models.Hash;
import models.PasswordPolicy;
import models.Role;
import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

/**
 * Carga datos iniciales que requiere la aplicación.
 * 
 * 
 * @author Juan Edi
 * @since May 19, 2012
 */
@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
    	if (App.count() == 0L) {  // si no hay aplicaciones es porque la base de datos está vacía.
    		Fixtures.deleteDatabase();
    		System.out.println("Configurando por primera vez la aplicación. Las credenciales de login son admin/admin");
    		setupSIMS();
    	}
    }

    public final void setupSIMS() {
    	/*------- SIMS APP -------*/
        App sims = new App();
        sims.name = "sims";
        sims.hashType = Hash.SHA512;
        sims.configured = true;
        
        Role createAppRole = new Role();
        createAppRole.app = sims;
        createAppRole.name = Role.SIMS_CREATE_APP_ROLE;
        
        Role createUserRole = new Role();
        createUserRole.app = sims;
        createUserRole.name = Role.SIMS_CREATE_USER_ROLE;
        
        Role auditRole = new Role();
        auditRole.app = sims;
        auditRole.name = Role.SIMS_AUDIT_ROLE;
        
        Role passwordPolicyRole = new Role();
        passwordPolicyRole.app = sims;
        passwordPolicyRole.name = Role.SIMS_PASSWORD_POLICY_ROLE;
        
        sims.roles.add(createAppRole);
        sims.roles.add(createUserRole);
        sims.roles.add(auditRole);
        sims.roles.add(passwordPolicyRole);
        

        /*------- USERS -------*/
        User admin = new User();
        admin.email = "seginfapp@gmail.com";
        admin.firstName = "Administrador";
        admin.lastName = "de Identidades";
        admin.username = "admin";
        admin.setPassword("admin");
        admin.apps.add(sims);
        admin.roles.add(createAppRole);
        admin.roles.add(createUserRole);
        admin.roles.add(auditRole);
        admin.roles.add(passwordPolicyRole);
        admin.lastPasswordChanged = aboutToExpire(); // para que vea la notificación de cambio de clave 
        
        sims.owner = admin;
        
        /*------- PASSWORD POLICY -------*/
        PasswordPolicy policy = new PasswordPolicy("default", 6, false, false, false, false, 90, 1);
        policy.activate();
        
        /*------- PERSIST -------*/
        sims.save();
        admin.save();
        policy.save();    	
    }
    
    /** provee una fecha tal que la clave esté cerca de expirar */
    private Date aboutToExpire() {
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DAY_OF_YEAR, -80);
    	return cal.getTime();
    }
}
