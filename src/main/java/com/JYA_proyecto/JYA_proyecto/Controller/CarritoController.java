package com.JYA_proyecto.JYA_proyecto.controller;

import com.JYA_proyecto.JYA_proyecto.model.Carrito;
import com.JYA_proyecto.JYA_proyecto.service.CarritoService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;                      
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.math.BigDecimal;                   

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/drawer")
    public String drawer(Model model) {
        model.addAttribute("carrito", carritoService.getCarrito());
        return "fragments/carrito :: drawer";
    }

    /** Página completa “Ver carrito” */
    @GetMapping("/pagina")
    public String pagina(Model model) {
        Carrito c = carritoService.getCarrito();

        BigDecimal subtotal = c.getSubtotal();
        BigDecimal envio = c.getItems().isEmpty()
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(2500);
        BigDecimal total = subtotal.add(envio);

        model.addAttribute("carrito", c);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("envio", envio);
        model.addAttribute("total", total);
        model.addAttribute("carritoVacio", c.getItems().isEmpty());

        return "carrito/pagina";
    }

    @PostMapping("/agregar/{id}")
    public String agregar(@PathVariable("id") Long productoId,
                          @RequestParam(defaultValue = "1") int cantidad,
                          RedirectAttributes ra,
                          jakarta.servlet.http.HttpServletRequest req) {
        carritoService.agregar(productoId, cantidad);
        ra.addFlashAttribute("abrirCarrito", true);
        String ref = req.getHeader("Referer");
        return "redirect:" + (ref != null ? ref : "/tienda");
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable("id") Long productoId,
                             @RequestParam @Min(1) int cantidad,
                             RedirectAttributes ra) {
        carritoService.actualizar(productoId, cantidad);
        ra.addFlashAttribute("abrirCarrito", true);
        return "redirect:/carrito/pagina";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long productoId, RedirectAttributes ra) {
        carritoService.eliminar(productoId);
        ra.addFlashAttribute("abrirCarrito", true);
        return "redirect:/carrito/pagina";
    }

    @PostMapping("/limpiar")
    public String limpiar(RedirectAttributes ra) {
        carritoService.limpiar();
        ra.addFlashAttribute("abrirCarrito", true);
        return "redirect:/carrito/pagina";
    }
    @PostMapping("/finalizar")
public String finalizarCompra(RedirectAttributes ra) {
    carritoService.limpiar();


    ra.addFlashAttribute("compraOk", true);

    return "redirect:/carrito/pagina";
}
}
