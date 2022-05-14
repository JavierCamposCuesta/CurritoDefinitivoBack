package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.error.AnuncioIdNoEstaEnListaExcetion;
import com.example.demo.error.AnuncioIdNotFoundException;
import com.example.demo.error.AnuncioYaExistenteException;
import com.example.demo.error.ApiError;
import com.example.demo.error.CategoriaSinDatosException;
import com.example.demo.error.CrearAnuncioException;
import com.example.demo.error.LoginInvalidException;
import com.example.demo.error.ParametrosInvalidosException;
import com.example.demo.error.SolicitanteNoExisteEnAnuncioException;
import com.example.demo.error.TokenInvalidException;
import com.example.demo.error.UsuarioIdNotFoundException;
import com.example.demo.model.Anuncio;
import com.example.demo.model.Categoria;
import com.example.demo.model.Usuario;
import com.example.demo.repository.AnuncioRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.AnuncioService;
import com.example.demo.service.CategoriaService;
import com.example.demo.service.UsuarioService;

/**
 * Clase controlador, en ella implementaremos los métodos necesarios para responder las peticiones desde el front
 * @author javier
 *
 */
@CrossOrigin(origins = "http://localhost:4200")
//@CrossOrigin(origins = "https://javiercamposcuesta.github.io")
@RestController
public class CurritoController {
	
	@Autowired
	private AnuncioService anuncioService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired private UsuarioRepository usuarioRepository;
	
	@Autowired private AnuncioRepository anuncioRepository;
	
	
	
	
	/**
	 * Este método devolverá la lista de categorias, así como una page y un limite, los cuales servirán para futuras modificaciones, actualmente no nos sirve
	 * En caso de no existir ninguna categoría devolverá un error correspondiente
	 * @return json con lista de categorias, page y limite
	 */
	@GetMapping("/categorias")
	public ResponseEntity<Map<String, Object>> mostrarCategorias(){
		if(categoriaService.mostrarCategorias().isEmpty()) {
			throw new CategoriaSinDatosException();
		}
		else {
			Map<String, Object> map = new HashMap<>();
			map.put("data", categoriaService.mostrarCategorias());
			map.put("page", 3);
			map.put("limit", 250);
			return ResponseEntity.ok(map);
		}
	}
	
	
	/**
	 * Este método recibe un token en la cabecera de la petición y comprueba si el token es válido, en caso de ser válido
	 * devuelve el usuario al que pertenece, en caso contrario devuelve la exepcion correspondiente
	 * @return usuario | null
	 */
	@GetMapping("comprobarToken")
    public ResponseEntity<Usuario> validarToken(){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(email!=null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
        	return ResponseEntity.ok(usuarioService.usuarioPorEmail(email));
        }
        else {
        	throw new TokenInvalidException();
        }
    }
	
	
	
	/**
	 * Este método recibe un objeto de tipo anuncio como parámetro y añade el anuncio donde corresponde
	 * @param anuncio
	 * @return anuncio en caso que se haya añadido el anuncio, notFound en caso de que el usuario no esté logueado
	 * y error correspondiente en caso de que no se pueda crear debido a campos invalidos
	 */
//	@PostMapping("/anuncio")
//	public ResponseEntity<Anuncio> addCurrito(@RequestParam MultipartFile file
//			, @RequestParam String titulo, @RequestParam String categoria,
//			@RequestParam String precio, @RequestParam String tipoPrecio, @RequestParam String descripcion, @RequestParam String ubicacion
//			){
//		System.out.println("fdsfdsf1");
//		System.out.println(titulo);
//		System.out.println(file.getOriginalFilename());
//		
//			String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			if(email!=null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
//				
//			
//				//	return new ResponseEntity<Anuncio>(anuncioService.addAnuncio(email, anuncio), HttpStatus.CREATED);
//				return new ResponseEntity<Anuncio>(HttpStatus.CREATED);
//
//			}
//			else {
//				throw new TokenInvalidException();
//			}
//				}
	
	@PostMapping("/anuncio")
	public ResponseEntity<Anuncio> addCurrito(@RequestParam MultipartFile file
			, @RequestParam String titulo, @RequestParam String categoria,
			@RequestParam String precio, @RequestParam String tipoPrecio, @RequestParam String descripcion, @RequestParam String ubicacion
			){
		
			String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(email!=null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
				
			
					return new ResponseEntity<Anuncio>(anuncioService.addAnuncioCompleto(email, titulo, categoria, precio, tipoPrecio, descripcion, ubicacion, file), HttpStatus.CREATED);
				//return new ResponseEntity<Anuncio>(HttpStatus.CREATED);

			}
			else {
				throw new TokenInvalidException();
			}
				}
	
	/**
	 * Este método devolverá un anuncio por id, en caso de no existir el anuncio con ese id devolverá la exepcion correspondiente
	 * @param idAnuncio
	 * @return anuncio
	 */
	@GetMapping("/anuncio/{id}")
	public ResponseEntity<Anuncio> mostrarAnuncio(@PathVariable(value="id")int idAnuncio){
			
			if( anuncioRepository.existsById(idAnuncio)) {
				return ResponseEntity.ok(anuncioService.mostrarAnuncio(idAnuncio));
			}
			else {
				throw new AnuncioIdNotFoundException(idAnuncio);
			}
				}
	
	/**
	 * Método para actualizar anuncios, recibe un id de anuncio y un anuncio, en caso que el anuncio exista cambia los valores
	 * del antiguo anuncio por los del nuevo
	 * @param anuncio
	 * @param idAnuncio
	 * @return Anuncio modificado en caso de que todo haya ido bien, error en caso de que no exista el anuncio o no este correctamente logueado
	 */
	@PutMapping("/anuncio/{id}")
	public ResponseEntity<Anuncio> editCurrito(@RequestParam MultipartFile file
			, @RequestParam String titulo, @RequestParam String categoria,
			@RequestParam String precio, @RequestParam String tipoPrecio, @RequestParam String descripcion, @RequestParam String ubicacion,
			@PathVariable(value="id")int idAnuncio){
		
			String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(email!=null && anuncioRepository.getById(idAnuncio)!= null) {
				if(anuncioRepository.existsById(idAnuncio)) {
					
					return new ResponseEntity<Anuncio>(anuncioService.editAnuncio(idAnuncio, email, titulo, categoria, precio, tipoPrecio, descripcion, ubicacion, file), HttpStatus.CREATED);
				}
				else {
					throw new AnuncioIdNotFoundException(idAnuncio);
				}
			}
			else {
				throw new TokenInvalidException();
			}
				}
	
//	/**
//	 * Método para actualizar anuncios, recibe un id de anuncio y un anuncio, en caso que el anuncio exista cambia los valores
//	 * del antiguo anuncio por los del nuevo
//	 * @param anuncio
//	 * @param idAnuncio
//	 * @return Anuncio modificado en caso de que todo haya ido bien, error en caso de que no exista el anuncio o no este correctamente logueado
//	 */
//	@PutMapping("/anuncio/{id}")
//	public ResponseEntity<Anuncio> editCurrito(@RequestBody Anuncio anuncio, @PathVariable(value="id")int idAnuncio){
//			String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			if(email!=null && anuncioRepository.getById(idAnuncio)!= null) {
//				if(anuncioRepository.existsById(idAnuncio)) {
//					
//					return new ResponseEntity<Anuncio>(anuncioService.editAnuncio(idAnuncio, anuncio), HttpStatus.CREATED);
//				}
//				else {
//					throw new AnuncioIdNotFoundException(idAnuncio);
//				}
//			}
//			else {
//				throw new TokenInvalidException();
//			}
//				}
	
	
	
	/**
	 * Este metodo sirve para borrar anuncio por id, en caso que el token o el id del anuncio sean inválidos se devolverá la exepcion
	 * correspondiente
	 * @param idAnuncio
	 * @return no content es caso de que se borre y exepcion correspondiente en caso que falle
	 */
	@DeleteMapping("/anuncio/{id}")
	public ResponseEntity<?> borrarAnuncio( @PathVariable(value="id")int idAnuncio){
			String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(email!=null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
				if(anuncioRepository.existsById(idAnuncio)) {
					
					anuncioService.borrarAnuncio(idAnuncio, email);
					return ResponseEntity.noContent().build();
				}
				else {
					throw new AnuncioIdNotFoundException(idAnuncio);
				}
				
			}
			else {
				throw new TokenInvalidException();
			}
				}
	
	/**
	 * Metodo que devuelve todos los anuncios que hay en la lista ListaOfertados
	 * @return lista o exepcion en caso de token invalido
	 */
	@GetMapping("profile/mis-anuncios")
    public ResponseEntity<List<Anuncio>> mostrarAnuncios(){
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(email != null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
			return ResponseEntity.ok(usuarioService.mostrarAnuncios(email));
		}
		else {
			throw new TokenInvalidException();
		}
		
    }
	
	/**
	 * Metodo que devuelve todos los anuncios que hay en la lista ListaTerminados
	 * @return lista o exepcion en caso de token invalido
	 */
	@GetMapping("profile/mis-anuncios-terminados")
    public ResponseEntity<List<Anuncio>> mostrarAnunciosTerminados(){
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(email != null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
			return ResponseEntity.ok(usuarioService.mostrarAnunciosTerminados(email));
		}
		else {
			throw new TokenInvalidException();
		}
		
    }
	
	/**
	 * Metodo que devuelve todos los anuncios que hay en la lista ListaSolicitados
	 * @return lista o exepcion en caso de token invalido
	 */
	@GetMapping("profile/mis-anuncios-solicitados")
    public ResponseEntity<List<Anuncio>> mostrarAnunciosSolicitados(){
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(email != null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
			return ResponseEntity.ok(usuarioService.mostrarAnunciosSolicitados(email));
		}
		else {
			throw new TokenInvalidException();
		}
		
    }
	
	/**
	 * Metodo que devuelve todos los anuncios que hay en la lista ListaRealizados
	 * @return lista o exepcion en caso de token invalido
	 */
	@GetMapping("profile/mis-anuncios-realizados")
    public ResponseEntity<List<Anuncio>> mostrarAnunciosRealizados(){
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(email != null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
			return ResponseEntity.ok(usuarioService.mostrarAnunciosRealizados(email));
		}
		else {
			throw new TokenInvalidException();
		}
		
    }
	
	/**
	 * Metodo para finalizar un anuncio, le pasamos el id del anuncio que vamos a finalizar
	 * @param idAnuncio
	 * @return El anuncio finalizado en caso que se realice la operacion, exepcion en caso que no exista el anuncio con ese id o el usuario no esté autorizado
	 */
	//Cambiar a put
	@GetMapping("/anuncio/{id}/finalizar-anuncio")
	public ResponseEntity<Anuncio> finalizarAnuncio(@PathVariable(value="id")int idAnuncio){
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(email!=null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
			if(anuncioRepository.existsById(idAnuncio)) {
				return ResponseEntity.ok(anuncioService.finalizarAnuncio(idAnuncio, email));
			}
			else {
				throw new AnuncioIdNotFoundException(idAnuncio);
			}
		}
		else {
			return ResponseEntity.notFound().build(); 
		}
	}
	
	@GetMapping("/anuncio/{idAnuncio}/finalizar-anuncio/{emailSolicitante}")
	public ResponseEntity<Anuncio> solicitanteAddAnuncio(@PathVariable int idAnuncio, @PathVariable String emailSolicitante){
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(email!=null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
			if(anuncioRepository.existsById(idAnuncio)) {
				return ResponseEntity.ok(anuncioService.solicitanteAddAnuncio(idAnuncio, email, emailSolicitante));
			}
			else {
				throw new AnuncioIdNotFoundException(idAnuncio);
			}
		}
		else {
			return ResponseEntity.notFound().build(); 
		}
	}
	
	/**
	 * Metodo que devuelve todos los anuncios recientes, con un máximo de 6 anuncios
	 * @return lista de anuncios recientes
	 */
	@GetMapping("anuncios/anuncios-recientes")
    public ResponseEntity<List<Anuncio>> mostrarAnunciosRecientes(){
			return ResponseEntity.ok(anuncioService.mostrarAnunciosRecientes());
		
    }
	

	
	//########## Busquedas de anuncios
	
	/**
	 * Metodo de filtrado de anuncios, recibirá unos parámetros y devolverá una lista con el filtrado corresponediente
	 * @param termino
	 * @param categoria
	 * @param rangoPrecio
	 * @param orden
	 * @return lista de anuncios
	 */
	@GetMapping("anuncios")
    public ResponseEntity<List<Anuncio>> mostrarAnunciosTermino(String termino, String categoria, int[] rangoPrecio, String orden){
		
		if(termino == null || categoria == null || rangoPrecio == null || orden == null) {
			throw new ParametrosInvalidosException();
		}
		else {
			if(termino.equals("undefined")) {
				termino = " ";
				return ResponseEntity.ok(anuncioService.mostrarAnunciosPorFiltro(termino, categoria, rangoPrecio, orden));
			}
			else {
				return ResponseEntity.ok(anuncioService.mostrarAnunciosPorFiltro(termino, categoria, rangoPrecio, orden));
			}
			
		}
		
		
    }
	
	/**
	 * Este metodo sirve para añadir anuncios a favoritos
	 * @param anuncio
	 * @return el anuncio que se ha añadido, error en caso de que ya estuviese añadido o error en caso de token invalido
	 */
	@PostMapping("/favoritos")
	public ResponseEntity<Anuncio> addFavoritos(@RequestBody Anuncio anuncio){
			String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(email!=null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
				return new ResponseEntity<Anuncio>(anuncioService.addFavorito(email, anuncio), HttpStatus.CREATED);
			}
			else {
				throw new TokenInvalidException();
			}
				}
	
	
	/**
	 * Metodo para borrar de la lista de favoritos
	 * @param idAnuncio
	 * @return no content en caso de poder eliminarlo, si no devuelve exepciones
	 */
	@DeleteMapping("/favoritos/{id}")
	public ResponseEntity<?> borrarAnuncioFavorito( @PathVariable(value="id")int idAnuncio){
			String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(email!=null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
				if(anuncioRepository.existsById(idAnuncio)) {
					anuncioService.borrarAnuncioFavorito(idAnuncio, email);
					return ResponseEntity.noContent().build();
				}
				else {
					throw new AnuncioIdNotFoundException(idAnuncio);
				}
					
			}
			else {
				throw new TokenInvalidException();
			}
				}
	
	/**
	 * Metodo para mostrar los anuncios en la lista de favoritos
	 * @return lista de los anuncios en favorito o excepcion si el token no es valido
	 */
	@GetMapping("favoritos")
    public ResponseEntity<List<Anuncio>> mostrarAnunciosFavoritos(){
		
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(email != null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
			return ResponseEntity.ok(anuncioService.mostrarAnunciosFavoritos(email));
		}
		else {
			throw new TokenInvalidException();
		}
		
    }
	
	@GetMapping("anuncio/{idAnuncio}/solicitantes")
    public ResponseEntity<List<Usuario>> mostrarSolicitantesAnuncio(@PathVariable int idAnuncio){
		
		
		
			return ResponseEntity.ok(anuncioService.mostrarSolicitantesAnuncio(idAnuncio));
		
		
    }
	
	/**
	 * Metodo para añadir un anuncio a la lista de solicitados
	 * @param anuncio
	 * @return El anuncio si se añade correctamente | Token invalido | Anuncio no existente | Anuncio ya añadido en la lista
	 */
	@PostMapping("/anuncios-solicitados")
	public ResponseEntity<Anuncio> addAnuncioSolicitado(@RequestBody Anuncio anuncio){
			String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(email!=null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
				if(anuncioRepository.existsById(anuncio.getId())) {
				
					return new ResponseEntity<Anuncio>(anuncioService.addAnuncioSolicitado(email, anuncio), HttpStatus.CREATED);
				}
				else {
					throw new AnuncioIdNotFoundException(anuncio.getId());
				}
			}
			else {
				throw new TokenInvalidException();
			}
				}
	
	
	/**
	 * Metodo para mostrar un usuario en un anuncio concreto
	 * @param idAnuncio
	 * @param idSolicitante
	 * @return usuario || usuario no existe || anuncio no existe || solicitante no existe en ese anuncio || token invalido 
	 */
	@GetMapping("anuncio/{idAnuncio}/solicitante/{idSolicitante}")
    public ResponseEntity<Usuario>mostrarSolicitanteAnuncio( @PathVariable int idAnuncio, @PathVariable int idSolicitante){
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(email != null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
			if(!anuncioRepository.existsById(idAnuncio)) {
				throw new AnuncioIdNotFoundException(idAnuncio);
			}
			if(!usuarioRepository.existsById(idSolicitante)) {
				throw new UsuarioIdNotFoundException(idSolicitante);
			}
			Anuncio anuncio = anuncioRepository.getById(idAnuncio);
			Usuario usuarioBuscar = usuarioRepository.getById(idSolicitante);
			int posicion = anuncio.getListaSolicitantes().indexOf(usuarioBuscar);
			
			if(posicion == -1) {
				throw new SolicitanteNoExisteEnAnuncioException(idAnuncio,idSolicitante);
			}
			
			
			return ResponseEntity.ok(usuarioRepository.getById(idSolicitante));
		}
		else {
			throw new TokenInvalidException();
		}
		
    }
	
	/**
	 * Metodo para añadir un solicitante a un anuncio por id
	 * @param idAnuncio
	 * @param usuario
	 * @return Anuncio al que se ha añadido el solicitante || idAnuncio no existe || usurio no existe || Anuncio ya añadido || token invalido
	 */
	@PostMapping("anuncio/{idAnuncio}/solicitante")
    public ResponseEntity<Anuncio>addSolicitanteAnuncio( @PathVariable int idAnuncio, @RequestBody Usuario usuario){
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(email != null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
			if(!anuncioRepository.existsById(idAnuncio)) {
				throw new AnuncioIdNotFoundException(idAnuncio);
			}
			
			if(!usuarioRepository.existsById(usuario.getId())) {
				throw new UsuarioIdNotFoundException(usuario.getId());
			}
			
			//Le metemos al anuncio un nuevo solicitante
			return new ResponseEntity<Anuncio>(anuncioService.addSolicitanteAnuncio(idAnuncio, usuario), HttpStatus.CREATED);
		}
		else {
			throw new TokenInvalidException();
		}
		
    }
	
	/**
	 * Metodo para borrar un solicitante concreto de un anuncio concreto
	 * @param idAnuncio
	 * @param idSolicitante
	 * @return noContent si se ha borrar || anuncio no existe || usuario no existe || solicitante no existe en ese anuncio || token invaldo
	 */
	@DeleteMapping("anuncio/{idAnuncio}/solicitante/{idSolicitante}")
    public ResponseEntity<?>borrarSolicitanteAnuncio( @PathVariable int idAnuncio, @PathVariable int idSolicitante){
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(email != null && usuarioRepository.findByEmail(email).orElse(null)!=null) {
			if(!anuncioRepository.existsById(idAnuncio)) {
				throw new AnuncioIdNotFoundException(idAnuncio);
			}
			if(!usuarioRepository.existsById(idSolicitante)) {
				throw new UsuarioIdNotFoundException(idSolicitante);
			}
			Anuncio anuncio = anuncioRepository.getById(idAnuncio);
			Usuario usuarioBuscar = usuarioRepository.getById(idSolicitante);
			int posicion = anuncio.getListaSolicitantes().indexOf(usuarioBuscar);
			
			if(posicion == -1) {
				throw new SolicitanteNoExisteEnAnuncioException(idAnuncio,idSolicitante);
			}
			
			anuncioService.borrarSolicitanteAnuncio(idAnuncio, idSolicitante);
			return ResponseEntity.noContent().build();
		}
		else {
			throw new TokenInvalidException();
		}
		
    }
	
	
	
//#################### EXEPCIONES
	
	/**
	 * Exepcion que se lanzará cuando no existan categorias
	 * @param ex
	 * @return mensaje correspondiente
	 */
	@ExceptionHandler(CategoriaSinDatosException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(CategoriaSinDatosException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NO_CONTENT);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiError);
	}
	
	/**
	 * Exepcion que se lanzará cuando el token no sea válido
	 * @param ex
	 * @return mensaje correspondiente
	 */
	@ExceptionHandler(TokenInvalidException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(TokenInvalidException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.FORBIDDEN);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
	}
	
	/**
	 * Exepcion que se lanzará cuando el anuncio no se pueda crear
	 * @param ex
	 * @return mensaje correspondiente
	 */
	@ExceptionHandler(CrearAnuncioException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(CrearAnuncioException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.BAD_REQUEST);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	/**
	 * Exepcion que se lanzará cuando el anuncio con el id especificado no exista
	 * @param ex
	 * @return mensaje correspondiente
	 */
	@ExceptionHandler(AnuncioIdNotFoundException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(AnuncioIdNotFoundException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	
	/**
	 * Exepcion que se lanzará cuando el anuncio con el id especificado no exista en la lista donde lo buscamos
	 * @param ex
	 * @return mensaje correspondiente
	 */
	@ExceptionHandler(AnuncioIdNoEstaEnListaExcetion.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(AnuncioIdNoEstaEnListaExcetion ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	
	/**
	 * Exepcion que se lanzará cuando falta alguno de los parametros para realizar la busqueda
	 * @param ex
	 * @return mensaje correspondiente
	 */
	@ExceptionHandler(ParametrosInvalidosException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(ParametrosInvalidosException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	
	/**
	 * Exepcion que se lanzará cuando falta alguno de los parametros para realizar la busqueda
	 * @param ex
	 * @return mensaje correspondiente
	 */
	@ExceptionHandler(AnuncioYaExistenteException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(AnuncioYaExistenteException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.BAD_REQUEST);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	/**
	 * Exepcion que se lanzará cuando falta alguno de los parametros para realizar la busqueda
	 * @param ex
	 * @return mensaje correspondiente
	 */
	@ExceptionHandler(UsuarioIdNotFoundException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(UsuarioIdNotFoundException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	
	/**
	 * Exepcion que se lanzará cuando no exista un solicitante en un anuncio concreto
	 * @param ex
	 * @return mensaje correspondiente
	 */
	@ExceptionHandler(SolicitanteNoExisteEnAnuncioException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(SolicitanteNoExisteEnAnuncioException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	

}
