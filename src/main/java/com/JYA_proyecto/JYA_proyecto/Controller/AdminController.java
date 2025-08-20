package com.JYA_proyecto.JYA_proyecto.Controller;

import com.JYA_proyecto.JYA_proyecto.model.Usuario;
import com.JYA_proyecto.JYA_proyecto.service.UsuarioService;
import com.JYA_proyecto.JYA_proyecto.service.SuscripcionService;
import com.JYA_proyecto.JYA_proyecto.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SuscripcionService suscripcionService;
    
    @Autowired
    private ProductoService productoService;

    @GetMapping
    public String admin(Model model) {
        // Estadísticas para el dashboard
        model.addAttribute("totalUsuarios", usuarioService.getUsuarios().size());
        model.addAttribute("totalSuscripciones", suscripcionService.contarSuscripciones());
        model.addAttribute("totalProductos", productoService.obtenerTodos().size());
        return "admin/dashboard";
    }

    @GetMapping("/suscripciones")
    public String listarSuscripciones(Model model) {
        var suscripciones = suscripcionService.obtenerTodas();
        model.addAttribute("suscripciones", suscripciones);
        model.addAttribute("totalSuscripciones", suscripciones.size());
        return "admin/suscripciones";
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        var usuarios = usuarioService.getUsuarios();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        return "admin/usuarios";
    }

    @GetMapping("/usuarios/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/usuario-form";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        usuario = usuarioService.getUsuario(usuario);
        
        if (usuario == null) {
            return "redirect:/admin/usuarios";
        }
        
        model.addAttribute("usuario", usuario);
        return "admin/usuario-form";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario,
                               @RequestParam(required = false) String nuevaPassword,
                               RedirectAttributes redirectAttributes) {
        try {
            boolean esNuevo = (usuario.getIdUsuario() == null);
            
            if (esNuevo) {
                // Verificar que no exista el usuario
                if (usuarioService.existeUsuarioPorUsernameOCorreo(usuario.getUsername(), usuario.getCorreo())) {
                    redirectAttributes.addFlashAttribute("mensaje", "El usuario o correo ya existe");
                    redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                    return "redirect:/admin/usuarios/nuevo";
                }
            }

            // Manejar contraseña
            if (esNuevo || (nuevaPassword != null && !nuevaPassword.trim().isEmpty())) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String passwordToEncrypt = esNuevo ? usuario.getPassword() : nuevaPassword;
                usuario.setPassword(encoder.encode(passwordToEncrypt));
            } else if (!esNuevo) {
                // Mantener la contraseña existente
                Usuario usuarioExistente = usuarioService.getUsuario(usuario);
                usuario.setPassword(usuarioExistente.getPassword());
            }

            // Guardar usuario
            usuarioService.save(usuario, esNuevo);
            
            redirectAttributes.addFlashAttribute("mensaje", 
                esNuevo ? "Usuario creado exitosamente" : "Usuario actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(id);
            usuarioService.delete(usuario);
            
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/suscripciones/eliminar")
    public String eliminarSuscripcion(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            suscripcionService.eliminarSuscripcion(email);
            redirectAttributes.addFlashAttribute("mensaje", "Suscripción eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar suscripción: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/admin/suscripciones";
    }
}