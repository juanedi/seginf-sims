package ar.uba.dc.seginf.sims.marshallers;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
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
		PasswordChangedMessage msg = new PasswordChangedMessage("pepe", "SHA512", "123", sampleDate());
		String str = marshaller.marshall(msg);
		assertEquals("pepe,SHA512,123,1989-01-06", str);
	}
	
	@Test
	public final void testUnmarshall() {
		PasswordChangedMessage msg = marshaller.unMarshall("pepe,SHA512,123,1989-01-06");
		Date expectedDate = DateUtils.truncate(sampleDate(), Calendar.DAY_OF_MONTH);
		assertEquals("pepe", msg.getUsername());
		assertEquals("SHA512", msg.getHashType());
		assertEquals("123", msg.getPassword());
		assertEquals(expectedDate, msg.getPasswordExpiration());
	}
	
	/** fecha de prueba: 6 de enero de 1989, 22 hs. */
	private Date sampleDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1989);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 6);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		return cal.getTime();
	}
}
