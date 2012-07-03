package controllers;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;

import play.mvc.Controller;
import play.mvc.With;
import services.LDAPService;

@With(Secure.class)
public class Application extends Controller {

    @Inject static LDAPService ldap;
    
    public static void index() {
        String username = Security.connected();
        List<String> groups = ldap.listGroups(username);
        render(groups);
    }

}