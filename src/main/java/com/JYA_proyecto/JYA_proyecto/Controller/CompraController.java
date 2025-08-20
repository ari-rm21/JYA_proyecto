package com.JYA_proyecto.JYA_proyecto.Controller;

import com.JYA_proyecto.JYA_proyecto.model.CarritoItem;
import com.JYA_proyecto.JYA_proyecto.service.CarritoService;
import com.JYA_proyecto.JYA_proyecto.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/checkout")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class CompraController {
    
    @Autowired
    private CarritoService carritoService;
    
    @Autowired
    private ProductoService productoService;
    
    @GetMapping
    public String mostrarCheckout(Model model, Authentication authentication) {
        if (carritoService.estaVacio()) {
            return "redirect:/carrito";
        }
        
        List<CarritoItem> items = carritoService.obtenerItems();
        BigDecimal subtotal = carritoService.calcularTotal();
        BigDecimal impuestos = subtotal.multiply(new BigDecimal("0.13")); // 13% IVA
        BigDecimal envio = new BigDecimal("5.00");
        BigDecimal total = subtotal.add(impuestos).add(envio);
        
        model.addAttribute("items", items);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("impuestos", impuestos);
        model.addAttribute("envio", envio);
        model.addAttribute("total", total);
        model.addAttribute("usuarioLogueado", authentication.getName());
        
        return "checkout";
    }
    
    @PostMapping("/procesar")
    public String procesarCompra(@RequestParam String nombre,
                               @RequestParam String email,
                               @RequestParam String telefono,
                               @RequestParam String direccion,
                               @RequestParam String metodoPago,
                               @RequestParam(required = false) String numeroTarjeta,
                               @RequestParam(required = false) String cvv,
                               @RequestParam(required = false) String fechaExpiracion,
                               @RequestParam(required = false) String telefonoSinpe,
                               RedirectAttributes redirectAttributes,
                               Authentication authentication) {
        
        if (carritoService.estaVacio()) {
            redirectAttributes.addFlashAttribute("mensaje", "El carrito está vacío");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/carrito";
        }
        
        try {
            // Validar datos según método de pago
            if ("tarjeta".equals(metodoPago)) {
                if (numeroTarjeta == null || numeroTarjeta.trim().isEmpty() ||
                    cvv == null || cvv.trim().isEmpty() ||
                    fechaExpiracion == null || fechaExpiracion.trim().isEmpty()) {
                    throw new RuntimeException("Faltan datos de la tarjeta");
                }
            } else if ("sinpe".equals(metodoPago)) {
                if (telefonoSinpe == null || telefonoSinpe.trim().isEmpty()) {
                    throw new RuntimeException("Falta el número de teléfono para SINPE Móvil");
                }
            }
            
            // Reducir stock de productos
            for (CarritoItem item : carritoService.obtenerItems()) {
                boolean stockReducido = productoService.reducirStock(item.getProductoId(), item.getCantidad());
                if (!stockReducido) {
                    throw new RuntimeException("Stock insuficiente para el producto: " + item.getNombre());
                }
            }
            
            // Simular procesamiento del pago
            Thread.sleep(2000); // Simular tiempo de procesamiento
            
            // Limpiar carrito después de compra exitosa
            carritoService.limpiarCarrito();
            
            redirectAttributes.addFlashAttribute("mensaje", "¡Compra realizada exitosamente!");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            redirectAttributes.addFlashAttribute("detallesCompra", true);
            redirectAttributes.addFlashAttribute("nombreCliente", nombre);
            redirectAttributes.addFlashAttribute("emailCliente", email);
            redirectAttributes.addFlashAttribute("usuarioLogueado", authentication.getName());
            
            return "redirect:/checkout/confirmacion";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al procesar la compra: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/checkout";
        }
    }
    
    @GetMapping("/confirmacion")
    public String mostrarConfirmacion(Model model, Authentication authentication) {
        model.addAttribute("usuarioLogueado", authentication.getName());
        return "confirmacion-compra";
    }
}