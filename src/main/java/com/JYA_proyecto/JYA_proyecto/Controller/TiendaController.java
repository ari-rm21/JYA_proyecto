package com.JYA_proyecto.JYA_proyecto.controller;

import com.JYA_proyecto.JYA_proyecto.service.ProductoService;
import com.JYA_proyecto.JYA_proyecto.service.CategoriaService;
import com.JYA_proyecto.JYA_proyecto.model.Producto;
import com.JYA_proyecto.JYA_proyecto.model.Categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TiendaController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    //  Página principal (index)
    @GetMapping({"/", "/index"})
    public String verIndex(Model model) {
        List<Producto> productos = productoService.obtenerTodos();
        model.addAttribute("productos", productos);

        return "index"; 
    }

    //  Mostrar todos los productos activos en tienda
    @GetMapping("/tienda")
    public String verTienda(Model model) {
        List<Producto> productos = productoService.obtenerTodos();
        List<Categoria> categorias = categoriaService.listarTodas();

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("categoriaSeleccionada", "Todos");

        return "tienda"; 
    }

    //  Mostrar productos filtrados por categoría
    @GetMapping("/tienda/categoria/{nombre}")
    public String verPorCategoria(@PathVariable String nombre, Model model) {
        List<Producto> productos = productoService.obtenerPorCategoria(nombre);
        List<Categoria> categorias = categoriaService.listarTodas();

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("categoriaSeleccionada", nombre);

        return "tienda";
    }
}
