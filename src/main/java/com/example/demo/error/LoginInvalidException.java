package com.example.demo.error;

public class LoginInvalidException extends RuntimeException{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoginInvalidException() {
		super("Credenciales incorrectas");
	}
}
