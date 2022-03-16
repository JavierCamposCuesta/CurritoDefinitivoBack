package com.example.demo.error;

public class AnuncioIdNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public AnuncioIdNotFoundException(int idAnuncio) {
		super("No existe el anuncio con id: "+ idAnuncio);
	}
}
