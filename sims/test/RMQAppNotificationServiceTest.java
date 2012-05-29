import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import models.App;
import models.User;
import play.db.jpa.GenericModel.JPAQuery;
import play.modules.spring.Spring;
import play.test.UnitTest;
import services.AppNotificationService;


/**
 * Para probar las notificaciones a las applicaciones.
 * Se deja en ignore porque se comunica con RMQ.
 * 
 * Levanta datos de data.yxml.
 * 
 * @author Juan Edi
 * @since May 29, 2012
 */
@Ignore
public class RMQAppNotificationServiceTest extends UnitTest {

    private AppNotificationService notificationService;

    @Before
    public void init() {
        notificationService = Spring.getBeanOfType(AppNotificationService.class);
    }
 
    @Test
    public void testNewUserNotify() {
        User user = User.find("byUsername", "jedi").first();
        App app = App.find("byName", "demodb").first();

        assertNotNull(user);
        assertNotNull(app);
        notificationService.notifyNewUser(user, app);
    }
}
