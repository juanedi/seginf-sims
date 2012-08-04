package util;
import models.App;
import models.Hash;
import models.PasswordPolicy;
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
        
        /*------- DEMO LDAP -------*/
        
        App demoldap = new App();
        demoldap.name = "demoldap";
        demoldap.hashType = Hash.MD5;
        demoldap.configured = true;
        
        Role demoLdapBase = new Role();
        demoLdapBase.app = demoldap;
        demoLdapBase.name = "base";
        
        demoldap.roles.add(demoLdapBase);
        
        
        /*------- DEMO DB -------*/
        
        App demodb = new App();
        demodb.name = "demodb";
        demodb.hashType = Hash.SHA256;
        demodb.configured = true;
        
        Role demoDbAmin = new Role();
        demoDbAmin.app = demodb;
        demoDbAmin.name = "ADMIN";
        
        demodb.roles.add(demoDbAmin);
        
        /*------- OTRA APP -------*/
        
        App prueba = new App();
        prueba.name = "prueba";
        prueba.hashType = Hash.MD5;
        prueba.configured = false;

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
        admin.roles.add(passwordPolicyRole);
        
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
        prueba.owner = admin;
        
        /*------- PASSWORD POLICY -------*/
        PasswordPolicy policy = new PasswordPolicy("default", 1, false, false, false, false, 90, 3);
        policy.activate();
        
        /*------- PERSIST -------*/
        
        sims.save();
        demoldap.save();
        demodb.save();
        prueba.save();
        
        jedi.save();
        admin.save();
        
        policy.save();
    }
    
}
