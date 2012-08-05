package ar.uba.dc.seginf.sims.marshallers;

import java.util.regex.Matcher;

import ar.uba.dc.seginf.sims.messages.UserRemovedMessage;

/**
 * Marshaller para {@link UserRemovedMessage}.
 * 
 * @author jedi
 *
 */
public class UserRemovedMessageMarshaller extends RegexpMessageMarshaller<UserRemovedMessage> {

	private static final String PATTERN = "([\\w]+)";

	public UserRemovedMessageMarshaller() {
		super(PATTERN);
	}

	@Override
	protected UserRemovedMessage doUnMarshall(String msg, Matcher matcher) {
        String username = matcher.group(1);
        return new UserRemovedMessage(username);
	}

	@Override
	protected String doMarshall(UserRemovedMessage msg) {
        return listJoin(msg.getUsername());
	}

}
