package ar.uba.dc.seginf.sims.marshallers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import ar.uba.dc.seginf.sims.messages.Message;

/**
 * Implementación base de {@link MessageMarshaller} que 
 * utiliza como base una expresión regular.
 * 
 * Permite parsear con la expresión ya matcheada, y valida tras 
 * serializar que el mensaje matchee la expresión.
 * 
 * @author Juan Edi
 * @since May 28, 2012
 */
public abstract class RegexpMessageMarshaller<T extends Message> implements MessageMarshaller<T> {

    private final Pattern pattern;
    
    protected RegexpMessageMarshaller(final String pattern) {
        Validate.notNull(pattern);
        this.pattern = Pattern.compile(pattern);
    }
    
    /** @see MessageMarshaller#marshall(Message) */
    @Override
    public String marshall(T object) {
        String msg = doMarshall(object);
        if (!pattern.matcher(msg).matches()) {
            throw new IllegalStateException("Se serializó incorrectamente un mensaje");
        }
        return msg;
    }

    /** @see MessageMarshaller#unMarshall(java.lang.String) */
    @Override
    public T unMarshall(String msg) {
        Matcher matcher = pattern.matcher(msg);
        if (matcher.matches()) {
            return doUnMarshall(msg, matcher);
        } else {
           throw new IllegalArgumentException("Mensaje con formato inválido"); 
        }
    }

    /** Concatena los toString de todos los objectos con "," */
    protected static String listJoin(Object ... array) {
        return StringUtils.join(array, ',');
    }
    
    /** Parsea habiendo validado que el mensaje cumple el patrón requerido */
    protected abstract T doUnMarshall(String msg, Matcher matcher);

    /** Serializa el mensaje */
    protected abstract String doMarshall(T object);
}
