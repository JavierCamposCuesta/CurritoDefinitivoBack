package com.example.demo.error;

public class ParametrosInvalidosException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ParametrosInvalidosException() {
		super("Faltan parametros para realizar la busqueda");
	}
}
