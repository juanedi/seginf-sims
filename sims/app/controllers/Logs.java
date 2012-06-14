package controllers;

import play.mvc.Controller;
import play.mvc.With;

/**
 * Controller para pantalla de logs.
 * 
 * 
 * @author Juan Edi
 * @since May 27, 2012
 */
public class Logs extends SecureController {

    /** Sirve pantalla principal de consulta de logs */
    public static void logs() {
        render();
    }
    
}
