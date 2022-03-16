package com.example.demo.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Anuncio;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;

/**
 * Servicio que recogerá los métodos que realicen funciones propias de un usuario
 * @author javier
 *
 */
@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/**
	 * Metodo para mostrar un usuario a traves de su email
	 * @param email
	 * @return usuario
	 */
	public Usuario usuarioPorEmail(String email) {
		 return usuarioRepository.findByEmail(email).orElse(null);
	}

	/**
	 * Metodo para mostras los anuncios ofertados
	 * @param email
	 * @return lista anuncios
	 */
	public List<Anuncio> mostrarAnuncios(String email) {
		Usuario nuevoUsuario = usuarioRepository.findByEmail(email).orElse(null);
		List<Anuncio>listaAnuncios = nuevoUsuario.getListaOfertados();
		return listaAnuncios;
	}
	
	/**
	 * Lista para mostrar los anuncios terminados
	 * @param email
	 * @return lista de anuncios
	 */
	public List<Anuncio> mostrarAnunciosTerminados(String email) {
		Usuario nuevoUsuario = usuarioRepository.findByEmail(email).orElse(null);
		List<Anuncio>listaAnunciosTerminados = nuevoUsuario.getListaTerminados();
		return listaAnunciosTerminados;
	}
	
	/**
	 * Metodo para mostrar los anuncios solicitados
	 * @param email
	 * @return lista de anuncios
	 */
	public List<Anuncio> mostrarAnunciosSolicitados(String email) {
		Usuario nuevoUsuario = usuarioRepository.findByEmail(email).orElse(null);
		List<Anuncio>listaAnunciosSolicitados = nuevoUsuario.getListaDemandados();
		return listaAnunciosSolicitados;
	}
	
	/**
	 * Metodo para mostrar los anuncios realizados
	 * @param email
	 * @return lista anuncios
	 */
	public List<Anuncio> mostrarAnunciosRealizados(String email) {
		Usuario nuevoUsuario = usuarioRepository.findByEmail(email).orElse(null);
		List<Anuncio>listaAnunciosRealizados = nuevoUsuario.getListaRealizados();
		return listaAnunciosRealizados;
	}

}
