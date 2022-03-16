package com.example.demo.error;

public class UsuarioIdNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public UsuarioIdNotFoundException(int idUsuario) {
		super("El usuario con id: " +idUsuario+ " no existe");
	}
}
