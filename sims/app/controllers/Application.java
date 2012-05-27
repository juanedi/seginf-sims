package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

@With(Secure.class)
public class Application extends Controller {

    /** sirve página principal */
    public static void index() {
        render();
    }

    /** sirve pantalla de alta de aplicaciones */
    public static void create() {
        render();
    }
    
}