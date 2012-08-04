package ar.uba.dc.seginf.sims.marshallers;

import static org.junit.Assert.*;

import org.junit.Test;

import ar.uba.dc.seginf.sims.messages.PasswordChangedMessage;


/**
 * Test para {@link PasswordChangedMessageMarshaller}
 * 
 * @author jedi
 *
 */
public class PasswordChangeMessageMarshallerTest {

	private MessageMarshaller<PasswordChangedMessage> marshaller = new PasswordChangedMessageMarshaller();
	
	@Test
	public final void testMarshall() {
		PasswordChangedMessage msg = new PasswordChangedMessage("pepe", "SHA512", "123");
		String str = marshaller.marshall(msg);
		assertEquals("pepe,SHA512,123", str);
	}
	
	@Test
	public final void testUnmarshall() {
		PasswordChangedMessage msg = marshaller.unMarshall("pepe,SHA512,123");
		assertEquals("pepe", msg.getUsername());
		assertEquals("SHA512", msg.getHashType());
		assertEquals("123", msg.getPassword());
	}
	
}
