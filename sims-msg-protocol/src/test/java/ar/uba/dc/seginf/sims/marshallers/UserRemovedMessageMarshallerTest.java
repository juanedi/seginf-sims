package ar.uba.dc.seginf.sims.marshallers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ar.uba.dc.seginf.sims.messages.UserRemovedMessage;

/**
 * Test para {@link UserRemovedMessageMarshaller}.
 * 
 * @author jedi
 *
 */
public class UserRemovedMessageMarshallerTest {
	
	private MessageMarshaller<UserRemovedMessage> marshaller = new UserRemovedMessageMarshaller();
	
	@Test
	public final void testMarshall() {
		UserRemovedMessage msg = new UserRemovedMessage("pepe");
		String str = marshaller.marshall(msg);
		assertEquals("pepe", str);
	}
	
	@Test
	public final void testUnmarshall() {
		UserRemovedMessage msg = marshaller.unMarshall("pepe");
		assertEquals("pepe", msg.getUsername());
	}
	
}
