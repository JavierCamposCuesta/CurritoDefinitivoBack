package com.example.demo.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Categoria;
import com.example.demo.repository.CategoriaRepository;

/**
 * Servicio que recoge los métodos necesarios para realizar operaciones referentes a las categorias
 * @author javier
 *
 */
@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	/**
	 * Metodo para mostrar las categorias
	 * @return lista de categorias
	 */
	public List<Categoria> mostrarCategorias(){
		return categoriaRepository.findAll();
	}

}
