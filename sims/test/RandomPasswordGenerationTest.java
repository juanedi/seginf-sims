import org.junit.Test;
import org.springframework.util.Assert;

import net.sf.oval.constraint.AssertTrue;
import services.RandomPasswordUtils;
import models.PasswordPolicy;


/**
 * Test para generación de claves random para una política.
 * 
 * @author jedi
 *
 */
public class RandomPasswordGenerationTest {

	@Test
	public void testGenerateForDifferentPolicies() {
		PasswordPolicy[] policies = {
				new PasswordPolicy("a", 20, true, true, true, true, 1, 1),
				new PasswordPolicy("a", 20, true, true, true, false, 1, 1),
				new PasswordPolicy("a", 20, true, true, false, true, 1, 1),
				new PasswordPolicy("a", 20, true, true, false, false, 1, 1),
				new PasswordPolicy("a", 20, true, false, true, true, 1, 1),
				new PasswordPolicy("a", 20, true, false, true, false, 1, 1),
				new PasswordPolicy("a", 20, true, false, false, true, 1, 1),
				new PasswordPolicy("a", 20, true, false, false, false, 1, 1),
				new PasswordPolicy("a", 20, false, true, true, true, 1, 1),
				new PasswordPolicy("a", 20, false, true, true, false, 1, 1),
				new PasswordPolicy("a", 20, false, true, false, true, 1, 1),
				new PasswordPolicy("a", 20, false, true, false, false, 1, 1),
				new PasswordPolicy("a", 20, false, false, true, true, 1, 1),
				new PasswordPolicy("a", 20, false, false, true, false, 1, 1),
				new PasswordPolicy("a", 20, false, false, false, true, 1, 1),
				new PasswordPolicy("a", 20, false, false, false, false, 1, 1),
		};
		
		for (PasswordPolicy policy : policies) {
			for(int i = 0; i < 5; i++) {
				String password = RandomPasswordUtils.generateRandomPassword(policy);
				Assert.isTrue(policy.checkFormat(password));
			}
		}
	}
	
}
