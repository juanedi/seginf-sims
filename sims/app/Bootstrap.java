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
//@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        Fixtures.deleteDatabase();
        Fixtures.loadModels("data.yml");
    }
    
}
