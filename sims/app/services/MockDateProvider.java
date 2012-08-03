package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.Validate;

public class MockDateProvider implements DateProvider {

	private static final String DEFAULT_FORMAT = "dd-MM-yyyy";
	
	private String format;
	private Date date;

	public MockDateProvider() {
		this(DEFAULT_FORMAT);
	}
	
	public MockDateProvider(final String dateFormat) {
		Validate.notEmpty(dateFormat);
		this.format = dateFormat;
		this.date = new Date();
	}
	
	@Override
	public Date getDate() {
		return date;
	}

	/** setea la fecha que devolverá para lo siguientes llamados. */
	public void setDate(String date) {
		Validate.notNull(date);
		try {
			this.date = new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("formato inválido. se espera " + format);
		}
	}
	
}
