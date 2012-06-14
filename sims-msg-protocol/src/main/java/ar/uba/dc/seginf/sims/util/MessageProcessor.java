package ar.uba.dc.seginf.sims.util;

import ar.uba.dc.seginf.sims.messages.Message;

/**
 * Encargado de procesar mensajes que llegan.
 * 
 * 
 * @author Juan Edi
 * @since May 29, 2012
 */
public interface MessageProcessor<T extends Message> {

    void process(T msg);
    
}
