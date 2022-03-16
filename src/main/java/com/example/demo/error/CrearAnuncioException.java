package com.example.demo.error;

public class CrearAnuncioException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public CrearAnuncioException() {
		super("Error al crear o modificar el anuncio, campos inválidos");
	}
}
