package models;

/**
 * Tipos de evento de auditoría
 * 
 * @author Juan Edi
 *
 */
public enum EventType {

	PASSWORD_CHANGE("Cambio de clave"),
	
	USER_CREATED("Creación de un usuario"),
	
	APP_CREATED("Creación de una aplicación"),
	
	APP_ACCESS_CHANGED("Asignación o revocación de acceso de un usuario a una aplicación"),
	
	ROLE_CHANGED("Asignación o revocación de un rol a un usuario"),
	
	PASSWORD_POLICY_CREATED("Nueva política de complejidad de claves"),
	
	PASSWORD_POLICY_CHANGE("Cambio de la política de complejidad de claves");
	
	private final String description;
	
	private EventType(String description) {
		this.description = description;
	}
	
	/** Descripción del tipo de evento */
	public String getDescription() {
		return description;
	}
	
}
