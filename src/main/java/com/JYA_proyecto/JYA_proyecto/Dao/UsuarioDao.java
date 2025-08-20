package com.JYA_proyecto.JYA_proyecto.dao;

import com.JYA_proyecto.JYA_proyecto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioDao extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
    Usuario findByUsernameOrCorreo(String username, String correo);
    boolean existsByUsernameOrCorreo(String username, String correo);
}
