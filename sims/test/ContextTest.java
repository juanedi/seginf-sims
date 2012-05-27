import org.junit.*;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import play.modules.spring.Spring;
import play.test.UnitTest;


/**
 * Test de configuración de contexto
 * 
 * 
 * @author Juan Edi
 * @since May 27, 2012
 */
public class ContextTest extends UnitTest {

    /** Levanta un bean que está seguro en el contexto */
    @Test
    public void basicContextTest() {
        Object bean = Spring.getBeanOfType(PropertyPlaceholderConfigurer.class);
        assertNotNull(bean);
    }
    
    /** Chequea que esté el amqpTemplate para interactuar con rabbitmq */
    @Test
    public void rabbitContextTest() {
        Object connectionFactory = Spring.getBeanOfType(ConnectionFactory.class);
        assertNotNull(connectionFactory);

        Object rabbitAdmin = Spring.getBeanOfType(AmqpAdmin.class);
        assertNotNull(rabbitAdmin);
        
        Object amqpTemplate = Spring.getBeanOfType(AmqpTemplate.class);
        assertNotNull(amqpTemplate);
    }
    
}
