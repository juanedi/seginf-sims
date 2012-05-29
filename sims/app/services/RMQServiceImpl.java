/*
 * Copyright (c) 2012 Zauber S.A.  -- All rights reserved
 */
package services;

import org.apache.commons.lang.Validate;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;

/**
 * Implementación de {@link RMQService}.
 * 
 * 
 * @author Juan Edi
 * @since May 27, 2012
 */
public class RMQServiceImpl implements RMQService {

    private final String baseURL;
    private final String vhost;
    private final String username;
    private final String password;
    
    private AmqpAdmin admin;

    /** Creates the RMQServiceImpl. */
    public RMQServiceImpl(final String host, final String port, final String vhost, 
                          final String username, final String password,
                          final AmqpAdmin admin) {
        Validate.notEmpty(host);
        Validate.notEmpty(port);
        Validate.notEmpty(vhost);
        Validate.notEmpty(username);
        Validate.notEmpty(password);
        Validate.notNull(admin);
        this.baseURL = "http://" + host + ":" + port;
        this.vhost = vhost;
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    /** @see services.RMQService#setUser(java.lang.String) */
    @Override
    public void setUser(String appName) {
        // crea la cola en RMQ
        admin.declareQueue(new Queue(appName));
        
        // crea usuario con password default
        putUserJSON(appName, appName);
        
        // configura los permisos del usuario 
        // para que lo único que pueda hacer sea
        // leer de su cola de mensajes.
        putUserPermissionsJSON(appName);
    }
    
    /** @see services.RMQService#changeUserPassword(java.lang.String, java.lang.String) */
    @Override
    public void changeUserPassword(String appName, String appPassword) {
        putUserJSON(appName, appPassword);
    }

    /** realiza un post al api de management de RMQ */
    private void putUserJSON(final String appName, final String appPassword) {
        WSRequest request = createRequest(getUserURL(appName), userPayload(appPassword));
        doPUT(request);
    }
    
    /** crea los permisos para que el usuario pueda solamente leer le la cola que le corresponde */
    private void putUserPermissionsJSON(final String appName) {
        WSRequest request = createRequest(getUserPermissionsURL(appName, vhost), userPermissionsPayload(appName));
        doPUT(request);
    }
    

    private WSRequest createRequest(final String url, final String payload) {
        WSRequest request = WS.url(url);
        request.authenticate(username, password);
        request.setHeader("Content-Type", "application/json");
        request.body = payload;
        return request;
    }
    
    
    private void doPUT(WSRequest request) {
        HttpResponse response = request.put();
        if (response.getStatus() != 204) {
            throw new IllegalStateException("falló la conexión con api de RMQ");
        }
    }
    
    private String getUserURL(final String appName) {
        return baseURL + "/api/users/" + appName;
    }
    
    private String getUserPermissionsURL(final String appName, final String vhost) {
        return baseURL + "/api/permissions/" + vhost + "/" + appName;
    }
    
    private String userPayload(final String password) {
        return "{\"password\":\"" + password + "\",\"tags\":\"monitoring\"}";
    }
    
    private String userPermissionsPayload(String appName) {
        return "{\"configure\":\"\",\"write\":\"\",\"read\":\"^" + appName + "$\"}";
    }
}
