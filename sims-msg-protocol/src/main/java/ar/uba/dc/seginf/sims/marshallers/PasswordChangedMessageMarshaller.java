package ar.uba.dc.seginf.sims.marshallers;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;

import ar.uba.dc.seginf.sims.messages.Message;
import ar.uba.dc.seginf.sims.messages.PasswordChangedMessage;
import ar.uba.dc.seginf.sims.util.ISODateUtils;

public class PasswordChangedMessageMarshaller extends RegexpMessageMarshaller<PasswordChangedMessage>{
    
    private static final String PATTERN = "([\\w]+),"  				// username
            							+ "([\\w]+),"               // hashType
            							+ "((?:[\\w]|\\+|/|=)+),"    // password hasheado
            							+ "((?:[\\d]|\\-)+)";		// fecha de expiraci—n

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
        return new PasswordChangedMessage(username, hashType, password, date);
    }

    /** @see RegexpMessageMarshaller#doMarshall(Message) */
    @Override
    protected String doMarshall(PasswordChangedMessage msg) {
        return listJoin(
                msg.getUsername(),
                msg.getHashType(), 
                msg.getPassword(),
                ISODateUtils.format(msg.getPasswordExpiration()));
    }
    
    private static Date parseDate(final String dateStr) {
    	try {
			return ISODateUtils.parse(dateStr);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
    }
    
}


