package ar.uba.dc.seginf.sims.marshallers;

import java.util.regex.Matcher;

import ar.uba.dc.seginf.sims.messages.PasswordChangedMessage;

public class PasswordChangedMessageMarshaller extends RegexpMessageMarshaller<PasswordChangedMessage>{
    
    private static final String PATTERN = "([\\w]+),"  				// username
            							+ "([\\w]+),"               // hashType
            							+ "((?:[\\w]|\\+|/|=)+)";   // password hasheado

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
        return new PasswordChangedMessage(username, hashType, password);
    }

    /** @see RegexpMessageMarshaller#doMarshall(Message) */
    @Override
    protected String doMarshall(PasswordChangedMessage msg) {
        return listJoin(
                msg.getUsername(),
                msg.getHashType(), 
                msg.getPassword());
    }
}


