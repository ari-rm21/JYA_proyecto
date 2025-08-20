package com.JYA_proyecto.JYA_proyecto.Controller;

import com.JYA_proyecto.JYA_proyecto.model.Producto;
import com.JYA_proyecto.JYA_proyecto.service.CarritoService;
import com.JYA_proyecto.JYA_proyecto.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/carrito")
public class CarritoController {
    
    @Autowired
    private CarritoService carritoService;
    
    @Autowired
    private ProductoService productoService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String mostrarCarrito(Model model) {
        model.addAttribute("items", carritoService.obtenerItems());
        model.addAttribute("total", carritoService.calcularTotal());
        model.addAttribute("cantidadTotal", carritoService.obtenerCantidadTotal());
        return "carrito";
    }
    
    @PostMapping("/agregar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String agregarProducto(@RequestParam Long productoId, 
                                 @RequestParam(defaultValue = "1") Integer cantidad,
                                 RedirectAttributes redirectAttributes) {
        
        Optional<Producto> producto = productoService.obtenerPorId(productoId);
        
        if (producto.isPresent()) {
            if (producto.get().getStock() >= cantidad) {
                carritoService.agregarItem(producto.get(), cantidad);
                redirectAttributes.addFlashAttribute("mensaje", "Producto agregado al carrito exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "Stock insuficiente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            }
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Producto no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/tienda";
    }
    
    @PostMapping("/actualizar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String actualizarCantidad(@RequestParam Long productoId, 
                                   @RequestParam Integer cantidad,
                                   RedirectAttributes redirectAttributes) {
        
        if (cantidad > 0) {
            Optional<Producto> producto = productoService.obtenerPorId(productoId);
            if (producto.isPresent() && producto.get().getStock() >= cantidad) {
                carritoService.actualizarCantidad(productoId, cantidad);
                redirectAttributes.addFlashAttribute("mensaje", "Cantidad actualizada");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "Stock insuficiente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            }
        } else {
            carritoService.eliminarItem(productoId);
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado del carrito");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        }
        
        return "redirect:/carrito";
    }
    
    @PostMapping("/eliminar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String eliminarProducto(@RequestParam Long productoId,
                                 RedirectAttributes redirectAttributes) {
        carritoService.eliminarItem(productoId);
        redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado del carrito");
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/carrito";
    }
    
    @PostMapping("/limpiar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String limpiarCarrito(RedirectAttributes redirectAttributes) {
        carritoService.limpiarCarrito();
        redirectAttributes.addFlashAttribute("mensaje", "Carrito vaciado");
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/carrito";
    }
}