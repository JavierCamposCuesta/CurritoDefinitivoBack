package com.example.demo.model;


import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Clase comentario
 * @author javier
 *
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Comentario{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToOne()
	private Usuario usuarioComentado;
	
	@OneToOne()
	private Usuario usuarioComentador;
	
	private String textoComentario;
	private LocalDate fecha;
	private int puntuacionEstrellas;
	
	private boolean realizado = false;
	
	private boolean realizaste = false;
	
	@OneToOne()
	private Anuncio anuncio;
	
	public Comentario () {
		
	}

	public Comentario(Usuario usuarioComentado, Usuario usuarioComentador, LocalDate fecha) {
		super();
		this.usuarioComentado = usuarioComentado;
		this.usuarioComentador = usuarioComentador;
		this.fecha = fecha;
//		this.anuncio = anuncio;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Usuario getUsuarioComentado() {
		return usuarioComentado;
	}

	public void setUsuarioComentado(Usuario usuarioComentado) {
		this.usuarioComentado = usuarioComentado;
	}

	public Usuario getUsuarioComentador() {
		return usuarioComentador;
	}

	public void setUsuarioComentador(Usuario usuarioComentador) {
		this.usuarioComentador = usuarioComentador;
	}

	public String getTextoComentario() {
		return textoComentario;
	}

	public void setTextoComentario(String textoComentario) {
		this.textoComentario = textoComentario;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Anuncio getAnuncio() {
		return anuncio;
	}

	public void setAnuncio(Anuncio anuncio) {
		this.anuncio = anuncio;
	}

	public int getPuntuacionEstrellas() {
		return puntuacionEstrellas;
	}

	public void setPuntuacionEstrellas(int puntuacionEstrellas) {
		this.puntuacionEstrellas = puntuacionEstrellas;
	}

	public boolean isRealizado() {
		return realizado;
	}

	public void setRealizado(boolean realizado) {
		this.realizado = realizado;
	}

	public boolean isRealizaste() {
		return realizaste;
	}

	public void setRealizaste(boolean realizaste) {
		this.realizaste = realizaste;
	}
	
	
	
	
	
	
	
	

}
