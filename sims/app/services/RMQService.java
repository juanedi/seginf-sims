/*
 * Copyright (c) 2012 Zauber S.A.  -- All rights reserved
 */
package services;

/**
 * Servicio para interactuar con RabbitMQ
 * 
 * 
 * @author Juan Edi
 * @since May 27, 2012
 */
public interface RMQService {

    void createQueue(String appName);
    
    void createUser(String appName);

    void changeUserPassword(String appName, String password);
    
}
