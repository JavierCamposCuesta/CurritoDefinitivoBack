package com.example.demo.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Usuario;

/**
 * clase repositorio usuario para conectar a usuario con la base de datos
 * @author javier
 *
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	public Optional<Usuario> findByEmail(String email);
}
