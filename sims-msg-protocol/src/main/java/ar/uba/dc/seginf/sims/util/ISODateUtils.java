package ar.uba.dc.seginf.sims.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Parsea y serializa fechas usando el formato ISO (yyyy-MM-dd).
 * 
 * @author jedi
 *
 */
public class ISODateUtils {

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	public static Date parse(final String str) throws ParseException {
		return dateFormat().parse(str);
	}
	
	public static String format(final Date date) {
		return dateFormat().format(date);
	}
	
	/** date format, se crea para cada uso porque no es thread safe. */
	private static DateFormat dateFormat() {
		return new SimpleDateFormat(DATE_FORMAT);		
	}
}
