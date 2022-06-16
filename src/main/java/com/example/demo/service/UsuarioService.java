package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.error.UpdateProfileError;
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
	@Autowired
	PasswordEncoder passwordEncoder;
	
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

	/**
	 * Metodo para subir un nuevo comentario, este método recibe el id del comentario que hay que actualizar y los datos necesarios
	 * @param idComentario
	 * @param textoComentario
	 * @param puntuacionEstrellas
	 * @return Comentario modificado
	 */
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
	
	/**
	 * Metodo para editar un perfil, recibimos todos los datos modificables de usuario
	 * @param imagenProfile
	 * @param nombre
	 * @param apellidos
	 * @param telefono
	 * @param ubicacion
	 * @param email
	 * @return perfil editado
	 */
	public Usuario editProfile(MultipartFile imagenProfile, String nombre, String apellidos, String telefono,
			String ubicacion, String email) {
		
			Usuario userEdit = usuarioRepository.findByEmail(email).orElse(null);
			userEdit.setNombre(nombre);
			userEdit.setApellidos(apellidos);
			userEdit.setTelefono(telefono);
			userEdit.setUbicacion(ubicacion);
		
		try {
			userEdit.setFotoPerfil(imagenProfile.getBytes());
		} catch (IOException e) {
			throw new UpdateProfileError(email);
		}
		
		usuarioRepository.save(userEdit);
			return userEdit;
	}
	
	/**
	 * Metodo para editar perfil en caso de no recibir una imagen 
	 * @param nombre
	 * @param apellidos
	 * @param telefono
	 * @param ubicacion
	 * @param email
	 * @return perfil editado
	 */
	public Usuario editProfileNoImg(String nombre, String apellidos, String telefono,
			String ubicacion, String email) {
		
			Usuario userEdit = usuarioRepository.findByEmail(email).orElse(null);
			userEdit.setNombre(nombre);
			userEdit.setApellidos(apellidos);
			userEdit.setTelefono(telefono);
			userEdit.setUbicacion(ubicacion);
		
		
			usuarioRepository.save(userEdit);
			return userEdit;
	}
	
	/**
	 * Metodo para cambiar la contraseña, para realizar esta operación codificamos la pass antes de guardarla en bd
	 * @param passwordNew
	 * @param passwordNew2
	 * @param email
	 * @return usuario
	 */
	public Usuario editPass(String passwordNew, String passwordNew2, String email) {
		Usuario userEdit = usuarioRepository.findByEmail(email).orElse(null);
		String encodedPass = passwordEncoder.encode(passwordNew);
		
		userEdit.setPassword(encodedPass);
		usuarioRepository.save(userEdit);
		return userEdit;
	}

	/**
	 * metodo para mostrar un anuncio por id
	 * @param idProfile
	 * @return
	 */
	public Usuario mostrarUsuario(int idProfile) {
		
		return usuarioRepository.findById(idProfile).orElse(null);
	}

	/**
	 * metodo para mostrar los anuncios ofertados de un usuario a través del id de usuario
	 * @param idProfile
	 * @return
	 */
	public List<Anuncio> mostrarAnunciosOfertadosUsuario(int idProfile) {
		return usuarioRepository.findById(idProfile).orElse(null).getListaOfertados();
	}
	
	/**
	 * Lista para mostrar las opiniones de un usuario
	 * @param email
	 * @return lista de opiniones
	 */
	public List<Comentario> mostrarOpinionesUsuario(int idProfile) {
		return comentarioRepository.getOpiniones(idProfile);

	}

}
