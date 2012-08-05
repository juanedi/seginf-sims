import java.text.ParseException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import ar.uba.dc.seginf.sims.messages.NewUserMessage;
import ar.uba.dc.seginf.sims.messages.PasswordExpiredMessage;
import ar.uba.dc.seginf.sims.messages.UserRemovedMessage;
import ar.uba.dc.seginf.sims.util.ISODateUtils;
import play.libs.Crypto;
import play.libs.Crypto.HashType;
import play.modules.spring.Spring;
import play.test.UnitTest;
import services.LDAPService;
import services.NewUserMessageProcessor;
import services.PasswordExpiredMessageProcessor;
import services.UserRemovedMessageProcessor;


public class UserRemovedMessageProcessorTest extends UnitTest {

	private LDAPService ldapService;
	private NewUserMessageProcessor newUserProcessor;
	private UserRemovedMessageProcessor processor;
	
    @Before
    public void init() {
    	ldapService = Spring.getBeanOfType(LDAPService.class);
    	newUserProcessor = new NewUserMessageProcessor(ldapService);
    	processor = new UserRemovedMessageProcessor(ldapService);
    }

	@Test
	public final void testPasswordExpired() throws ParseException {
		String plainPass = "pass";
		newUserProcessor.process(new NewUserMessage("pepe", "José", "Gozález", "MD5", 
								Crypto.passwordHash(plainPass, HashType.MD5), 
								ISODateUtils.parse("2012-12-01"), Collections.<String>emptyList()));
		
		assertTrue(ldapService.authenticate("pepe", "pass"));

		UserRemovedMessage msg = new UserRemovedMessage("pepe");
		processor.process(msg);
		
		assertFalse(ldapService.authenticate("pepe", "pass"));
	}

	
}
