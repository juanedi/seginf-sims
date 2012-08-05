import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import ar.uba.dc.seginf.sims.messages.PasswordChangedMessage;
import ar.uba.dc.seginf.sims.util.ISODateUtils;

import play.libs.Crypto;
import play.libs.Crypto.HashType;
import play.modules.spring.Spring;
import play.test.UnitTest;
import services.LDAPService;
import services.PasswordChangedMessageProcessor;

/**
 * Test para {@link PasswordChangedMessageProcessor}
 * 
 */
public class PasswordChangedMessageProcessorTest extends UnitTest {

	private LDAPService ldapService;
	private PasswordChangedMessageProcessor processor;
	
    @Before
    public void init() {
    	ldapService = Spring.getBeanOfType(LDAPService.class);
    	processor = new PasswordChangedMessageProcessor(ldapService);
    }

	@Test
	public final void testChangePassword() throws ParseException {
		String plainPass= "nuevaClave";
		String hashedPass = Crypto.passwordHash(plainPass, HashType.MD5);
		assertFalse(ldapService.authenticate("rfestini", plainPass));
		
		PasswordChangedMessage msg = new PasswordChangedMessage("rfestini", "MD5", hashedPass, ISODateUtils.parse("2012-12-01"));
		processor.process(msg);
		
		assertTrue(ldapService.authenticate("rfestini", plainPass));
	}
	
	
}
