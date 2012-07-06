package notifiers;

import models.User;

/**
 * 
 * Notificaciones por mail.  
 * 
 * 
 * @author Juan Edi
 * @since Jul 6, 2012
 */
public class Mailer extends play.mvc.Mailer {

    private static final String FROM = "Administrador de identidades <seginfadmistrador@gmail.com>";
    
    public static void welcome(final User user, final String generatedPassword) {
        setSubject("Sus datos para ingresar al administrador de identidades");
        addRecipient(user.email);
        setFrom(FROM);
        send(user, generatedPassword);
    }
    
}
