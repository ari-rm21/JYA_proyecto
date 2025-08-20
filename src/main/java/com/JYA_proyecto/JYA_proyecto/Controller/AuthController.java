package com.JYA_proyecto.JYA_proyecto.Controller;

import com.JYA_proyecto.JYA_proyecto.model.Usuario;
import com.JYA_proyecto.JYA_proyecto.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(Usuario usuario, 
                                 RedirectAttributes redirectAttributes) {
        try {
            // Validar que no exista el usuario o correo
            if (usuarioService.existeUsuarioPorUsernameOCorreo(usuario.getUsername(), usuario.getCorreo())) {
                redirectAttributes.addFlashAttribute("mensaje", "El usuario o correo ya existe");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/registro";
            }

            // Encriptar contraseña
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            usuario.setPassword(encoder.encode(usuario.getPassword()));
            usuario.setActivo(true);

            // Guardar usuario con rol USER
            usuarioService.save(usuario, true);

            redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al registrar usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/registro";
        }
    }
}