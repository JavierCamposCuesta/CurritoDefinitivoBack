package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.error.AnuncioIdNotFoundException;
import com.example.demo.error.ApiError;
import com.example.demo.error.EmailEnUsoException;
import com.example.demo.error.EmailNotFoundException;
import com.example.demo.error.LoginInvalidException;
import com.example.demo.error.TokenInvalidException;
import com.example.demo.error.UsuarioIdNotFoundException;
import com.example.demo.model.Anuncio;
import com.example.demo.model.LoginCredentials;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JWTUtil;
import com.example.demo.service.EmailService;
import com.example.demo.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@CrossOrigin(origins = "https://javiercamposcuesta.github.io")
@CrossOrigin(origins = "*")
@RestController
public class AuthController {
	
	@Autowired private UsuarioRepository usuarioRepository;
    @Autowired private JWTUtil jwtUtil;
    @Autowired private AuthenticationManager authManager;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EmailService emailService;
    
    @Autowired
	private UsuarioService usuarioService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    /**
     * Metodo para registrar usuarios
     * @param user
     * @return Token generado || email ya en uso
     */
    @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody Usuario user){
    	if(usuarioRepository.findByEmail(user.getEmail()).orElse(null)==null) {
    		String encodedPass = passwordEncoder.encode(user.getPassword());
    		user.setPassword(encodedPass);

    		user = usuarioRepository.save(user);
    		String token = jwtUtil.generateToken(user.getEmail());
    		return Collections.singletonMap("jwt_token", token);
    	}
    	else {
    		throw new EmailEnUsoException();
    	}
    }
    
    /**
     * Metodo para loguear usuario
     * @param body
     * @return token generado || credenciales incorrectas
     */
    @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody LoginCredentials body){
        try {
        	logger.trace("##### log de prueba");
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());

            authManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(body.getEmail());
            return Collections.singletonMap("jwt_token", token);
        }catch (AuthenticationException authExc){
        	logger.info("se ha producido un error");
        	throw new LoginInvalidException();
//            throw new RuntimeException("Invalid Login Credentials");
        }
    }
    
    /**
	 * Este método recibe un email por parámetro, comprueba si el email existe y devuelve el usuario al que pertenece,
	 * en caso contrario devuelve null
	 * @param email
	 * @return true | false
	 */
	@GetMapping("usuario")
    public ResponseEntity<Boolean> usuarioPorEmail(String email){
		if(usuarioService.usuarioPorEmail(email) != null) {
			return ResponseEntity.ok(true);
//			return ResponseEntity.ok(usuarioService.usuarioPorEmail(email));
		}
		else {
//			throw new EmailNotFoundException();
			return ResponseEntity.ok(false);
		}
		
    }
	
	/**
     * Este metodo envia un codigo de verificacion al correo del usuario que se intenta registrar
     * @param email Email del usuario que intenta registrarse
     * @return CodVerificacion que se genera
     * @throws MessagingException
     */
    @PostMapping("verification")
    public ResponseEntity<Integer> emailVerify(@RequestBody String email) throws MessagingException{
    	
		return ResponseEntity.status(HttpStatus.CREATED).body(this.emailService.enviarCodVerificacion(email));
    	
    }
    
    /**
	 * Este metodo recibe unos parámetros para actualizar la contraseña
	 * 
	 * @return usuarioEditado
	 */
	@PutMapping("/profile/updatePass")
	public ResponseEntity<Usuario> updatePass(@RequestParam String passwordOld,@RequestParam String passwordNew,
			@RequestParam String passwordNew2) {
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (email != null && usuarioRepository.findByEmail(email).orElse(null) != null) {
			try {
	            UsernamePasswordAuthenticationToken authInputToken =
	                    new UsernamePasswordAuthenticationToken(email, passwordOld);

	            authManager.authenticate(authInputToken);

	            return new ResponseEntity<Usuario>(usuarioService.editPass(passwordNew, passwordNew2, email), HttpStatus.CREATED);
	        }catch (AuthenticationException authExc){
	        	//nota cambiar exception
	        	throw new LoginInvalidException();
	        }
				
			
		} else {
			throw new TokenInvalidException();
		}
	}
	
	/**
	 * Metodo para buscar un usuario a traves de un id que recibimos como parámetro
	 * @param idProfile
	 * @return Usuario en caso de encontrarlo
	 */
	@GetMapping("/usuario/{id}")
	public ResponseEntity<Usuario> mostrarAnuncio(@PathVariable(value="id")int idProfile){
			
			if( usuarioRepository.existsById(idProfile)) {
				return ResponseEntity.ok(usuarioService.mostrarUsuario(idProfile));
			}
			else {
				throw new UsuarioIdNotFoundException(idProfile);
			}
				}
	/**
	 * Metodo para mostrar la exepcion de Login invalid
	 * @param ex
	 * @return mensaje de excepcion
	 */
	@ExceptionHandler(LoginInvalidException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(LoginInvalidException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	
	/**
	 * Metodo para mostrar la exepcion de email en uso
	 * @param ex
	 * @return mensaje de excepcion
	 */
	@ExceptionHandler(EmailEnUsoException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(EmailEnUsoException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	
	/**
	 * Metodo para mostrar la exepcion de email no encontrado
	 * @param ex
	 * @return mensaje de excepcion
	 */
	@ExceptionHandler(EmailNotFoundException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(EmailNotFoundException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	

}
