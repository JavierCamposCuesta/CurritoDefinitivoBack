package com.example.demo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


/*
 * Clase usuario
 */
@Entity
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String email;
	
	//Evita que el campo password se incluya en el JSON de respuesta
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private String nombre;
	private String apellidos;
	private String telefono;
	private LocalDate fechaNacimiento;
	private String ubicacion;
	
	@OneToMany(
//			cascade = CascadeType.ALL, orphanRemoval = true
			)
	@JsonIgnore
	private List<Anuncio>listaFavoritos = new ArrayList<Anuncio>();
	
	@OneToMany(
//			cascade = CascadeType.ALL 
			)
	@JsonIgnore
	private List<Anuncio>listaOfertados = new ArrayList<Anuncio>();
	
	@OneToMany(
//			cascade = CascadeType.ALL, orphanRemoval = true
			)
	@JsonIgnore
	private List<Anuncio>listaTerminados = new ArrayList<Anuncio>();
	
	@OneToMany(
//			cascade = CascadeType.ALL
			)
	@JsonIgnore
	private List<Anuncio>listaDemandados = new ArrayList<Anuncio>();
	
	@OneToMany(
//			cascade = CascadeType.ALL, orphanRemoval = true
			)
	@JsonIgnore
	private List<Anuncio>listaRealizados = new ArrayList<Anuncio>();
	
	@OneToMany(
			cascade = CascadeType.ALL, orphanRemoval = true
			)
	@JsonIgnore
	private List<Comentario>comentariosPendientes = new ArrayList<>();
	
	@OneToMany(
//			cascade = CascadeType.ALL, 
			orphanRemoval = true
			)
	@JsonIgnore
	private List<Comentario>listaComentarios = new ArrayList<>();
	
	
//	private List<Chat>listaChats = new ArrayList<Chat>();
	private String fotoPerfil;
	
	public Usuario() {
		
	}
	
	public Usuario(int id) {
			this.id = id;
		}

	

	public Usuario(String email, String password, String nombre, String apellidos, String telefono,
			 String ubicacion, LocalDate fechaNacimiento) {
		super();
		this.email = email;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.telefono = telefono;
		this.fechaNacimiento = fechaNacimiento;
		this.ubicacion = ubicacion;
	}
	
	
	
	



	public Usuario(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}



	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public List<Anuncio> getListaFavoritos() {
		return listaFavoritos;
	}

	public void setListaFavoritos(List<Anuncio> listaFavoritos) {
		this.listaFavoritos = listaFavoritos;
	}

	public List<Anuncio> getListaOfertados() {
		return listaOfertados;
	}

	public void setListaOfertados(List<Anuncio> listaOfertados) {
		this.listaOfertados = listaOfertados;
	}
	
	

	public List<Anuncio> getListaTerminados() {
		return listaTerminados;
	}



	public void setListaTerminados(List<Anuncio> listaTerminados) {
		this.listaTerminados = listaTerminados;
	}



	public List<Anuncio> getListaDemandados() {
		return listaDemandados;
	}

	public void setListaDemandados(List<Anuncio> listaDemandados) {
		this.listaDemandados = listaDemandados;
	}

	
	public List<Anuncio> getListaRealizados() {
		return listaRealizados;
	}



	public void setListaRealizados(List<Anuncio> listaRealizados) {
		this.listaRealizados = listaRealizados;
	}



	public String getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(String fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public List<Comentario> getComentariosPendientes() {
		return comentariosPendientes;
	}



	public void setComentariosPendientes(List<Comentario> comentariosPendientes) {
		this.comentariosPendientes = comentariosPendientes;
	}



	public List<Comentario> getListaComentarios() {
		return listaComentarios;
	}



	public void setListaComentarios(List<Comentario> listaComentarios) {
		this.listaComentarios = listaComentarios;
	}



	@Override
	public int hashCode() {
		return Objects.hash(email);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(email, other.email);
	}
	
	
	
	
	
	
	
	

}
