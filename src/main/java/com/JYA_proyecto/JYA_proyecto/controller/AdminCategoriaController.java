package com.JYA_proyecto.JYA_proyecto.controller;

import com.JYA_proyecto.JYA_proyecto.dao.CategoriaDao;
import com.JYA_proyecto.JYA_proyecto.model.Categoria;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categorias")
// @PreAuthorize("hasRole('ADMIN')")  // si usas seguridad por método
public class AdminCategoriaController {

    @Autowired private CategoriaDao categoriaDao;

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "admin/categorias/form";   // <- vista que creamos abajo
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        var cat = categoriaDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La categoría no existe"));
        model.addAttribute("categoria", cat);
        return "admin/categorias/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("categoria") Categoria categoria,
                          BindingResult br,
                          RedirectAttributes ra) {
        if (br.hasErrors()) {
            return "admin/categorias/form";
        }
        categoriaDao.save(categoria);
        ra.addFlashAttribute("msgOk", "Categoría guardada correctamente.");
        return "redirect:/tienda"; // <- al volver, el sidebar se recarga con la nueva categoría
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        if (categoriaDao.existsById(id)) {
            categoriaDao.deleteById(id);
            ra.addFlashAttribute("msgOk", "Categoría eliminada.");
        } else {
            ra.addFlashAttribute("msgErr", "La categoría ya no existe.");
        }
        return "redirect:/tienda";
    }
}
