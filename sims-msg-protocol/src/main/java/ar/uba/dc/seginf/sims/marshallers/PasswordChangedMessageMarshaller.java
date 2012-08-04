package ar.uba.dc.seginf.sims.marshallers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

import ar.uba.dc.seginf.sims.messages.PasswordChangedMessage;

public class PasswordChangedMessageMarshaller extends RegexpMessageMarshaller<PasswordChangedMessage>{
    
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
    private static final String PATTERN = "([\\w]+),"  				// username
            							+ "([\\w]+),"               // hashType
            							+ "((?:[\\w]|\\+|/|=)+),"    // password hasheado
            							+ "((?:[\\d]|\\-)+),"				// fecha de cambio
            							+ "([\\w]+)";   			// d�as de validez

    /** Creates the NewUserMessageMarshaller. */
    public PasswordChangedMessageMarshaller() {
        super(PATTERN);
    }

    /** @see RegexpMessageMarshaller#doUnMarshall(String, Matcher) */
    @Override
    protected PasswordChangedMessage doUnMarshall(String msg, Matcher matcher) {
        String username = matcher.group(1);
        String hashType = matcher.group(2);
        String password = matcher.group(3);
        Date date = parseDate(matcher.group(4));
        Integer daysValid = Integer.valueOf(matcher.group(5));
        return new PasswordChangedMessage(username, hashType, password, date, daysValid);
    }

    /** @see RegexpMessageMarshaller#doMarshall(Message) */
    @Override
    protected String doMarshall(PasswordChangedMessage msg) {
        return listJoin(
                msg.getUsername(),
                msg.getHashType(), 
                msg.getPassword(),
                serializeDate(msg.getServerDate()),
                String.valueOf(msg.getDaysValid()));
    }
    
    private static Date parseDate(final String dateStr) {
    	try {
			return new SimpleDateFormat(DATE_FORMAT).parse(dateStr);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
    }
    
    private static String serializeDate(final Date date) {
    	return new SimpleDateFormat(DATE_FORMAT).format(date);
    }
}


