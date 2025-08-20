package com.JYA_proyecto.JYA_proyecto.config;

import com.JYA_proyecto.JYA_proyecto.dao.UsuarioDao;
import com.JYA_proyecto.JYA_proyecto.model.Rol;
import com.JYA_proyecto.JYA_proyecto.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SeedUsersConfig {
//ES UN SOLO ARCHIVO PARA PRUEBAS DE USUARIOS!!
    @Bean
    CommandLineRunner seedAndCheck(UsuarioDao dao, PasswordEncoder enc) {
        return args -> {
            // --- ADMIN ---
            Usuario admin = dao.findByUsername("admin");
            if (admin == null) {
                admin = new Usuario();
                admin.setUsername("admin");
                admin.setNombre("Admin");
                admin.setApellidos("Site");
                admin.setCorreo("admin@site.test");
                admin.setActivo(true);
                admin.setPassword(enc.encode("admin123"));
                Rol ra = new Rol();
                ra.setNombre("ROLE_ADMIN");
                ra.setUsuario(admin);
                admin.setRoles(new ArrayList<>(List.of(ra)));
                dao.save(admin);
                System.out.println(">> Creado admin/admin123");
            } else {
                // fuerza password y rol correctos
                admin.setPassword(enc.encode("admin123"));
                ensureRole(admin, "ROLE_ADMIN");
                dao.save(admin);
                System.out.println(">> Actualizado admin/admin123");
            }

            // --- USUARIO1 ---
            Usuario u1 = dao.findByUsername("usuario1");
            if (u1 == null) {
                u1 = new Usuario();
                u1.setUsername("usuario1");
                u1.setNombre("Usuario");
                u1.setApellidos("Normal");
                u1.setCorreo("usuario1@site.test");
                u1.setActivo(true);
                u1.setPassword(enc.encode("user124"));
                Rol rr = new Rol();
                rr.setNombre("ROLE_USER");
                rr.setUsuario(u1);
                u1.setRoles(new ArrayList<>(List.of(rr)));
                dao.save(u1);
                System.out.println(">> Creado usuario1/user124");
            } else {
                u1.setPassword(enc.encode("user124"));
                ensureRole(u1, "ROLE_USER");
                dao.save(u1);
                System.out.println(">> Actualizado usuario1/user124");
            }

            // --- VERIFICACIONES ---
            admin = dao.findByUsername("admin");
            u1 = dao.findByUsername("usuario1");
            System.out.println("ADMIN len=" + admin.getPassword().length() +
                               " start=" + admin.getPassword().substring(0,4) +
                               " matches=" + enc.matches("admin123", admin.getPassword()));
            System.out.println("USER1 len=" + u1.getPassword().length() +
                               " start=" + u1.getPassword().substring(0,4) +
                               " matches=" + enc.matches("user124", u1.getPassword()));
        };
    }

    private void ensureRole(Usuario u, String roleName) {
        if (u.getRoles() == null) u.setRoles(new ArrayList<>());
        boolean has = u.getRoles().stream().anyMatch(r -> roleName.equals(r.getNombre()));
        if (!has) {
            Rol r = new Rol();
            r.setNombre(roleName);
            r.setUsuario(u);
            u.getRoles().add(r);
        }

    }
}
