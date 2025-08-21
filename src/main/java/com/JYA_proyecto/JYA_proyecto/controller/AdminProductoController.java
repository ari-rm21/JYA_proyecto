package com.JYA_proyecto.JYA_proyecto.controller;

import com.JYA_proyecto.JYA_proyecto.dao.ProductoDao;
import com.JYA_proyecto.JYA_proyecto.dao.CategoriaDao;    
import com.JYA_proyecto.JYA_proyecto.model.Producto;
import com.JYA_proyecto.JYA_proyecto.model.Categoria;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/productos")
public class AdminProductoController {

    @Autowired private ProductoDao productoDao;
    @Autowired private CategoriaDao categoriaDao;  

    /** Disponibiliza SIEMPRE la lista de categorías para esta vista */
    @ModelAttribute("categorias")
    public List<Categoria> categorias() {
        return categoriaDao.findAll();
    }

    /** Formulario: crear */
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Producto p = new Producto();
        p.setCategoria(new Categoria());
        model.addAttribute("producto", p);
        return "admin/productos/form";
    }

    /** Formulario: editar */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Producto p = productoDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no existe"));
        if (p.getCategoria() == null) p.setCategoria(new Categoria());
        model.addAttribute("producto", p);
        return "admin/productos/form";
    }

    /** Guardar (crear/editar) */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") Producto producto,
                          BindingResult br,
                          RedirectAttributes ra) {
        if (br.hasErrors()) {
            return "admin/productos/form";
        }
        productoDao.save(producto);
        ra.addFlashAttribute("msgOk", "Producto guardado correctamente.");
        return "redirect:/tienda";
    }

    /** Eliminar */
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        if (productoDao.existsById(id)) {
            productoDao.deleteById(id);
            ra.addFlashAttribute("msgOk", "Producto eliminado.");
        } else {
            ra.addFlashAttribute("msgErr", "El producto ya no existe.");
        }
        return "redirect:/tienda";
    }
}
