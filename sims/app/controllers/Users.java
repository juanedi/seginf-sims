package controllers;

import play.mvc.Controller;
import play.mvc.With;

/**
 * Controller para alta de usuarios.
 * 
 * 
 * @author Juan Edi
 * @since May 27, 2012
 */
public class Users extends SecureController {

    public static void create() {
        render();
    }
}
