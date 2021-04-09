package com.example.aplicacion.Exception;

public class UsernameOrIdNotFound extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5233887560664382570L;

	public UsernameOrIdNotFound() {
		super("Usuario o Id no encontrado");
	}
	
	public UsernameOrIdNotFound(String message) {
		super(message);
		
	}
}
