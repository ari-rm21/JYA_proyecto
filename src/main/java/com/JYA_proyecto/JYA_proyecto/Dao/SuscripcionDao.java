package com.JYA_proyecto.JYA_proyecto.dao;

import com.JYA_proyecto.JYA_proyecto.model.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuscripcionDao extends JpaRepository<Suscripcion, Long> {
    
    boolean existsByEmail(String email);
    
    void deleteByEmail(String email);
    
    Suscripcion findByEmail(String email);
}