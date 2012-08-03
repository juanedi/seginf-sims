package controllers;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import notifiers.Mailer;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import play.data.validation.Required;

import services.AccountingLogger;
import services.AppNotificationService;
import models.App;
import models.PasswordPolicy;
import models.Role;
import models.User;

/**
 * Controller para creación de políticas de claves.
 * 
 * 
 * @author Ruben Festini
 * @since Jul 19, 2012
 */

public class PasswordPolicies extends SecureController {
	
    @Inject static AppNotificationService appNotificationService;
    @Inject static AccountingLogger accountingLogger;

    /** sirve pantalla de cambio de password */
    public static void list() {
        List<PasswordPolicy> policies = PasswordPolicy.all().fetch();
        PasswordPolicy currentPolicy = PasswordPolicy.getCurrent();
        render(policies, currentPolicy);
    }
    
    public static void create() {
        render();
    }    
    
    public static void setActive(@Required final String policyName) {
    	try {
    		if (StringUtils.isEmpty(policyName)) {
    			throw new IllegalArgumentException("No se especificó política");
    		}
    		
    		PasswordPolicy policy = PasswordPolicy.find("byName", policyName).first();
    		if (policy == null) {
    			throw new IllegalArgumentException("No se encontró la política " + policyName);
    		}
    		
    		policy.activate();
    		policy.save();
    		
    		accountingLogger.logPasswordPolicyActivated(connectedUser(), policy);
    		
    		flash.success("Política de claves actualizada");
    		list();
    		
    	} catch (IllegalArgumentException e) {
        	flash.error(e.getMessage());
        	create();
    	}
    }
    
    @Check(Role.SIMS_PASSWORD_POLICY_ROLE)
    public static void postPasswordPolicy(@Required final String name,
           @Required final Integer passwordLength,
           @Required final boolean useLowerCaseLetters,
           @Required final boolean useUpperCaseLetters,
           @Required final boolean useSpecialCharsLetters,
           @Required final boolean useNumbers,
           @Required final Integer duration,
           @Required final Integer differentToLast) {

        try {
        	
        	if(validation.hasErrors()) {
        		Set<String> missingFields = validation.errorsMap().keySet();
        		throw new IllegalArgumentException(String.format("Falta completar los campos: %s", missingFields));
        	}
        	
        	if (PasswordPolicy.find("byName", name).first() != null) {
        		throw new IllegalArgumentException("Ya existe una política con ese nombre");
        	}
        	
        	if (passwordLength <= 0) {
        		throw new IllegalArgumentException("La longitud de clave debe ser positiva");
        	}
        	
        	if (duration <= 0) {
        		throw new IllegalArgumentException("La duración de clave debe ser positiva");
        	}
        	
        	if (differentToLast <= 0) {
        		throw new IllegalArgumentException("La cantidad de claves pasadas a comparar debe ser positiva");
        	}
        	
        	PasswordPolicy passPolicy = new PasswordPolicy(name,
        			passwordLength,
        			useLowerCaseLetters,
        			useUpperCaseLetters,
        			useSpecialCharsLetters,
        			useNumbers,
        			duration,
        			differentToLast);
        	
        	passPolicy.save();
        	
        	accountingLogger.logPasswordPolicyCreated(connectedUser(), passPolicy);
        	
            flash.success("Política de claves creada exitosamente.");
            PasswordPolicies.list();
        	
        } catch (IllegalArgumentException e) {
        	flash.error(e.getMessage());
        	create();
        }
        
    }

}
