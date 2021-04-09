package com.example.aplicacion.Exception;

public class CustomeFieldValidationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4097730446105698119L;

	private String fieldName;
	
	public CustomeFieldValidationException(String message, String fieldName) {
		
		super(message);
		this.fieldName = fieldName;
		
	}
	
	public String getFieldName() {
		return this.fieldName;
	}
}
