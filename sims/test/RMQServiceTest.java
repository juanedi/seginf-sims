import javax.inject.Inject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import play.modules.spring.Spring;
import play.test.UnitTest;
import services.RMQService;
import services.RMQServiceImpl;

/**
 * Pruebas sobre {@link RMQService}.
 * Se deja ignorado para no ejecutar automáticamente (interactúa contra RMQ).
 * 
 * 
 * @author Juan Edi
 * @since May 28, 2012
 */
@Ignore
public class RMQServiceTest extends UnitTest {

    private RMQService rmqService;
    
    /** init */
    @Before
    public final void init() {
        rmqService = Spring.getBeanOfType(RMQService.class);
        assertNotNull(rmqService);
    }
    
    @Test
    public final void setupUserTest() {
        rmqService.setUser("testApp");
    }

    /** Modifica la clave */
    @Test
    public final void changePasswordTest() {
        rmqService.changeUserPassword("MYAPP", "123");
    }
    
    
}
