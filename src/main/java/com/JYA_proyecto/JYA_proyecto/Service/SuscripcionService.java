package com.JYA_proyecto.JYA_proyecto.service;

import com.JYA_proyecto.JYA_proyecto.model.Suscripcion;
import java.util.List;

public interface SuscripcionService {
    
    // Obtener todas las suscripciones
    public List<Suscripcion> obtenerTodas();
    
    // Suscribir un email
    public boolean suscribir(String email);
    
    // Verificar si un email ya está suscrito
    public boolean existeSuscripcion(String email);
    
    // Eliminar suscripción
    public void eliminarSuscripcion(String email);
    
    // Contar suscripciones
    public long contarSuscripciones();
}