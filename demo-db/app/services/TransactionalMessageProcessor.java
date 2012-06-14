package services;

import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import ar.uba.dc.seginf.sims.messages.Message;
import ar.uba.dc.seginf.sims.util.MessageProcessor;

/**
 * Abre una transacción play y procesa un mensaje.
 * 
 * 
 * @author Juan Edi
 * @since May 29, 2012
 */
public abstract class TransactionalMessageProcessor<T extends Message> implements MessageProcessor<T> {

    /** Abre la transacción y deja lógica de procesamiento a la implementación final. */
    public final void process(T msg) {
        JPAPlugin.startTx(false);
        boolean rollBack = false;
        try {
            doProcess(msg);
            JPA.em().flush();
        } catch (RuntimeException e) {
            rollBack = true;
            throw e;
        } finally {
            JPAPlugin.closeTx(rollBack);
        }
    }
    
    protected abstract void doProcess(T msg);
    
}
