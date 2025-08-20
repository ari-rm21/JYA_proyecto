package com.JYA_proyecto.JYA_proyecto.service;

import com.JYA_proyecto.JYA_proyecto.model.Usuario;
import java.util.List;

public interface UsuarioService {

    List<Usuario> getUsuarios();

    Usuario getUsuario(Usuario usuario);

    Usuario getUsuarioPorUsername(String username);

    // ❌ RECOMENDACIÓN: elimina este método porque usas contraseñas con BCrypt
    // y ya no puedes buscar por username+password en BD.
    // Usuario getUsuarioPorUsernameYPassword(String username, String password);

    Usuario getUsuarioPorUsernameOCorreo(String username, String correo);

    boolean existeUsuarioPorUsernameOCorreo(String username, String correo);

    void save(Usuario usuario, boolean crearRolUser);

    void delete(Usuario usuario);
}
