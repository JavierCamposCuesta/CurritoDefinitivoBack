package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Anuncio;
import com.example.demo.model.Comentario;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ComentarioRepository;
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
	@Autowired
	private ComentarioRepository comentarioRepository;
	
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
	
	/**
	 * Lista para mostrar las opiniones de un usuario
	 * @param email
	 * @return lista de opiniones
	 */
	public List<Comentario> mostrarOpiniones(String email) {
		Usuario nuevoUsuario = usuarioRepository.findByEmail(email).orElse(null);
		List<Comentario>listaOpiniones = comentarioRepository.getOpiniones(nuevoUsuario.getId());
		return listaOpiniones;
	}
	
	/**
	 * Metodo para mostrar las notificicaiones de un usuario
	 * @param email
	 * @return lista de notificaciones
	 */
	public List<Comentario> mostrarNotificaciones(String email) {
		Usuario nuevoUsuario = usuarioRepository.findByEmail(email).orElse(null);
		List<Comentario>listaNotificaciones = comentarioRepository.getNotificaciones(nuevoUsuario.getId());
		return listaNotificaciones;
	}
	
	/**
	 * Metodo para mostrar las notificicaiones de un usuario
	 * @param email
	 * @return lista de notificaciones
	 */
	public List<Comentario> mostrarOpinionesPendientes(String email) {
		Usuario nuevoUsuario = usuarioRepository.findByEmail(email).orElse(null);
		List<Comentario>listaNotificaciones = comentarioRepository.getOpinionesPendientes(nuevoUsuario.getId());
		return listaNotificaciones;
	}

	public Comentario subirComentario(String idComentario, String textoComentario, String puntuacionEstrellas) {
		int puntuacionEstrellasConvert = Integer.parseInt(puntuacionEstrellas);
		int idComentarioConvert = Integer.parseInt(idComentario);
		
		Comentario nuevoComentario = comentarioRepository.getById(idComentarioConvert);
		nuevoComentario.setTextoComentario(textoComentario);
		nuevoComentario.setPuntuacionEstrellas(puntuacionEstrellasConvert);
		nuevoComentario.setFecha(LocalDate.now());
		nuevoComentario.setRealizado(true);
		
		Usuario usuarioOfertante =  usuarioRepository.findByEmail(nuevoComentario.getUsuarioComentado().getEmail()).orElse(null);
		usuarioOfertante.setPuntuacionMedia(puntuacionEstrellasConvert);
		
		usuarioRepository.save(usuarioOfertante);
		comentarioRepository.save(nuevoComentario);
		return nuevoComentario;
	}

}
