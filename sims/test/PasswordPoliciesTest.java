import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import models.PasswordPolicy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.db.jpa.JPA;
import play.test.UnitTest;


public class PasswordPoliciesTest extends UnitTest {

	private PasswordPolicy policy;
	
	@Before
	public final void init() {
		this.policy = new PasswordPolicy("una", 4, true, true, true, true, 60,2);
		this.policy.activate();
		this.policy.save();
	}
	
    /** rollback :) */
    @After
    public void rollBack() {
        JPA.setRollbackOnly();
    }
	
    /** Chequeo passwordCheck */
    @Test 
    public void checkCheckPasswordTest() {
        // password válida
        assertTrue(policy.checkPassword("12e@SS"));
        
        // password inválida
        assertFalse(policy.checkPassword("aas33#"));
        
    }

    /** Obtención de la política actual */
    @Test
    public final void getCurrentTest() throws InterruptedException {
    	PasswordPolicy other = new PasswordPolicy("otra", 2, false, false, false, false, 10,1);
    	other.save();
    	
    	PasswordPolicy current = PasswordPolicy.getCurrent();
    	assertNotNull(current);
    	assertEquals("una", current.name);
    	
    	activate(other);
    	
    	current = PasswordPolicy.getCurrent();
    	assertNotNull(current);
    	assertEquals("otra", current.name);
    	
    	activate(policy);
    	
    	current = PasswordPolicy.getCurrent();
    	assertNotNull(current);
    	assertEquals("una", current.name);
    }

    /** espera un momento para que cambie el timestamp y activa una policy */
    private void activate(PasswordPolicy policy) throws InterruptedException {
    	Thread.sleep(500L);
    	policy.activate();
    	policy.save();
    }
    
}
