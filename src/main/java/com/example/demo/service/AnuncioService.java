package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.error.AnuncioIdNoEstaEnListaExcetion;
import com.example.demo.error.AnuncioIdNotFoundException;
import com.example.demo.error.AnuncioYaExistenteException;
import com.example.demo.error.CrearAnuncioException;
import com.example.demo.model.Anuncio;
import com.example.demo.model.Categoria;
import com.example.demo.model.Usuario;
import com.example.demo.repository.AnuncioRepository;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.UsuarioRepository;

import net.bytebuddy.dynamic.loading.PackageDefinitionStrategy.Definition.Undefined;

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
	
	public void crearUsuario(Usuario usuario) {
		usuarioRepository.save(usuario);
	}

	/**
	 * Metodo para añadir un anuncio
	 * @param email
	 * @param anuncio
	 * @return el anuncio que se ha añadido
	 */
	public Anuncio addAnuncio(String email, Anuncio anuncio) {
		if(anuncio.getTitulo().isBlank() || anuncio.getTitulo() == null || anuncio.getPrecio() < 0 ) {
			throw new CrearAnuncioException();
		}
		else {
		Anuncio nuevoAnuncio = new Anuncio(anuncio.getTitulo(), anuncio.getCategoria(), anuncio.getPrecio(), anuncio.getTipoPrecio(), anuncio.getDescripcion());
		nuevoAnuncio.setFechaAnuncio(LocalDateTime.now());
		nuevoAnuncio.setUbicacion(anuncio.getUbicacion());
		
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
		Categoria categoria = categoriaRepository.findById(anuncio.getCategoria()).orElse(null);
		categoria.getListaAnuncios().add(nuevoAnuncio);
		usuario.getListaOfertados().add(nuevoAnuncio);
		nuevoAnuncio.setAutorAnuncio(usuario);
		anuncioRepository.save(nuevoAnuncio);
		categoriaRepository.save(categoria);
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
	public Anuncio editAnuncio(int id, Anuncio anuncio) {
//		Anuncio nuevoAnuncio = new Anuncio(anuncio.getTitulo(), anuncio.getCategoria(), anuncio.getPrecio(), anuncio.getTipoPrecio(), anuncio.getDescripcion());
//		nuevoAnuncio.setFechaAnuncio(LocalDate.now());
		if(anuncio.getTitulo().isBlank() || anuncio.getTitulo() == null || anuncio.getPrecio() < 0 ) {
			throw new CrearAnuncioException();
		}
		else {
		Anuncio anuncioEditar = anuncioRepository.getById(id);
		anuncioEditar.setTitulo(anuncio.getTitulo());
		anuncioEditar.setCategoria(anuncio.getCategoria());
		anuncioEditar.setPrecio(anuncio.getPrecio());
		anuncioEditar.setTipoPrecio(anuncio.getTipoPrecio());
		anuncioEditar.setDescripcion(anuncio.getDescripcion());
		
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
	 * metodo para marcar un anuncio como finalizado
	 * @param idAnuncio
	 * @param email
	 * @return el anuncio que se ha finalizado
	 */
	public Anuncio finalizarAnuncio(int idAnuncio, String email) {
		Anuncio anuncioBorrar = anuncioRepository.getById(idAnuncio);
		
		if(usuarioRepository.findByEmail(email).orElse(null).getListaOfertados().contains(anuncioBorrar)) {
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
		usuario.getListaOfertados().remove(anuncioBorrar);
		
		anuncioBorrar.setFechaFin(LocalDateTime.now());
		
		anuncioBorrar.setFinalizado(true);
		
		usuario.getListaTerminados().add(anuncioBorrar);
		
		usuarioRepository.save(usuario);
		return anuncioBorrar;
		}
		else {
			throw new AnuncioIdNoEstaEnListaExcetion(idAnuncio);
		}

		
	}
	
	
	public Anuncio solicitanteAddAnuncio(int idAnuncio, String email, String emailSolicitante) {
		Anuncio anuncioAdd = anuncioRepository.getById(idAnuncio);
		System.out.println(emailSolicitante);
//		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
		Usuario solicitante = usuarioRepository.findByEmail(emailSolicitante).orElse(null);
		System.out.println(solicitante.getNombre());
//		usuario.getListaOfertados().remove(anuncioBorrar);
		solicitante.getListaDemandados().remove(anuncioAdd);
		solicitante.getListaRealizados().add(anuncioAdd);
		
		
		
		
		usuarioRepository.save(solicitante);
		return anuncioAdd;
		
		
	}

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
		try {
			Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
			anuncioRepository.getById(anuncio.getId()).getListaSolicitantes().add(usuario);
			Anuncio anuncioEditar = anuncioRepository.findById(anuncio.getId()).orElse(null);
			
			usuario.getListaDemandados().add(anuncio);
			
//			anuncioRepository.save(anuncioEditar);
			usuarioRepository.save(usuario);
			return anuncioEditar;
		}
		catch (Exception e) {
			throw new AnuncioYaExistenteException(anuncio.getId());
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
	
	
	
	
	

	/**
	 * Este metodo será para comprobar si algno de los resultados esta en favoritos, lo implementaremos más adelante
	 */
	
//	public List<Anuncio> modificarListaResultado(String email, List<Anuncio> mostrarAnunciosPorFiltro) {
//		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
//		List<Anuncio> listaAnunciosConFavoritos = mostrarAnunciosPorFiltro;
//		for(int i = 0 ; i<mostrarAnunciosPorFiltro.size(); i++) {
//			for(int j = 0 ; j<usuario.getListaFavoritos().size(); j++) {
//				if(mostrarAnunciosPorFiltro.get(i) == usuario.getListaFavoritos().get(j)) {
//					listaAnunciosConFavoritos.get(i).setEnFavoritos(true);
//				}
//			}
//			}
//		
//		
//		return listaAnunciosConFavoritos;
//	}
}
