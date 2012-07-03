package ar.uba.dc.seginf.sims.marshallers;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import ar.uba.dc.seginf.sims.messages.UserRolesChangedMessage;

/**
 * Test para {@link UserRolesChangedMessageMarshaller}.
 * 
 * 
 * @author Juan Edi
 * @since Jul 3, 2012
 */
public class UserRolesChangedMessageMarshallerTest {

    private MessageMarshaller<UserRolesChangedMessage> marshaller = new UserRolesChangedMessageMarshaller();
    
    @Test
    public final void emptyRolesMarshallTest() {
        UserRolesChangedMessage msg = new UserRolesChangedMessage("juanedi", Collections.<String>emptyList());
        String str = marshaller.marshall(msg);
        assertEquals("juanedi,[]", str);
    }
    
    @Test
    public final void singleRoleMarshallTest() {
        UserRolesChangedMessage msg = new UserRolesChangedMessage("juanedi", Arrays.asList("ROL1"));
        String str = marshaller.marshall(msg);
        assertEquals("juanedi,[ROL1]", str);
    }
    
    @Test
    public final void multipleRolesMarshallTest() {
        UserRolesChangedMessage msg = new UserRolesChangedMessage("juanedi", Arrays.asList("ROL1", "ROL2"));
        String str = marshaller.marshall(msg);
        assertEquals("juanedi,[ROL1,ROL2]", str);
    }
    
    /** mensaje inválido */
    @Test(expected = IllegalArgumentException.class)
    public final void failedUnMarshallTest() {
        marshaller.unMarshall("juanedi,123");
    }

    @Test
    public final void emptyRolesUnMarshallTest() {
        UserRolesChangedMessage msg = marshaller.unMarshall("juanedi,[]");
        assertEquals("juanedi", msg.getUsername());
        assertEquals(0, msg.getRoles().size());
    }
    
    @Test
    public final void singleRoleUnMarshallTest() {
        UserRolesChangedMessage msg = marshaller.unMarshall("juanedi,[ROL1]");
        assertEquals("juanedi", msg.getUsername());
        assertEquals(1, msg.getRoles().size());
        assertEquals("ROL1", msg.getRoles().get(0));
    }
    
    @Test
    public final void multipleRolesUnMarshallTest() {
        UserRolesChangedMessage msg = marshaller.unMarshall("juanedi,[ROL1,ROL2,ROL3]");
        assertEquals("juanedi", msg.getUsername());
        assertEquals(3, msg.getRoles().size());
        assertEquals("ROL1", msg.getRoles().get(0));
        assertEquals("ROL2", msg.getRoles().get(1));
        assertEquals("ROL3", msg.getRoles().get(2));
    }
    
}
