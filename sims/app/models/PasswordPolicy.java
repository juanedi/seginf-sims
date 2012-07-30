package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.Validate;

/**
 * Usuario del administrador de identidades.
 * 
 * 
 * @author Ruben Festini
 * @since Jul 19, 2012
 */

@Entity

@Table( name = "password_policies")


public class PasswordPolicy extends Model {

    @Column(name = "name", nullable = false)
    public String name;

    @Column(name = "password_length", nullable = false)
    public int passwordLength;
	
    @Column(name = "use_lower_case_letters", nullable = false)
    public boolean useLowerCaseLetters;

    @Column(name = "use_upper_case_letters", nullable = false)
    public boolean useUpperCaseLetters;

    @Column(name = "use_special_chars_letters", nullable = false)
    public boolean useSpecialCharsLetters;

    @Column(name = "use_numbers", nullable = false)
    public boolean useNumbers;
    
    @Column(name = "req_every_type", nullable = false)
    public boolean reqEveryType;
    
    /** Cantidad de Días*/
    @Column(name = "duration", nullable = false)
    public int duration;

    public PasswordPolicy (String name,
    	   int passwordLength,
           boolean useLowerCaseLetters,
           boolean useUpperCaseLetters,
           boolean useSpecialCharsLetters,
           boolean useNumbers,
           boolean reqEveryType,
           int duration) {

        this.name = name;
        this.passwordLength = passwordLength;
        this.useLowerCaseLetters = useLowerCaseLetters;
        this.useUpperCaseLetters = useUpperCaseLetters;
        this.useSpecialCharsLetters = useSpecialCharsLetters;
        this.useNumbers = useNumbers;
        this.reqEveryType = reqEveryType;
        this.duration = duration;
    }
}