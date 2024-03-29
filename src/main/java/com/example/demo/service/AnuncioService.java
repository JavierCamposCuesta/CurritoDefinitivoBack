package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.error.AnuncioIdNoEstaEnListaExcetion;
import com.example.demo.error.AnuncioIdNotFoundException;
import com.example.demo.error.AnuncioYaExistenteException;
import com.example.demo.error.CrearAnuncioException;
import com.example.demo.model.Anuncio;
import com.example.demo.model.Categoria;
import com.example.demo.model.Comentario;
import com.example.demo.model.Usuario;
import com.example.demo.repository.AnuncioRepository;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ComentarioRepository;
import com.example.demo.repository.UsuarioRepository;

/**
 * Servicio de anuncio, encargado de hacer todas las operaciones relacionadas con los anuncios
 * @author javier
 *
 */
@Service
public class AnuncioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private AnuncioRepository anuncioRepository;
	
	@Autowired
	private ComentarioRepository comentarioRepository;
	
	public void crearUsuario(Usuario usuario) {
		usuarioRepository.save(usuario);
	}

	/**
	 * Metodo para añadir un anuncio
	 * @param email
	 * @param anuncio
	 * @return el anuncio que se ha añadido
	 */
//	public Anuncio addAnuncio(String email, Anuncio anuncio) {
//		if(anuncio.getTitulo().isBlank() || anuncio.getTitulo() == null || anuncio.getPrecio() < 0 ) {
//			throw new CrearAnuncioException();
//		}
//		else {
//		Anuncio nuevoAnuncio = new Anuncio(anuncio.getTitulo(), anuncio.getCategoria(), anuncio.getPrecio(), anuncio.getTipoPrecio(), anuncio.getDescripcion());
//		nuevoAnuncio.setFechaAnuncio(LocalDateTime.now());
//		nuevoAnuncio.setUbicacion(anuncio.getUbicacion());
//		
//		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
//		Categoria categoria = categoriaRepository.findById(anuncio.getCategoria()).orElse(null);
//		categoria.getListaAnuncios().add(nuevoAnuncio);
//		usuario.getListaOfertados().add(nuevoAnuncio);
//		nuevoAnuncio.setAutorAnuncio(usuario);
//		anuncioRepository.save(nuevoAnuncio);
//		categoriaRepository.save(categoria);
//		usuarioRepository.save(usuario);
//		return nuevoAnuncio;
//		}
//		
//	}
	
	/**
	 * Metodo para añadir un anuncio pasandole una foto
	 * @param email
	 * @param titulo
	 * @param categoria
	 * @param precio
	 * @param tipoPrecio
	 * @param descripcion
	 * @param ubicacion
	 * @param file
	 * @return
	 */
	public Anuncio addAnuncioCompleto(String email, String titulo, String categoria, String precio, String tipoPrecio,
			String descripcion, String ubicacion, MultipartFile file) {
		Double convertPrecio = Double.parseDouble(precio);
		if(titulo.isBlank() || titulo == null || convertPrecio < 0 ) {
			throw new CrearAnuncioException();
		}
		else {
		Anuncio nuevoAnuncio = new Anuncio(titulo, categoria, convertPrecio, tipoPrecio, descripcion, ubicacion);
		nuevoAnuncio.setFechaAnuncio(LocalDateTime.now());
		nuevoAnuncio.setUbicacion(ubicacion);
		
		
		
		try {
			if(file != null) {
				nuevoAnuncio.setFile(file.getBytes());
			}
			else {
				nuevoAnuncio.setFile(null);
			}
		} catch (IOException e) {
			throw new CrearAnuncioException();
		}
		
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
		Categoria categoriaNueva = categoriaRepository.findById(categoria).orElse(null);
		categoriaNueva.getListaAnuncios().add(nuevoAnuncio);
		usuario.getListaOfertados().add(nuevoAnuncio);
		nuevoAnuncio.setAutorAnuncio(usuario);
		anuncioRepository.save(nuevoAnuncio);
		categoriaRepository.save(categoriaNueva);
		usuarioRepository.save(usuario);
		return nuevoAnuncio;
		}
	}
	
	/**
	 * Metodo para añadir un anuncio sin foto, le añadimos una foto por defecto
	 * @param email
	 * @param titulo
	 * @param categoria
	 * @param precio
	 * @param tipoPrecio
	 * @param descripcion
	 * @param ubicacion
	 * @param file
	 * @return
	 */
	public Anuncio addAnuncioCompletoSinFoto(String email, String titulo, String categoria, String precio, String tipoPrecio,
			String descripcion, String ubicacion) {
		Double convertPrecio = Double.parseDouble(precio);
		if(titulo.isBlank() || titulo == null || convertPrecio < 0 ) {
			throw new CrearAnuncioException();
		}
		else {
		Anuncio nuevoAnuncio = new Anuncio(titulo, categoria, convertPrecio, tipoPrecio, descripcion, ubicacion);
		nuevoAnuncio.setFechaAnuncio(LocalDateTime.now());
		nuevoAnuncio.setUbicacion(ubicacion);
		
		
		try {
			byte[] imagenDefault = Files.readAllBytes(Paths.get("src/main/resources/static/img/default.png"));
			nuevoAnuncio.setFile(imagenDefault);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			
		
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
		Categoria categoriaNueva = categoriaRepository.findById(categoria).orElse(null);
		categoriaNueva.getListaAnuncios().add(nuevoAnuncio);
		usuario.getListaOfertados().add(nuevoAnuncio);
		nuevoAnuncio.setAutorAnuncio(usuario);
		anuncioRepository.save(nuevoAnuncio);
		categoriaRepository.save(categoriaNueva);
		usuarioRepository.save(usuario);
		return nuevoAnuncio;
		}
	}
	
	/**
	 * Metodo para editar un anuncio, le pasamos un id y un anuncio nuevo para editar los cambios
	 * @param id
	 * @param anuncio
	 * @return el nuevo anuncio ya editado
	 */
	public Anuncio editAnuncio(int id, String email, String titulo, String categoria, String precio, String tipoPrecio,
			String descripcion, String ubicacion, MultipartFile file) {
//		Anuncio nuevoAnuncio = new Anuncio(anuncio.getTitulo(), anuncio.getCategoria(), anuncio.getPrecio(), anuncio.getTipoPrecio(), anuncio.getDescripcion());
//		nuevoAnuncio.setFechaAnuncio(LocalDate.now());
		Double convertPrecio = Double.parseDouble(precio);
		if(titulo.isBlank() || titulo == null || convertPrecio < 0 ) {
			throw new CrearAnuncioException();
		}
		else {
		Anuncio anuncioEditar = anuncioRepository.getById(id);
		anuncioEditar.setTitulo(titulo);
		anuncioEditar.setCategoria(categoria);
		anuncioEditar.setPrecio(convertPrecio);
		anuncioEditar.setTipoPrecio(tipoPrecio);
		anuncioEditar.setDescripcion(descripcion);
		anuncioEditar.setUbicacion(ubicacion);
		try {
			if(file != null) {
				anuncioEditar.setFile(file.getBytes());
			}
			else {
				anuncioEditar.setFile(null);
			}
		} catch (IOException e) {
			throw new CrearAnuncioException();
		}
		
		anuncioRepository.save(anuncioEditar);
		return anuncioEditar;
		}
		
	}
	
	/**
	 * Metodo para editar un anuncio, le pasamos un id y un anuncio nuevo para editar los cambios
	 * @param id
	 * @param anuncio
	 * @return el nuevo anuncio ya editado
	 */
	public Anuncio editAnuncioNoImage(int id, String email, String titulo, String categoria, String precio, String tipoPrecio,
			String descripcion, String ubicacion) {
//		Anuncio nuevoAnuncio = new Anuncio(anuncio.getTitulo(), anuncio.getCategoria(), anuncio.getPrecio(), anuncio.getTipoPrecio(), anuncio.getDescripcion());
//		nuevoAnuncio.setFechaAnuncio(LocalDate.now());
		Double convertPrecio = Double.parseDouble(precio);
		if(titulo.isBlank() || titulo == null || convertPrecio < 0 ) {
			throw new CrearAnuncioException();
		}
		else {
		Anuncio anuncioEditar = anuncioRepository.getById(id);
		anuncioEditar.setTitulo(titulo);
		anuncioEditar.setCategoria(categoria);
		anuncioEditar.setPrecio(convertPrecio);
		anuncioEditar.setTipoPrecio(tipoPrecio);
		anuncioEditar.setDescripcion(descripcion);
		anuncioEditar.setUbicacion(ubicacion);
		
		
		anuncioRepository.save(anuncioEditar);
		return anuncioEditar;
		}
		
	}
	

	/**
	 * Este método sirve para borrar un anuncio, para ello tenemos que eliminar el anuncio en cascada de la lista de categoria y de la lista de ofertados
	 * @param idAnuncio
	 * @param email
	 */
	public void borrarAnuncio(int idAnuncio, String email) {
		Anuncio anuncioBorrar = anuncioRepository.getById(idAnuncio);
		if(usuarioRepository.findByEmail(email).orElse(null).getListaOfertados().contains(anuncioBorrar)) {
			categoriaRepository.getById(anuncioBorrar.getCategoria()).getListaAnuncios().remove(anuncioBorrar);
			usuarioRepository.findByEmail(email).orElse(null).getListaOfertados().remove(anuncioBorrar);
			anuncioRepository.deleteById(idAnuncio);
		}
		else {
			throw new AnuncioIdNoEstaEnListaExcetion(idAnuncio);
		}
		
	}
	
	/**
	 * Metodo para mostrar anuncio por id
	 * @param idAnuncio
	 * @return el anuncio concreto
	 */
	public Anuncio mostrarAnuncio(int idAnuncio) {
		return anuncioRepository.getById(idAnuncio);
		
		
	}

	
	/**
	 * metodo para marcar un anuncio como finalizado, para ello se realizan diferentes operacion
	 * En primer lugar se modifica el usuario al que pertenece el anuncio, eliminandolo de su lista de pendientes, además
	 * se le modifica la valoración puesta por el solicitante en el comentario
	 * En segundo lugar se modifica el usuario solicitante, al cual se le asigna el anuncio como realizado
	 * En tercer lugar se crea el comentario que realiza el dueño del anuncio al solicitante que lo realiza
	 * En cuarto lugar se crea un comentario vacío, el cual tendrá que rellenar el solicitante valorando al ofertante del anuncio,
	 * esta operación la realizará el solicitante cuando desee, mientras tanto el comentario aparecerá como no realizado
	 * @param idAnuncio
	 * @param emailSolicitante
	 * @param textoComentario
	 * @param puntuacionEstrellas
	 * @param email
	 * @return El anuncio finalizado
	 */
	public Anuncio finalizarAnuncio(int idAnuncio, String emailSolicitante, String textoComentario,
		String puntuacionEstrellas, String email) {
		
		Anuncio anuncioBorrar = anuncioRepository.getById(idAnuncio);
		//Convertimos la puntuación a entero
		int puntuacionEstrellasDouble = Integer.parseInt(puntuacionEstrellas);
		
		if(usuarioRepository.findByEmail(email).orElse(null).getListaOfertados().contains(anuncioBorrar)) {
		//Realizamos operacion para el usuario
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
		usuario.getListaOfertados().remove(anuncioBorrar);
		
		anuncioBorrar.setFechaFin(LocalDateTime.now());
		anuncioBorrar.setFinalizado(true);
		
		usuario.getListaTerminados().add(anuncioBorrar);
		
		//Realizamos operaciones para solicitante
		Usuario solicitante = usuarioRepository.findByEmail(emailSolicitante).orElse(null);
		solicitante.getListaDemandados().remove(anuncioBorrar);
		solicitante.getListaRealizados().add(anuncioBorrar);
		solicitante.setPuntuacionMedia(puntuacionEstrellasDouble);
		
		//Realizamos operaciones de añadir el comentario
		Comentario comentario = new Comentario();
		comentario.setUsuarioComentador(usuario);
		comentario.setUsuarioComentado(solicitante);
		comentario.setAnuncio(anuncioBorrar);
		comentario.setTextoComentario(textoComentario);
		comentario.setPuntuacionEstrellas(puntuacionEstrellasDouble);
		comentario.setFecha(LocalDate.now());
		comentario.setRealizado(true);
		comentario.setRealizaste(true);
		
		//Este comentario lo creamos y se rellenará cuando el solicitante decida rellenarlo, mientras
		//no lo rellene aparecerá como pendiente
		Comentario comentarioPorRealizar = new Comentario();
		comentarioPorRealizar.setUsuarioComentador(solicitante);
		comentarioPorRealizar.setUsuarioComentado(usuario);
		comentarioPorRealizar.setAnuncio(anuncioBorrar);
		
		//Persistimos en bd todas las modificaciones
		comentarioRepository.save(comentario);
		comentarioRepository.save(comentarioPorRealizar);
		usuarioRepository.save(usuario);
		usuarioRepository.save(solicitante);
		return anuncioBorrar;
		}
		else {
			throw new AnuncioIdNoEstaEnListaExcetion(idAnuncio);
		}

		
	}

	/**
	 * Metodo para mostrar los solicitantes de un anuncio el cual recibimos su id
	 * @param idAnuncio
	 * @return
	 */
	public List<Usuario> mostrarSolicitantesAnuncio(int idAnuncio) {
		List<Usuario> listaSolicitantesAnuncio = anuncioRepository.getById(idAnuncio).getListaSolicitantes();
		return listaSolicitantesAnuncio;
	}
	
	/**
	 * Metodo para mostrar los 6 anuncios mas recientes
	 * @return lista de anuncios
	 */
	public List<Anuncio> mostrarAnunciosRecientes() {
		List<Anuncio> listaAnunciosRecientes = anuncioRepository.getRecientes();
		return listaAnunciosRecientes;
	}

	/**
	 * Metodo para mostrar una lista de anuncios según los filtros elegidos
	 * @param termino
	 * @param categoria
	 * @param rangoPrecio
	 * @param orden
	 * @return lista de anuncios filtrada
	 */
	public List<Anuncio> mostrarAnunciosPorFiltro(String termino, String categoria, int[] rangoPrecio, String orden) {
		List<Anuncio> listaAnuncios = new ArrayList<Anuncio>();
		termino = termino.toUpperCase();
		if(!categoria.equals("Todas las categorias") && rangoPrecio[0] == 0 && rangoPrecio[1] == 5000) {
			if(orden.equals("Novedades")) {
				listaAnuncios = anuncioRepository.getAnunciosPorCategoriaNovedades(termino, categoria);
			}
			else if(orden.equals("De más barato a más caro")) {
				listaAnuncios = anuncioRepository.getAnunciosPorCategoriaPrecioAscendente(termino, categoria);
			}
			else if(orden.equals("De más caro a más barato")) {
				listaAnuncios = anuncioRepository.getAnunciosPorCategoriaPrecioDescendente(termino, categoria);
			}
			else if(orden.equals("Distancia")) {
				//No disponible
			}
			else {
				listaAnuncios = anuncioRepository.getAnunciosPorCategoriaAntiguos(termino, categoria);
			}
		}
		else if((rangoPrecio[0] != 0 || rangoPrecio[1] != 5000) && categoria.equals("Todas las categorias")) {
			if(orden.equals("Novedades")) {
				listaAnuncios = anuncioRepository.getAnunciosPorPrecioNovedades(termino, rangoPrecio[0], rangoPrecio[1]);
			}
			else if(orden.equals("De más barato a más caro")) {
				listaAnuncios = anuncioRepository.getAnunciosPorPrecioAscendente(termino, rangoPrecio[0], rangoPrecio[1]);
			}
			else if(orden.equals("De más caro a más barato")) {
				listaAnuncios = anuncioRepository.getAnunciosPorPrecioDescendiente(termino, rangoPrecio[0], rangoPrecio[1]);
			}
			else if(orden.equals("Distancia")) {
				//No disponible
			}
			else {
				listaAnuncios = anuncioRepository.getAnunciosPorPrecioAntiguos(termino, rangoPrecio[0], rangoPrecio[1]);
			}
			
		}
		else if(!categoria.equals("Todas las categorias") && rangoPrecio[0] != 0 || rangoPrecio[1] != 5000) {
			if(orden.equals("Novedades")) {
				listaAnuncios = anuncioRepository.getAnunciosPorCategoriaPrecioNovedades(termino, categoria, rangoPrecio[0], rangoPrecio[1]);
			}
			else if(orden.equals("De más barato a más caro")) {
				listaAnuncios = anuncioRepository.getAnunciosPorCategoriaPrecioAscendente(termino, categoria, rangoPrecio[0], rangoPrecio[1]);
			}
			else if(orden.equals("De más caro a más barato")) {
				listaAnuncios = anuncioRepository.getAnunciosPorCategoriaPrecioDescendente(termino, categoria, rangoPrecio[0], rangoPrecio[1]);
			}
			else if(orden.equals("Distancia")) {
				//No disponible
			}
			else {
				listaAnuncios = anuncioRepository.getAnunciosPorCategoriaPrecioAntiguos(termino, categoria, rangoPrecio[0], rangoPrecio[1]);
			}
			
		}
		else if(termino != null) {
			if(orden.equals("Novedades")) {
				listaAnuncios = anuncioRepository.getAnunciosPorTerminoNovedades(termino);
			}
			else if(orden.equals("De más barato a más caro")) {
				listaAnuncios = anuncioRepository.getAnunciosPorTerminoPrecioAscendente(termino);
			}
			else if(orden.equals("De más caro a más barato")) {
				listaAnuncios = anuncioRepository.getAnunciosPorTerminoPrecioDescendente(termino);
			}
			else if(orden.equals("Distancia")) {
				//No disponible
			}
			else {
				listaAnuncios = anuncioRepository.getAnunciosPorTerminoAntiguos(termino);
			}
			
		}
		else {
			if(orden.equals("Novedades")) {
				listaAnuncios = anuncioRepository.getAnunciosSinTerminoNovedades();
			}
			else if(orden.equals("De más barato a más caro")) {
				listaAnuncios = anuncioRepository.getAnunciosSinTerminoPrecioAscendente();
			}
			else if(orden.equals("De más caro a más barato")) {
				listaAnuncios = anuncioRepository.getAnunciosSinTerminoPrecioDescendente();
			}
			else if(orden.equals("Distancia")) {
				//No disponible
			}
			else {
				listaAnuncios = anuncioRepository.getAnunciosSinTerminoAntiguos();
			}
			
			
		}
		return listaAnuncios;
	}

	/**
	 * Metodo para añadir anuncio a favoritos, recibe el email del usuario y el anuncio y lo añade a la lista de favoritos
	 * @param email
	 * @param anuncio
	 */
	public Anuncio addFavorito(String email, Anuncio anuncio) {
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
			try {
				usuario.getListaFavoritos().add(anuncio);
				usuarioRepository.save(usuario);
				return anuncio;
			      
			    } catch (Exception e) {
			    	if(anuncioRepository.existsById(anuncio.getId())) {
			    		throw new AnuncioYaExistenteException(anuncio.getId());
			    	}
			    	else {
			    		throw new AnuncioIdNotFoundException(anuncio.getId());
			    	}
			    }
			  }
	
	/**
	 * Metodo para borrar un anuncio de favoritos
	 * @param idAnuncio
	 * @param email
	 */
	public void borrarAnuncioFavorito(int idAnuncio, String email) {
		Anuncio anuncioBorrar = anuncioRepository.getById(idAnuncio);
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
		if(usuario.getListaFavoritos().contains(anuncioBorrar)) {
			usuario.getListaFavoritos().remove(anuncioBorrar);
			
			usuarioRepository.save(usuario);
			
		}
		else {
			throw new AnuncioIdNoEstaEnListaExcetion(idAnuncio);
		}
		
	}

	/**
	 * Metodo para mostrar los anuncios que tiene el usuario en favoritos
	 * @param email
	 * @return
	 */
	public List<Anuncio> mostrarAnunciosFavoritos(String email) {
		
		return usuarioRepository.findByEmail(email).orElse(null).getListaFavoritos();
	}
	
	
	/**
	 * Metodo para añadir un anuncio a la lista de solicitados, y al anuncio la lista de solicitantes
	 * @param email
	 * @param anuncio
	 */
	public Anuncio addAnuncioSolicitado(String email, Anuncio anuncio) {
		
			if(comprobarAnuncioSolicitado(email, anuncio)) {
				throw new AnuncioYaExistenteException(anuncio.getId());
			}
			else {
				try {
					
					Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
//					anuncioRepository.getById(anuncio.getId()).getListaSolicitantes().add(usuario);
					Anuncio anuncioEditar = anuncioRepository.findById(anuncio.getId()).orElse(null);
					anuncioEditar.getListaSolicitantes().add(usuario);
					
					usuario.getListaDemandados().add(anuncioEditar);
					usuarioRepository.save(usuario);
					return anuncioEditar;
				}
				catch (Exception e) {
					throw new AnuncioYaExistenteException(69);
				}
				}
			}
			
			
			
		
		
	
	
	/**
	 * Metodo para comprobar si el anuncio ya ha sido añadido a la lista de solicitados
	 * @param email
	 * @param anuncio
	 * @return
	 */
	public boolean comprobarAnuncioSolicitado(String email, Anuncio anuncio) {
		
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
		if(anuncioRepository.getById(anuncio.getId()).getListaSolicitantes().contains(usuario)) {
			return true;
		}
		else {
			return false;
		}
		
		
	}
	
	//############## Crud de 2º nivel, no sirven para la aplicacion real
	
	/**
	 * Este metodo añadira un solicitante a un anuncio concreto
	 * @param idAnuncio
	 * @param usuarioSolicitante
	 * @return el anuncio al que se ha añadido
	 */
	public Anuncio addSolicitanteAnuncio(int idAnuncio, Usuario usuarioSolicitante) {
		Anuncio anuncioEditar = anuncioRepository.getById(idAnuncio);
			if(anuncioEditar.getListaSolicitantes().contains(usuarioSolicitante)) {
				throw new AnuncioYaExistenteException(idAnuncio);
			}
			else {
				Usuario usuarioAdd = usuarioRepository.getById(usuarioSolicitante.getId());
				anuncioEditar.getListaSolicitantes().add(usuarioAdd);
				anuncioRepository.save(anuncioEditar);
				
				return anuncioEditar;
				
			}
	}
	
	/**
	 * Metodo para borrar un solicitante de un anuncio concreto
	 * @param idAnuncio
	 * @param idSolicitante
	 */
	public void borrarSolicitanteAnuncio(int idAnuncio, int idSolicitante) {
		Anuncio anuncioEditar = anuncioRepository.getById(idAnuncio);
		Usuario solicitante = usuarioRepository.getById(idSolicitante);
		anuncioEditar.getListaSolicitantes().remove(solicitante);
		anuncioRepository.save(anuncioEditar);
				
	}
	


}
