package com.example.demo.error;

public class AnuncioYaExistenteException extends RuntimeException{

	
	private static final long serialVersionUID = 1L;

	public AnuncioYaExistenteException(int idAnuncio) {
		super("El anuncio con id: " + idAnuncio + " ya esta añadido en la lista");
	}
}
