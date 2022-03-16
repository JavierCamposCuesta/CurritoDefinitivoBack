package com.example.demo.error;

public class EmailEnUsoException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public EmailEnUsoException() {
		super("No se puede crear usuario, email ya en uso");
	}
}
