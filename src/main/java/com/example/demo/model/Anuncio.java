package com.example.demo.model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Clase anuncio 
 * @author javier
 *
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Anuncio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String titulo;
	private double precio;
	
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "Text")
    private String descripcion;
	
//	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER,orphanRemoval = true)
//	private List<String>listaImagenes = new ArrayList<>();
	
    @OneToOne
    private File file;
	
	private String categoria;
	
	@ManyToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JsonBackReference
	private List<Usuario>listaSolicitantes = new ArrayList<>();
	
	private boolean finalizado = false;
	private LocalDateTime fechaAnuncio;
	private LocalDateTime fechaFin;
	private String tipoPrecio;
	private String ubicacion;
	
	private boolean enFavoritos = false;
	

	
	@ManyToOne(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	private Usuario autorAnuncio;
	
	public Anuncio() {
		
	}

	public Anuncio(String titulo, String categoria,double precio, String tipoPrecio, String descripcion) {
		super();
		this.titulo = titulo;
		this.precio = precio;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.tipoPrecio = tipoPrecio;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	
//	public List<String> getListaImagenes() {
//		return listaImagenes;
//	}
//
//	public void setListaImagenes(List<String> listaImagenes) {
//		this.listaImagenes = listaImagenes;
//	}

	

	public String getCategoria() {
		return categoria;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public List<Usuario> getListaSolicitantes() {
		return listaSolicitantes;
	}

	public void setListaSolicitantes(List<Usuario> listaSolicitantes) {
		this.listaSolicitantes = listaSolicitantes;
	}

	public boolean isFinalizado() {
		return finalizado;
	}

	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}

	public LocalDateTime getFechaAnuncio() {
		
		return fechaAnuncio;
		

		 
        
        
	}

	public void setFechaAnuncio(LocalDateTime fechaAnuncio) {
		this.fechaAnuncio = fechaAnuncio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getTipoPrecio() {
		return tipoPrecio;
	}

	public void setTipoPrecio(String tipoPrecio) {
		this.tipoPrecio = tipoPrecio;
	}

	public Usuario getAutorAnuncio() {
		return autorAnuncio;
	}

	public void setAutorAnuncio(Usuario autorAnuncio) {
		this.autorAnuncio = autorAnuncio;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public boolean isEnFavoritos() {
		return enFavoritos;
	}

	public void setEnFavoritos(boolean enFavoritos) {
		this.enFavoritos = enFavoritos;
	}
	
	


	
	
	
	
	
	

//	@Override
//	public int hashCode() {
//		return Objects.hash(id);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Anuncio other = (Anuncio) obj;
//		return id == other.id;
//	}
	
	
	
	
	
	

}
