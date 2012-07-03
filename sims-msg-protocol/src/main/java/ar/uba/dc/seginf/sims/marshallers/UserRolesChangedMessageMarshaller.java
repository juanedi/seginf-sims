package ar.uba.dc.seginf.sims.marshallers;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import ar.uba.dc.seginf.sims.messages.UserRolesChangedMessage;

/**
 * Parsea y serializa mensajes de cambio de roles de usuario.
 * 
 * Formato:
 * usuario,,[rol1,rol2,...]
 * 
 * @author Juan Edi
 * @since Jul 3, 2012
 */
public class UserRolesChangedMessageMarshaller extends RegexpMessageMarshaller<UserRolesChangedMessage> {

    private static final String PATTERN = "([\\w]+),"               // username
                                        + "\\[((?:[\\w],?)*)\\]";   // listado de roles separados por coma
                                                                    // se captura un grupo con todo el listado
                                                                    // el '?' se usa para no capturar el \\w de
                                                                    // adentro   

    
    /** Creates the UserRolesChangedMessageMarshaller. */
    protected UserRolesChangedMessageMarshaller() {
        super(PATTERN);
    }

    /** @see RegexpMessageMarshaller#doUnMarshall(Matcher) */
    @Override
    protected UserRolesChangedMessage doUnMarshall(String msg, Matcher matcher) {
        String username = matcher.group(1);
        String roleList = matcher.group(2);
        List<String> roles = Arrays.asList(StringUtils.split(roleList, ","));
        return new UserRolesChangedMessage(username, roles);
    }

    /** @see RegexpMessageMarshaller#doMarshall(Message) */
    @Override
    protected String doMarshall(UserRolesChangedMessage msg) {
        return listJoin(
                msg.getUsername(),
                "[" + listJoin(msg.getRoles().toArray()) + "]");
    }

}
