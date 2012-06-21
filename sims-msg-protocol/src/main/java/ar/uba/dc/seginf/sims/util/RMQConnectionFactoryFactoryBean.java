package ar.uba.dc.seginf.sims.util;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Factory bean para una {@link ConnectionFactory} con soporte para SSL.
 * 
 * 
 * @author Juan Edi
 * @since Jun 21, 2012
 */
public class RMQConnectionFactoryFactoryBean extends AbstractFactoryBean<ConnectionFactory> {

    private String host;
    private Integer port;
    private String vhost;
    private String username;
    private String password;
    private Boolean useSSL;
    
    private String trustStorePassphrase;
    private String trustStoreLocation;
    
    /** @see AbstractFactoryBean#getObjectType() */
    @Override
    public Class<ConnectionFactory> getObjectType() {
        return ConnectionFactory.class;
    }

    /** @see AbstractFactoryBean#createInstance() */
    @Override
    protected ConnectionFactory createInstance() throws Exception {
        com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory = 
                            new com.rabbitmq.client.ConnectionFactory();
        
        // Si no setearon los valores se configuran los default de RMQ.
        if (host != null) {
            rabbitConnectionFactory.setHost(host);
        }
        if (port != null) {
            rabbitConnectionFactory.setPort(port);
        }
        if (vhost != null) {
            rabbitConnectionFactory.setVirtualHost(vhost);
        }
        if (username != null) {
            rabbitConnectionFactory.setUsername(username);
        }
        if (password != null) {
            rabbitConnectionFactory.setPassword(password);
        }
        
        if (useSSL) {
            
            // Descomentar y pasar como argumento de inicialización al SSLContext 
            // para presentar certificado de cliente.
            
            //String keyPassPhrase = "simskeystore";
            //char[] keyPassphrase = keyPassPhrase.toCharArray();
            //KeyStore ks = KeyStore.getInstance("PKCS12");
            //ks.load(new FileInputStream("/path/to/client/keycert.p12"), keyPassphrase);
            
            //KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            //kmf.init(ks, keyPassphrase);
            
            if (trustStorePassphrase == null) {
                throw new IllegalStateException("No se configuró clave para el trustStore");
            }
            
            if (trustStoreLocation == null) {
                throw new IllegalStateException("Se intentó quiere usar SSL pero no se configuró un trustStore válido");
            }
            
            char[] trustPassphrase = trustStorePassphrase.toCharArray();
            KeyStore tks = KeyStore.getInstance("JKS");
            tks.load(new FileInputStream(trustStoreLocation), trustPassphrase);
            
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(tks);
            
            SSLContext c = SSLContext.getInstance("SSLv3");
            c.init(null, tmf.getTrustManagers(), null);
            
            rabbitConnectionFactory.useSslProtocol(c);
        }
        
        
        return new CachingConnectionFactory(rabbitConnectionFactory);
    }
    
    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUseSSL(Boolean useSSL) {
        this.useSSL = useSSL;
    }

    public void setTrustStorePassphrase(String trustStorePassphrase) {
        this.trustStorePassphrase = trustStorePassphrase;
    }

    public void setTrustStoreLocation(String trustStoreLocation) {
        this.trustStoreLocation = trustStoreLocation;
    }
    
}
