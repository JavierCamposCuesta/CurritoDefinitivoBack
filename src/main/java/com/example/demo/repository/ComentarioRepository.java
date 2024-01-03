package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Anuncio;
import com.example.demo.model.Comentario;
import com.example.demo.model.Usuario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer>{
	
	@Query(nativeQuery = true,value = "select * from comentario where comentario.usuario_comentado_id = ?1")
    List<Comentario> getOpiniones(int idUsuario);
	
	@Query(nativeQuery = true,value = "select * from comentario where comentario.usuario_comentador_id = ?1 AND comentario.realizado = false")                                 
    List<Comentario> getNotificaciones(int idUsuario);
	
	@Query(nativeQuery = true,value = "select * from comentario where comentario.usuario_comentador_id = ?1 AND comentario.realizado = false")                                 
    List<Comentario> getOpinionesPendientes(int idUsuario);

}
