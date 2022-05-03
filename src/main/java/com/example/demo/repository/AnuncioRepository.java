package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Anuncio;
import com.example.demo.model.Usuario;

/**
 * Clase para conectar la api con la base de datos, en ella crearemos las peticiones necesarias
 * @author javier
 *
 */
@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Integer>{
	
    @Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false order by fecha_anuncio desc limit 6")
    List<Anuncio> getRecientes();
    
    //########## Consultas busqueda por termino
    
    @Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false AND (UPPER(anuncio.titulo) LIKE %?1% OR UPPER(anuncio.descripcion) LIKE %?1%) order by fecha_anuncio desc")
    List<Anuncio> getAnunciosPorTerminoNovedades(String termino);
    
    @Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false AND (UPPER(anuncio.titulo) LIKE %?1% OR UPPER(anuncio.descripcion) LIKE %?1%) order by precio asc")
    List<Anuncio> getAnunciosPorTerminoPrecioAscendente(String termino);
    
    @Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false AND (UPPER(anuncio.titulo) LIKE %?1% OR UPPER(anuncio.descripcion) LIKE %?1%) order by precio desc")
    List<Anuncio> getAnunciosPorTerminoPrecioDescendente(String termino);
    
    @Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false AND (UPPER(anuncio.titulo) LIKE %?1% OR UPPER(anuncio.descripcion) LIKE %?1%) order by fecha_anuncio asc")
    List<Anuncio> getAnunciosPorTerminoAntiguos(String termino);

    
    //########### Consultas busqueda por categorias
    
	@Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false AND anuncio.categoria = ?2 AND (UPPER(anuncio.titulo) LIKE %?1% OR UPPER(anuncio.descripcion) LIKE %?1%) order by fecha_anuncio desc")
    List<Anuncio> getAnunciosPorCategoriaNovedades(String termino, String categoria);
	
	@Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false AND anuncio.categoria = ?2 AND (UPPER(anuncio.titulo) LIKE %?1% OR UPPER(anuncio.descripcion) LIKE %?1%) order by precio asc")
    List<Anuncio> getAnunciosPorCategoriaPrecioAscendente(String termino, String categoria);
	
	@Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false AND anuncio.categoria = ?2 AND (UPPER(anuncio.titulo) LIKE %?1% OR UPPER(anuncio.descripcion) LIKE %?1%) order by precio desc")
    List<Anuncio> getAnunciosPorCategoriaPrecioDescendente(String termino, String categoria);
	
	@Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false AND anuncio.categoria = ?2 AND (UPPER(anuncio.titulo) LIKE %?1% OR UPPER(anuncio.descripcion) LIKE %?1%) order by fecha_anuncio asc")
    List<Anuncio> getAnunciosPorCategoriaAntiguos(String termino, String categoria);
	
	
	//############ Consultas busqueda por precio
	
	@Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false AND anuncio.precio >= ?2 AND anuncio.precio <= ?3  AND (UPPER(anuncio.titulo) LIKE %?1% OR UPPER(anuncio.descripcion) LIKE %?1%) order by fecha_anuncio desc")
    List<Anuncio> getAnunciosPorPrecioNovedades(String termino, int precioMin, int precioMax);
	
	@Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false AND anuncio.precio >= ?2 AND anuncio.precio <= ?3  AND (UPPER(anuncio.titulo) LIKE %?1% OR UPPER(anuncio.descripcion) LIKE %?1%) order by precio asc")
    List<Anuncio> getAnunciosPorPrecioAscendente(String termino, int precioMin, int precioMax);
	
	@Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false AND anuncio.precio >= ?2 AND anuncio.precio <= ?3  AND (UPPER(anuncio.titulo) LIKE %?1% OR UPPER(anuncio.descripcion) LIKE %?1%) order by precio desc")
    List<Anuncio> getAnunciosPorPrecioDescendiente(String termino, int precioMin, int precioMax);
	
	@Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false AND anuncio.precio >= ?2 AND anuncio.precio <= ?3  AND (UPPER(anuncio.titulo) LIKE %?1% OR UPPER(anuncio.descripcion) LIKE %?1%) order by fecha_anuncio asc")
    List<Anuncio> getAnunciosPorPrecioAntiguos(String termino, int precioMin, int precioMax);
	
	
	//############ Consultas busqueda por categoria y precio
	
	@Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false AND anuncio.categoria = ?2 AND anuncio.precio >= ?3 AND anuncio.precio <= ?4  AND (UPPER(anuncio.titulo) LIKE %?1% OR UPPER(anuncio.descripcion) LIKE %?1%) order by fecha_anuncio desc")
    List<Anuncio> getAnunciosPorCategoriaPrecioNovedades(String termino, String categoria,  int precioMin, int precioMax);
	
	@Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false AND anuncio.categoria = ?2 AND anuncio.precio >= ?3 AND anuncio.precio <= ?4  AND (UPPER(anuncio.titulo) LIKE %?1% OR UPPER(anuncio.descripcion) LIKE %?1%) order by precio asc")
    List<Anuncio> getAnunciosPorCategoriaPrecioAscendente(String termino, String categoria,  int precioMin, int precioMax);
	
	@Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false AND anuncio.categoria = ?2 AND anuncio.precio >= ?3 AND anuncio.precio <= ?4  AND (UPPER(anuncio.titulo) LIKE %?1% OR UPPER(anuncio.descripcion) LIKE %?1%) order by precio desc")
    List<Anuncio> getAnunciosPorCategoriaPrecioDescendente(String termino, String categoria,  int precioMin, int precioMax);
	
	@Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false AND anuncio.categoria = ?2 AND anuncio.precio >= ?3 AND anuncio.precio <= ?4  AND (UPPER(anuncio.titulo) LIKE %?1% OR UPPER(anuncio.descripcion) LIKE %?1%) order by fecha_anuncio asc")
    List<Anuncio> getAnunciosPorCategoriaPrecioAntiguos(String termino, String categoria,  int precioMin, int precioMax);

	
	//############ Consultas busqueda sin termino
	
	@Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false order by fecha_anuncio desc")
	List<Anuncio> getAnunciosSinTerminoNovedades();
	
	@Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false order by precio_anuncio asc")
	List<Anuncio> getAnunciosSinTerminoPrecioAscendente();
	
	@Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false order by precio_anuncio desc")
	List<Anuncio> getAnunciosSinTerminoPrecioDescendente();
	
	@Query(nativeQuery = true,value = "select * from anuncio where anuncio.finalizado = false order by fecha_anuncio asc")
	List<Anuncio> getAnunciosSinTerminoAntiguos();
	
	
	
	
	
	
	
	
	
	
	
	
	
}
