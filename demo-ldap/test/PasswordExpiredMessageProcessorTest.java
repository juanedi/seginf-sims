import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import ar.uba.dc.seginf.sims.messages.PasswordChangedMessage;
import ar.uba.dc.seginf.sims.messages.PasswordExpiredMessage;
import ar.uba.dc.seginf.sims.util.ISODateUtils;
import play.libs.Crypto;
import play.libs.Crypto.HashType;
import play.modules.spring.Spring;
import play.test.UnitTest;
import services.LDAPService;
import services.PasswordChangedMessageProcessor;
import services.PasswordExpiredMessageProcessor;


public class PasswordExpiredMessageProcessorTest extends UnitTest {
	
	private LDAPService ldapService;
	private PasswordExpiredMessageProcessor processor;
	
    @Before
    public void init() {
    	ldapService = Spring.getBeanOfType(LDAPService.class);
    	processor = new PasswordExpiredMessageProcessor(ldapService);
    }

	@Test
	public final void testPasswordExpired() throws ParseException {
		assertTrue(ldapService.authenticate("rfestini", "rfestini"));
		
		PasswordExpiredMessage msg = new PasswordExpiredMessage("rfestini", "MD5", "rfestini");
		processor.process(msg);
		
		assertFalse(ldapService.authenticate("rfestini", "rfestini"));
	}
	
}
