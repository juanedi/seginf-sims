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
@With(Secure.class)
public class Users extends Controller {

    public static void create() {
        render();
    }
}
