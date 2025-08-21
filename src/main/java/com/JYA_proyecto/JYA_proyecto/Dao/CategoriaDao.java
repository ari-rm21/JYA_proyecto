package com.JYA_proyecto.JYA_proyecto.dao;

import com.JYA_proyecto.JYA_proyecto.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaDao extends JpaRepository<Categoria, Long> {
    Categoria findByNombre(String nombre);
//    List<Categoria> findAllByOrderByNombreAsc();
}
