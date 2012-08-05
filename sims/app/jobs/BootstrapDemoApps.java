package jobs;

import play.jobs.Job;
import play.jobs.OnApplicationStart;
import models.App;
import models.Hash;
import models.Role;
import models.User;

@OnApplicationStart
public class BootstrapDemoApps extends Job {

	public void doJob() {
		User admin = User.forUsername("admin");
		
		if (App.forName("demoldap") == null) {
			System.out.println("Configurando por única vez Demo LDAP");
			setupDemoLdap(admin);
		}
		
		if (App.forName("demodb") == null) {
			System.out.println("Configurando por única vez Demo DB");
			setupDemoDB(admin);
		}
	}
	
	private void setupDemoLdap(User owner) {
		App demoldap = new App();
        demoldap.name = "demoldap";
        demoldap.hashType = Hash.MD5;
        demoldap.configured = true;
        
        Role roleA = new Role();
        roleA.app = demoldap;
        roleA.name = "A";
        demoldap.roles.add(roleA);
        
        Role roleB = new Role();
        roleB.app = demoldap;
        roleB.name = "B";
        demoldap.roles.add(roleB);
        
        Role roleC = new Role();
        roleC.app = demoldap;
        roleC.name = "C";
        demoldap.roles.add(roleC);
        
        demoldap.owner = owner;
        demoldap.save();
	}
	
	private void setupDemoDB(User owner) {
        App demodb = new App();
        demodb.name = "demodb";
        demodb.hashType = Hash.SHA256;
        demodb.configured = true;
        
        Role demoDbAmin = new Role();
        demoDbAmin.app = demodb;
        demoDbAmin.name = "ADMIN";
        
        demodb.roles.add(demoDbAmin);
        demodb.owner = owner;
        demodb.save();		
	}
}
