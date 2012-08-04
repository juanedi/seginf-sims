package ar.uba.dc.seginf.sims.marshallers;

import java.util.regex.Matcher;

import ar.uba.dc.seginf.sims.messages.PasswordExpiredMessage;

public class PasswordExpiredMessageMarshaller extends RegexpMessageMarshaller<PasswordExpiredMessage> {

    private static final String PATTERN = "([\\w]+),"  				// username
										+ "([\\w]+),"               // hashType
										+ "((?:[\\w]|\\+|/|=)+)";   // password hasheado

	/** construye el marshaller */
	protected PasswordExpiredMessageMarshaller() {
		super(PATTERN);
	}

	@Override
	protected PasswordExpiredMessage doUnMarshall(String msg, Matcher matcher) {
        String username = matcher.group(1);
        String hashType = matcher.group(2);
        String password = matcher.group(3);
        return new PasswordExpiredMessage(username, hashType, password);
	}

	@Override
	protected String doMarshall(PasswordExpiredMessage msg) {
        return listJoin(
                msg.getUsername(),
                msg.getHashType(), 
                msg.getPassword());
	}

}
