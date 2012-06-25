package ar.uba.dc.seginf.sims.marshallers;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import ar.uba.dc.seginf.sims.messages.NewUserMessage;

/**
 * Parsea y serializa mensajes de usuario nuevo.
 * 
 * Formato:
 * usuario,hashType,password,[rol1,rol2,...]
 * 
 * 
 * @author Juan Edi
 * @since May 28, 2012
 */
public class NewUserMessageMarshaller extends RegexpMessageMarshaller<NewUserMessage>{

    private static final String PATTERN = "([\\w]+),"               // username
                                        + "([\\w]+),"               // firstName
                                        + "([\\w]+),"               // lastName
                                        + "([\\w]+),"               // hashType
                                        + "((?:[\\w]|\\+|/|=)+),"   // password hasheado
                                        + "\\[((?:[\\w],?)*)\\]";   // listado de roles separados por coma
                                                                    // se captura un grupo con todo el listado
                                                                    // el '?' se usa para no capturar el \\w de
                                                                    // adentro   
    
    /** Creates the NewUserMessageMarshaller. */
    public NewUserMessageMarshaller() {
        super(PATTERN);
    }

    /** @see RegexpMessageMarshaller#doUnMarshall(String, Matcher) */
    @Override
    protected NewUserMessage doUnMarshall(String msg, Matcher matcher) {
        String username = matcher.group(1);
        String firstName = matcher.group(2);
        String lastName = matcher.group(3);
        String hashType = matcher.group(4);
        String password = matcher.group(5);
        String roleList = matcher.group(6);
        List<String> roles = Arrays.asList(StringUtils.split(roleList, ","));
        return new NewUserMessage(username, firstName, lastName, hashType, password, roles);
    }

    /** @see RegexpMessageMarshaller#doMarshall(Message) */
    @Override
    protected String doMarshall(NewUserMessage msg) {
        return listJoin(
                msg.getUsername(),
                msg.getFirstName(),
                msg.getLastName(),
                msg.getHashType(), 
                msg.getPassword(), 
                "[" + listJoin(msg.getRoles().toArray()) + "]");
    }


}
