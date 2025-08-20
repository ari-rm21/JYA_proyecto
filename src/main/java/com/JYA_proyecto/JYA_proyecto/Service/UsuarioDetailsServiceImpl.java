package com.JYA_proyecto.JYA_proyecto.service;

import com.JYA_proyecto.JYA_proyecto.dao.UsuarioDao;
import com.JYA_proyecto.JYA_proyecto.model.Rol;
import com.JYA_proyecto.JYA_proyecto.model.Usuario;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioDao usuarioDao;
    
    @Autowired
    private HttpSession session;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Se busca el usuario que tiene el username pasado por parámetro
        Usuario usuario = usuarioDao.findByUsername(username);

        // Se valida si se recuperó un usuario / sino lanza un error
        if (usuario == null) {
            throw new UsernameNotFoundException(username);
        }

        // Si estamos acá es porque si se recuperó un usuario...
        session.removeAttribute("usuarioImagen");
        session.removeAttribute("usuarioNombre");
        session.setAttribute("usuarioImagen", usuario.getRutaImagen());
        session.setAttribute("usuarioNombre", usuario.getNombre() + " " + usuario.getApellidos());

        // Se van a recuperar los roles del usuario y se crean los roles para Spring Security
        var roles = new ArrayList<GrantedAuthority>();
        for (Rol rol : usuario.getRoles()) {
            roles.add(new SimpleGrantedAuthority(rol.getNombre()));
        }
        
        // Se retorna un User (de tipo UserDetails)
        return new User(usuario.getUsername(), usuario.getPassword(), roles);
    }
}