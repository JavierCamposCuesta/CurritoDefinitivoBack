package com.example.demo.error;

public class UpdateProfileError extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public UpdateProfileError(String email) {
		super("El perfil con email: " + email + " no ha sido posible actualizar");
	}

}
