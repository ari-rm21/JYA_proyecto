package com.JYA_proyecto.JYA_proyecto.config;

import com.JYA_proyecto.JYA_proyecto.model.Carrito;
import com.JYA_proyecto.JYA_proyecto.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class LayoutModelAdvice {

    @Autowired
    private CarritoService carritoService;

    @ModelAttribute("carrito")
    public Carrito provideCarrito(Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return new Carrito();
        }
        try {
            return carritoService.getCarrito();
        } catch (Exception e) {
            return new Carrito();
        }
    }
}
