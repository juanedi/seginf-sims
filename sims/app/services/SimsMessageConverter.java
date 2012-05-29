package services;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

import ar.uba.dc.seginf.sims.marshallers.MessageMarshaller;
import ar.uba.dc.seginf.sims.marshallers.NewUserMessageMarshaller;
import ar.uba.dc.seginf.sims.messages.Message;
import ar.uba.dc.seginf.sims.messages.NewUserMessage;

/**
 * Converter genérico para el administrador.
 * Recibe un {@link MessageMarshaller} que tiene la lógica de parseo/serialización.
 * 
 * 
 * @author Juan Edi
 * @since May 29, 2012
 */
public class SimsMessageConverter<T extends Message> implements MessageConverter {

    private final MessageConverter stringConverter = new SimpleMessageConverter();
    private final MessageMarshaller<T> marshaller;

    /** Creates the SimsMessageConverter. */
    public SimsMessageConverter(final MessageMarshaller<T> marshaller) {
        this.marshaller = marshaller;
    }
    
    /** @see MessageConverter#fromMessage(Message) */
    @Override
    public Object fromMessage(org.springframework.amqp.core.Message arg0)
            throws MessageConversionException {
        return marshaller.unMarshall((String) stringConverter.fromMessage(arg0));
    }

    /** @see MessageConverter#toMessage(Object, MessageProperties) */
    @Override
    public org.springframework.amqp.core.Message toMessage(Object arg0,
            MessageProperties arg1) throws MessageConversionException {
        return stringConverter.toMessage(marshaller.marshall((T) arg0), arg1);
    }

}
