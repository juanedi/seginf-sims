import models.App;
import models.Hash;
import models.Role;
import models.User;
import play.db.jpa.JPAPlugin;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

/**
 * Carga los datos de data.yml
 * 
 * 
 * @author Juan Edi
 * @since May 19, 2012
 */
@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        Fixtures.deleteDatabase();
        
        /*------- SIMS APP -------*/
        App sims = new App();
        sims.name = "sims";
        sims.hashType = Hash.SHA512;
        
        Role createAppRole = new Role();
        createAppRole.app = sims;
        createAppRole.name = Role.SIMS_CREATE_APP_ROLE;
        
        Role createUserRole = new Role();
        createUserRole.app = sims;
        createUserRole.name = Role.SIMS_CREATE_USER_ROLE;
        
        Role auditRole = new Role();
        auditRole.app = sims;
        auditRole.name = Role.SIMS_AUDIT_ROLE;
        
        sims.roles.add(createAppRole);
        sims.roles.add(createUserRole);
        sims.roles.add(auditRole);
        
        /*------- DEMO LDAP -------*/
        
        App demoldap = new App();
        demoldap.name = "demoldap";
        demoldap.hashType = Hash.MD5;
        
        Role demoLdapBase = new Role();
        demoLdapBase.app = demoldap;
        demoLdapBase.name = "base";
        
        demoldap.roles.add(demoLdapBase);
        
        
        /*------- DEMO DB -------*/
        
        App demodb = new App();
        demodb.name = "demodb";
        demodb.hashType = Hash.SHA256;
        
        Role demoDbAmin = new Role();
        demoDbAmin.app = demodb;
        demoDbAmin.name = "admin";
        
        demodb.roles.add(demoDbAmin);
        
        
        /*------- USERS -------*/
        User admin = new User();
        admin.email = "admin@sims.com";
        admin.firstName = "Pedro";
        admin.lastName = "Administrador";
        admin.username = "admin";
        admin.setPassword("admin");
        admin.apps.add(sims);
        admin.roles.add(createAppRole);
        admin.roles.add(createUserRole);
        admin.roles.add(auditRole);
        
        User jedi = new User();
        jedi.email = "jedi@dc.uba.ar";
        jedi.firstName = "Juan";
        jedi.lastName = "Edi";
        jedi.username = "jedi";
        jedi.setPassword("jedi");
        jedi.apps.add(sims);
        
        sims.owner = admin;
        demoldap.owner = admin;
        demodb.owner = admin;
        
        /*------- PERSIST -------*/
        
        sims.save();
        demoldap.save();
        demodb.save();
        
        jedi.save();
        admin.save();
    }
    
}
