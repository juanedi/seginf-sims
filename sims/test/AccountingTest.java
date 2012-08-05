import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import models.App;
import models.Event;
import models.EventType;
import models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.db.jpa.JPA;
import play.test.UnitTest;
import services.AccountingLogger;
import services.DatabaseAccountingLogger;
import services.DatabaseEventSearchService;
import services.EventSearchService;
import services.MockDateProvider;


/**
 * Tests de servicios de auditoría.
 * 
 * 
 * @author Juan Edi
 *
 */
public class AccountingTest extends UnitTest {

	AccountingLogger logger;
	EventSearchService searchService;
	private DateFormat dateFormat;
	private MockDateProvider dateProvider;

	private User admin;
	private User jedi;
	private App sims;
	private App demodb;
	
	@Before
	public final void init() {
		dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateProvider = new MockDateProvider("dd-MM-yyyy");
		
		logger = new DatabaseAccountingLogger(dateProvider);
		searchService = new DatabaseEventSearchService();
		
		admin = User.forUsername("admin");
		jedi = new User();
        jedi.email = "jedi@dc.uba.ar";
        jedi.firstName = "Juan";
        jedi.lastName = "Edi";
        jedi.username = "jedi";
        jedi.setPassword("jedi");
		jedi.save();
		
		sims = App.sims();
		demodb = App.forName("demodb");
		
		dateProvider.setDate("2010-01-01");
		logger.logAppCreated(admin, sims);
		logger.logAppCreated(admin, demodb);
		logger.logPasswordChange(jedi);
		
		dateProvider.setDate("2010-03-01");
		logger.logPasswordChange(admin);
		
		dateProvider.setDate("2010-04-01");
		logger.logRoleChanged(admin, admin, sims.roles.get(0));
	}

    /** rollback :) */
    @After
    public void rollBack() {
        JPA.setRollbackOnly();
    }
	
	@Test
	public final void testSearchByType() {
		List<Event> results = searchService.search(EventType.APP_CREATED, null, null, null, null, null);
		assertEquals(2, results.size());
		assertEquals(EventType.APP_CREATED, results.get(0).type);
		assertEquals(EventType.APP_CREATED, results.get(1).type);
	}
	
	@Test
	public final void testSearchByUser() {
		List<Event> results = searchService.search(null, jedi, null, null, null, null);
		assertEquals(1, results.size());
		assertEquals(jedi, results.get(0).responsible);
	}
	
	@Test
	public final void testSearchByApplication() {
		List<Event> results = searchService.search(null, null, Collections.singletonList(demodb), null, null, null);
		assertEquals(1, results.size());
		assertTrue(results.get(0).relatedApps.contains(demodb));
	}
	
	@Test
	public final void testSearchByRelatedUsers() {
		List<Event> results = searchService.search(null, null, null, Collections.singletonList(jedi), null, null);
		assertEquals(1, results.size());
		assertTrue(results.get(0).relatedUsers.contains(jedi));
	}
	
	@Test
	public final void testDateLowerBound() throws ParseException {
		List<Event> results = searchService.search(null, null, null, null, dateFormat.parse("2010-02-01"), null);
		assertEquals(2, results.size());
	}
	
	@Test
	public final void testDateUpperBound() throws ParseException {
		List<Event> results = searchService.search(null, null, null, null, null, dateFormat.parse("2010-02-01"));
		assertEquals(3, results.size());
	}
	
	@Test
	public final void testDateRange() throws ParseException {
		List<Event> results = searchService.search(null, null, null, null, 
												   dateFormat.parse("2010-03-01"), 
												   dateFormat.parse("2010-03-01"));
		assertEquals(1, results.size());		
	}
}
