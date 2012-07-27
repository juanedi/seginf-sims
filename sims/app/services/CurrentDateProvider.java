package services;

import java.util.Date;

/**
 * {@link DateProvider} que devuelve la fecha del momento de llamado.
 * 
 * @author Juan Edi
 *
 */
public class CurrentDateProvider implements DateProvider {

	@Override
	public Date getDate() {
		return new Date();
	}

}
