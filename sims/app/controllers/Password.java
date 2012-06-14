package controllers;

import play.mvc.Controller;
import play.mvc.With;

/**
 * Controller de pantalla de cambio de passwords.
 * 
 * 
 * @author Juan Edi
 * @since May 27, 2012
 */
public class Password extends SecureController {

    /** sirve pantalla de cambio de password */
    public static void info() {
        render();
    }
    
}
