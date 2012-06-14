package ar.uba.dc.seginf.sims.marshallers;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import ar.uba.dc.seginf.sims.messages.NewUserMessage;

/**
 * Test para {@link NewUserMessageMarshaller}.
 * 
 * 
 * @author Juan Edi
 * @since May 28, 2012
 */
public class NewUserMessageMashallerTest {

    private MessageMarshaller<NewUserMessage> marshaller = new NewUserMessageMarshaller();
    
    @Test
    public final void emptyRolesMarshallTest() {
        NewUserMessage msg = new NewUserMessage("juanedi", "Juan", "Edi", "MD5", "123", Collections.<String>emptyList());
        String str = marshaller.marshall(msg);
        assertEquals("juanedi,Juan,Edi,MD5,123,[]", str);
    }
    
    @Test
    public final void singleRoleMarshallTest() {
        NewUserMessage msg = new NewUserMessage("juanedi", "Juan", "Edi", "MD5", "123", Arrays.asList("ROL1"));
        String str = marshaller.marshall(msg);
        assertEquals("juanedi,Juan,Edi,MD5,123,[ROL1]", str);
    }
    
    @Test
    public final void multipleRolesMarshallTest() {
        NewUserMessage msg = new NewUserMessage("juanedi", "Juan", "Edi", "MD5", "123", Arrays.asList("ROL1", "ROL2"));
        String str = marshaller.marshall(msg);
        assertEquals("juanedi,Juan,Edi,MD5,123,[ROL1,ROL2]", str);
    }
    
    /** mensaje inválido */
    @Test(expected = IllegalArgumentException.class)
    public final void failedUnMarshallTest() {
        marshaller.unMarshall("juanedi,MD5,123");
    }

    @Test
    public final void emptyRolesUnMarshallTest() {
        NewUserMessage msg = marshaller.unMarshall("juanedi,Juan,Edi,MD5,123,[]");
        assertEquals("juanedi", msg.getUsername());
        assertEquals("Juan", msg.getFirstName());
        assertEquals("Edi", msg.getLastName());
        assertEquals("MD5", msg.getHashType());
        assertEquals("123", msg.getPassword());
        assertEquals(0, msg.getRoles().size());
    }
    
    @Test
    public final void singleRoleUnMarshallTest() {
        NewUserMessage msg = marshaller.unMarshall("juanedi,Juan,Edi,MD5,123,[ROL1]");
        assertEquals("juanedi", msg.getUsername());
        assertEquals("Juan", msg.getFirstName());
        assertEquals("Edi", msg.getLastName());
        assertEquals("MD5", msg.getHashType());
        assertEquals("123", msg.getPassword());
        assertEquals(1, msg.getRoles().size());
        assertEquals("ROL1", msg.getRoles().get(0));
    }
    
    @Test
    public final void multipleRolesUnMarshallTest() {
        NewUserMessage msg = marshaller.unMarshall("juanedi,Juan,Edi,MD5,123,[ROL1,ROL2,ROL3]");
        assertEquals("juanedi", msg.getUsername());
        assertEquals("Juan", msg.getFirstName());
        assertEquals("Edi", msg.getLastName());
        assertEquals("MD5", msg.getHashType());
        assertEquals("123", msg.getPassword());
        assertEquals(3, msg.getRoles().size());
        assertEquals("ROL1", msg.getRoles().get(0));
        assertEquals("ROL2", msg.getRoles().get(1));
        assertEquals("ROL3", msg.getRoles().get(2));
    }
}
