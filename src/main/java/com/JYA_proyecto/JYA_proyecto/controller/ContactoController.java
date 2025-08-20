package com.JYA_proyecto.JYA_proyecto.controller;

import com.JYA_proyecto.JYA_proyecto.model.MensajeContacto;
import com.JYA_proyecto.JYA_proyecto.dao.MensajeContactoDao;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class ContactoController {

    @Autowired
    private MensajeContactoDao dao;

  @GetMapping("/contacto")
public String verFormulario(Model model) {
    if (!model.containsAttribute("mensaje")) {
        model.addAttribute("mensaje", new MensajeContacto());
    }
    return "contacto";
}

@PostMapping("/contacto")
public String enviar(@Valid @ModelAttribute("mensaje") MensajeContacto mensaje,
                     BindingResult br,
                     RedirectAttributes ra) {
    if (br.hasErrors()) {
        ra.addFlashAttribute("org.springframework.validation.BindingResult.mensaje", br);
        ra.addFlashAttribute("mensaje", mensaje);
        return "redirect:/contacto";
    }

    mensaje.setFechaEnvio(LocalDateTime.now());
    dao.save(mensaje);
    ra.addFlashAttribute("ok", true);
    return "redirect:/contacto";
}

}
