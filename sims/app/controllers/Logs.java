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
@With(Secure.class)
public class Logs extends Controller {

    /** Sirve pantalla principal de consulta de logs */
    public static void logs() {
        render();
    }
    
}
