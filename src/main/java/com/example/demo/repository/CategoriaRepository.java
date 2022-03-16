package com.example.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Categoria;

/**
 * Clase repositorio para conectar la clase categoria con la base de datos
 * @author javier
 *
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, String>{

}
