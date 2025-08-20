package com.JYA_proyecto.JYA_proyecto.service;

import com.JYA_proyecto.JYA_proyecto.dao.UsuarioDao;
import com.JYA_proyecto.JYA_proyecto.model.Rol;
import com.JYA_proyecto.JYA_proyecto.model.Usuario;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    @Autowired private UsuarioDao usuarioDao;
    @Autowired private HttpSession session;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.findByUsername(username);
        if (usuario == null) throw new UsernameNotFoundException("No existe: " + username);

        session.setAttribute("usuarioImagen", usuario.getRutaImagen());
        session.setAttribute("usuarioNombre", usuario.getNombre() + " " + usuario.getApellidos());

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Rol r : usuario.getRoles()) {
            String nombre = r.getNombre();
            if (nombre != null && !nombre.startsWith("ROLE_")) nombre = "ROLE_" + nombre;
            authorities.add(new SimpleGrantedAuthority(nombre));
        }

        return new org.springframework.security.core.userdetails.User(
            usuario.getUsername(),
            usuario.getPassword(), // HASH BCrypt tal cual
            usuario.isActivo(), true, true, true,
            authorities
        );
    }
}
