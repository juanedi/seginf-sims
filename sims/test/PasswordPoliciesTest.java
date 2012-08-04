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

	private User user;
	private PasswordPolicy aPolicy;
	
	@Before
	public final void init() {
		// mayúsculas
		// minúsculas
		// caracteres especials
		// números
		// expira en 5 días
		// tiene que ser distinta a la anterior
		this.aPolicy = new PasswordPolicy("una", 4, true, true, true, true, 5, 2);
		this.aPolicy.save();
		
		user = User.forUsername("admin");
	}
	
    /** rollback :) */
    @After
    public void rollBack() {
        JPA.setRollbackOnly();
    }

    @Test
    /** se valida formato de la clave*/
    public void validatFormat() {
    	// formato inválida
    	assertFalse(aPolicy.validate(user, "aas33#"));

    	// formato válido
		assertTrue(aPolicy.validate(user, "12e@SS"));
    }
    
    @Test
    /** se chequea que sea distinta a las N anteriores */
    public void checkDifferentToLast() throws ParseException {
    	String validPass = "12e@SS";
    	
		// simulo que la clave anterior es igual a la nueva.
        user.passwords.add(new models.Password(user, validPass, ISODateUtils.parse("2010-01-01")));
        user.save();
        assertFalse(aPolicy.validate(user, validPass));
        
		// ahora la anteúltima es igual a la nueva.
        // también falla porque la política pide chequear 2.
        user.passwords.add(new models.Password(user, "22e@SS", ISODateUtils.parse("2010-01-02")));
        user.save();
        assertFalse(aPolicy.validate(user, validPass));
        
		// ahora la que es igual es la antepenúltima.
        // la validación es exitosa.
        user.passwords.add(new models.Password(user, "33e@SS", ISODateUtils.parse("2010-01-03")));
        user.save();
        assertTrue(aPolicy.validate(user, validPass));
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
