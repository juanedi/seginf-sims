package ar.uba.dc.seginf.sims.marshallers;

import ar.uba.dc.seginf.sims.messages.Message;

/**
 * Parsea y serialza mensajes.
 * 
 * 
 * @author Juan Edi
 * @since May 28, 2012
 */
public interface MessageMarshaller<T extends Message> {

    /** serializa un mensaje */
    String marshall(T object);
    
    /** parsea un mensaje */
    T unMarshall(String msg);
}
