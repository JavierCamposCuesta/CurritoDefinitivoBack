package com.example.demo.error;

public class CategoriaSinDatosException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CategoriaSinDatosException() {
		super("Lista de categorías vacía");
	}

}
