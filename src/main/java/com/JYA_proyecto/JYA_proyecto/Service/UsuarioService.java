package com.JYA_proyecto.JYA_proyecto.service;

import com.JYA_proyecto.JYA_proyecto.model.Usuario;
import java.util.List;

public interface UsuarioService {

    List<Usuario> getUsuarios();

    Usuario getUsuario(Usuario usuario);

    Usuario getUsuarioPorUsername(String username);



    Usuario getUsuarioPorUsernameOCorreo(String username, String correo);

    boolean existeUsuarioPorUsernameOCorreo(String username, String correo);

    void save(Usuario usuario, boolean crearRolUser);

    void delete(Usuario usuario);
}
