package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer>{

}
