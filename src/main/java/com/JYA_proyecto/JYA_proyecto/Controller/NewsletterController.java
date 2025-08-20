package com.JYA_proyecto.JYA_proyecto.Controller;

import com.JYA_proyecto.JYA_proyecto.service.SuscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/newsletter")
public class NewsletterController {

    @Autowired
    private SuscripcionService suscripcionService;

    @PostMapping("/suscribir")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> suscribir(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (email == null || email.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "El email es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }

            if (suscripcionService.existeSuscripcion(email)) {
                response.put("success", false);
                response.put("message", "Este email ya está suscrito");
                return ResponseEntity.ok(response);
            }

            boolean suscrito = suscripcionService.suscribir(email);
            
            if (suscrito) {
                response.put("success", true);
                response.put("message", "¡Gracias por suscribirte! Te mantendremos informado sobre nuestras novedades.");
            } else {
                response.put("success", false);
                response.put("message", "Error al procesar la suscripción. Inténtalo de nuevo.");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno. Inténtalo más tarde.");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/suscribir-form")
    public String suscribirForm(@RequestParam String email, 
                               RedirectAttributes redirectAttributes) {
        try {
            if (suscripcionService.existeSuscripcion(email)) {
                redirectAttributes.addFlashAttribute("mensaje", "Este email ya está suscrito");
                redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            } else if (suscripcionService.suscribir(email)) {
                redirectAttributes.addFlashAttribute("mensaje", "¡Gracias por suscribirte!");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "Error al suscribirse");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error interno");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/";
    }
}