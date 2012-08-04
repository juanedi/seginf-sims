import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.PasswordPolicy;
import models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.uba.dc.seginf.sims.util.ISODateUtils;

import play.db.jpa.JPA;
import play.test.UnitTest;


public class PasswordPoliciesTest extends UnitTest {

	private PasswordPolicy aPolicy;
	
	@Before
	public final void init() {
		this.aPolicy = new PasswordPolicy("una", 4, true, true, true, true, 5,2);
		this.aPolicy.save();
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
        assertTrue(aPolicy.checkPassword("12e@SS"));
        
        // password inválida
        assertFalse(aPolicy.checkPassword("aas33#"));
        
    }

    /** Obtención de la política actual */
    @Test
    public final void getCurrentTest() throws InterruptedException {
    	PasswordPolicy defaultPolicy = PasswordPolicy.find("byName", "default").first();
    	
    	PasswordPolicy current = PasswordPolicy.current();
    	assertNotNull(current);
    	assertEquals(defaultPolicy.name, current.name);
    	
    	activate(aPolicy);
    	
    	current = PasswordPolicy.current();
    	assertNotNull(current);
    	assertEquals(aPolicy.name, current.name);
    	
    	activate(defaultPolicy);
    	
    	current = PasswordPolicy.current();
    	assertNotNull(current);
    	assertEquals(defaultPolicy.name, current.name);
    }

    /** cheque de usuarios con clave expirada */
    @Test
    public final void getExpiredUsersTest() throws ParseException {
    	// Todos los usuarios se crean con clave nueva.
    	assertTrue(PasswordPolicy.usersWithExpiredPasswords().isEmpty());
    	
    	// Dejo uno al borde dela expiración.
		User user = User.forUsername("admin");
		user.lastPasswordChanged = substract(new Date(), PasswordPolicy.current().duration);
		user.save();
		assertTrue(PasswordPolicy.usersWithExpiredPasswords().isEmpty());
		
		// Un día más y expira.
		user.lastPasswordChanged = substract(user.lastPasswordChanged, 1);
		user.save();
		assertFalse(PasswordPolicy.usersWithExpiredPasswords().isEmpty());
    }
    
    /** espera un momento para que cambie el timestamp y activa una policy */
    private void activate(PasswordPolicy policy) throws InterruptedException {
    	Thread.sleep(500L);
    	policy.activate();
    	policy.save();
    }
    
    private Date substract(final Date date, final Integer days) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.DAY_OF_YEAR, (-1) * days);
    	return cal.getTime();
    }
}
