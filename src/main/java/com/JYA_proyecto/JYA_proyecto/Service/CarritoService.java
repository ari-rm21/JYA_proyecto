package com.JYA_proyecto.JYA_proyecto.service;

import com.JYA_proyecto.JYA_proyecto.dao.CarritoRegistroDao;
import com.JYA_proyecto.JYA_proyecto.dao.ProductoDao;
import com.JYA_proyecto.JYA_proyecto.dao.UsuarioDao;
import com.JYA_proyecto.JYA_proyecto.model.CarritoRegistro;
import com.JYA_proyecto.JYA_proyecto.model.Producto;
import com.JYA_proyecto.JYA_proyecto.model.Usuario;
import com.JYA_proyecto.JYA_proyecto.model.Carrito;
import com.JYA_proyecto.JYA_proyecto.model.CarritoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Service
@Primary
@Transactional
public class CarritoService {

    @Autowired private CarritoRegistroDao carritoDao;
    @Autowired private UsuarioDao usuarioDao;
    @Autowired private ProductoDao productoDao;

    private Long uid() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        String username = (a != null) ? a.getName() : null;
        Usuario u = usuarioDao.findByUsername(username);
        if (u == null) throw new IllegalStateException("Usuario no autenticado");
        return u.getIdUsuario(); 
    }

    @Transactional(readOnly = true)
    public Carrito getCarrito() {
        Long userId = uid();
        List<CarritoRegistro> filas = carritoDao.findAllByUsuario(userId);

        Carrito view = new Carrito();
        for (CarritoRegistro f : filas) {
            Producto p = f.getProducto();
  
            Long productoId = p.getId();
            view.add(new CarritoItem(
                    productoId,
                    p.getNombre(),
                    p.getPrecio(),
                    f.getCantidad(),
                    p.getImagenUrl()
            ));
        }
        return view;
    }

    public void agregar(Long productoId, int cantidad) {
        Long userId = uid();
        var existente = carritoDao.findRow(userId, productoId);
        if (existente.isPresent()) {
            var row = existente.get();
            row.setCantidad(row.getCantidad() + Math.max(1, cantidad));
        } else {
            Producto p = productoDao.findById(productoId).orElseThrow();
            Usuario u = usuarioDao.findById(userId).orElseThrow();

            CarritoRegistro row = new CarritoRegistro();
            row.setUsuario(u);
            row.setProducto(p);
            row.setCantidad(Math.max(1, cantidad));
            carritoDao.save(row);
        }
    }

    public void actualizar(Long productoId, int cantidad) {
        Long userId = uid();
        var row = carritoDao.findRow(userId, productoId) 
                .orElseThrow(() -> new IllegalArgumentException("No existe en carrito"));
        row.setCantidad(Math.max(1, cantidad));
    }

    public void eliminar(Long productoId) {
        carritoDao.deleteRow(uid(), productoId); 
    }

    public void limpiar() {
        carritoDao.deleteAllForUsuario(uid()); 
    }

    @Transactional(readOnly = true)
    public int getTotalCantidad() {
        return carritoDao.totalCantidad(uid());
    }

    @Transactional(readOnly = true)
    public BigDecimal getSubtotal() {
        return carritoDao.findAllByUsuario(uid()).stream()
                .map(r -> r.getProducto().getPrecio().multiply(BigDecimal.valueOf(r.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

 
}
