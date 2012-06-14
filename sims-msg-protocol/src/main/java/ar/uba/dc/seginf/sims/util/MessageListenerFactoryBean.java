package ar.uba.dc.seginf.sims.util;

import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import ar.uba.dc.seginf.sims.marshallers.MessageMarshaller;

/**
 * Para facilitar la instanciación de MessageListenerAdapters.
 * 
 * 
 * @author Juan Edi
 * @since May 29, 2012
 */
public class MessageListenerFactoryBean extends AbstractFactoryBean<MessageListenerAdapter> {

    private MessageListenerAdapter instance;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public MessageListenerFactoryBean(final MessageProcessor<?> processor, 
                                      final MessageMarshaller<?> marshaller) {
        instance = new MessageListenerAdapter(processor, new SimsMessageConverter(marshaller));
        instance.setDefaultListenerMethod("process");
    }
    
    /** @see org.springframework.beans.factory.config.AbstractFactoryBean#createInstance() */
    @Override
    protected MessageListenerAdapter createInstance() throws Exception {
        return instance;
    }

    /** @see org.springframework.beans.factory.config.AbstractFactoryBean#getObjectType() */
    @Override
    public Class<?> getObjectType() {
        return MessageListenerAdapter.class;
    }

}
