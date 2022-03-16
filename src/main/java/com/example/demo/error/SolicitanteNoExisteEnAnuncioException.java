package com.example.demo.error;

public class SolicitanteNoExisteEnAnuncioException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public SolicitanteNoExisteEnAnuncioException(int idAnuncio, int idSolicitante) {
		super("El usuario con id: " +idSolicitante + " no existe en el anuncio con id: " + idAnuncio);
	}
}
