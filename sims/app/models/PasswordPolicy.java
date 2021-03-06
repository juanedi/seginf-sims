package models;

import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import play.libs.Crypto;
import play.libs.Crypto.HashType;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.time.DateUtils;

/**
 * Usuario del administrador de identidades.
 * 
 * 
 * @author Ruben Festini
 * @since Jul 19, 2012
 */

@Entity
@Table(	name = "password_policies", 
		uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class PasswordPolicy extends Model {

	private static final String _SPECIAL_CHARS = "@#$%";
	public static final char[] SPECIAL_CHARS = _SPECIAL_CHARS.toCharArray();
	
	/**
	 * Se guarda este para saber cuál es la política actual.
	 * No se usa un boolean para evitar problemas de concurrencia al modificar
	 * (via JPA la única forma de setear sería desactivar todos primero y activar uno después por separado).
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "active", nullable = true)
	public Date lastActivationDate;
	
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
    
    /** Cantidad de Días*/
    @Column(name = "duration", nullable = false)
    public int duration;

    /** Cantidad de passwords pasadas que deben ser distintas */
    @Column(name = "last_different", nullable = false)
    public int differentToLast;
    
    public PasswordPolicy (String name,
    	   int passwordLength,
           boolean useLowerCaseLetters,
           boolean useUpperCaseLetters,
           boolean useSpecialCharsLetters,
           boolean useNumbers,
           int duration,
           int differentToLast) {

        this.name = name;
        this.passwordLength = passwordLength;
        this.useLowerCaseLetters = useLowerCaseLetters;
        this.useUpperCaseLetters = useUpperCaseLetters;
        this.useSpecialCharsLetters = useSpecialCharsLetters;
        this.useNumbers = useNumbers;
        this.duration = duration;
        this.differentToLast = differentToLast;
    }
    
    public boolean checkFormat(String password) {
    	if (password == null) return false;
    	
    	String regex = "";
    	
    	if (useLowerCaseLetters){
    		regex = regex + "(?=.*[a-z])";
    	}
    	
    	if (useUpperCaseLetters){
    		regex = regex + "(?=.*[A-Z])";
    	}
    	
    	if (useNumbers){
    		regex = regex + "(?=.*\\d)";
    	}

    	if (useSpecialCharsLetters){
    		regex = regex + "(?=.*[" + _SPECIAL_CHARS + "])";
    	}
    	regex = "(" + regex + ".{" + passwordLength + ",})"; 
    	 	
    	
    	return (password.matches(regex));
    }

    public void activate() {
    	this.lastActivationDate = new Date();
    }
    
    /** Valida que la una nueva clave sea válida con respecto a la política */
    public boolean validate(User user, String newPassword) {
    	// chequeo formato
    	boolean ret = checkFormat(newPassword);
    	
    	// chequeo que no coincida con las últimas N
    	String passwordHash = Password.hash(newPassword);
    	List<Password> lastPasswords = getLastPasswords(user, differentToLast);
    	
		for (Password p : lastPasswords){
			if (p.passwordHash.equals(passwordHash)){
    			ret = false;
    		}
        }
    	
        return ret;
    }
    
    public static PasswordPolicy current() {
    	return PasswordPolicy.find(
    			"select p1 from PasswordPolicy p1 where p1.lastActivationDate = " 
				+ "(select max(p2.lastActivationDate) from PasswordPolicy p2)").first();
    }
    
    /** lista de usuarios con clave expirada */
    public static List<User> usersWithPasswordsExpiringToday() {
		return usersWithPasswordsExpiringIn(0);	
    }
    
    /** lista de usuarios con clave a N días de vencer */
    public static List<User> usersWithPasswordsExpiringIn(int daysUntilExpiration) {
    	return User.find("lastPasswordChanged = ?1", limitDate(current(), daysUntilExpiration)).fetch();
    }
    
    /** lista de usuarios con clave a N o menos días de vencer */
    public static List<User> usersWithPasswordsNearExpiration(int daysUntilExpiration) {
    	return User.find("lastPasswordChanged < ?1", limitDate(current(), daysUntilExpiration)).fetch();
    }
    
    /** retorna la fecha de expiración para nuevas claves. es decir, hoy + tiempo_expiracion */
    public Date expirationDateForNewPassword() {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	cal.add(Calendar.DAY_OF_YEAR, duration);
    	return cal.getTime();
    }
    
	/** retorna la fecha de actualización que tendría que tener una clave para vencer dentro de N días.
	 * si se pasa un "0", retorna las que tienen fecha de actualización para vencer hoy. */
	private static Date limitDate(PasswordPolicy policy, int daysUntilExpiration) {
		Calendar limitDate = Calendar.getInstance();
		limitDate.add(Calendar.DATE, (-1) * policy.duration);
		limitDate.add(Calendar.DAY_OF_YEAR, daysUntilExpiration);
		return DateUtils.truncate(limitDate, Calendar.DATE).getTime();
	}
	
	private static List<Password> getLastPasswords(User user, int amount) {
    	Query query = JPA.em().createQuery("select p from User u join u.oldPasswords p where u = ?1 order by p.dateModified DESC"
    			,Password.class);
    	query.setFirstResult(0);
    	query.setMaxResults(amount);
    	return query.setParameter(1, user).getResultList();
	}
}
