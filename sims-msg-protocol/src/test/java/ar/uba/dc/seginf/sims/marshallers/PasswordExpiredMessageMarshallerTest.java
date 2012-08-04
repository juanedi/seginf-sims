package ar.uba.dc.seginf.sims.marshallers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ar.uba.dc.seginf.sims.messages.PasswordExpiredMessage;

public class PasswordExpiredMessageMarshallerTest {

	private MessageMarshaller<PasswordExpiredMessage> marshaller = new PasswordExpiredMessageMarshaller();
	
	@Test
	public final void testMarshall() {
		PasswordExpiredMessage msg = new PasswordExpiredMessage("pepe", "SHA512", "123");
		String str = marshaller.marshall(msg);
		assertEquals("pepe,SHA512,123", str);
	}
	
	@Test
	public final void testUnmarshall() {
		PasswordExpiredMessage msg = marshaller.unMarshall("pepe,SHA512,123");
		assertEquals("pepe", msg.getUsername());
		assertEquals("SHA512", msg.getHashType());
		assertEquals("123", msg.getPassword());
	}
	
}
