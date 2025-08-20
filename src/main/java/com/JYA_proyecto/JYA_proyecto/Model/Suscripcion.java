package com.JYA_proyecto.JYA_proyecto.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "suscripciones")
public class Suscripcion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 150)
    private String email;
    
    @Column(name = "fecha_alta", nullable = false)
    private LocalDateTime fechaAlta;
    
    // Constructores
    public Suscripcion() {
        this.fechaAlta = LocalDateTime.now();
    }
    
    public Suscripcion(String email) {
        this.email = email;
        this.fechaAlta = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }
}