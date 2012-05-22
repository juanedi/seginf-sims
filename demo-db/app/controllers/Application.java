package controllers;

import play.*;
import play.db.jpa.GenericModel.JPAQuery;
import play.mvc.*;

import java.util.*;

import models.*;

@With(Secure.class)
public class Application extends Controller {

    public static void index() {
        if (Security.isConnected()) {
            User connectedUser = User.find("byUsername", Security.connected()).first();
            renderArgs.put("user", connectedUser);
        }
        render();
    }

}