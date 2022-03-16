package com.example.demo.error;

public class AnuncioIdNoEstaEnListaExcetion extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public AnuncioIdNoEstaEnListaExcetion(int idAnuncio) {
		super("El anuncio con id: " + idAnuncio + " no está en esta lista");
	}
}
