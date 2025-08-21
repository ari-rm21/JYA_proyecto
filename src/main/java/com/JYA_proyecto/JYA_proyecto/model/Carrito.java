package com.JYA_proyecto.JYA_proyecto.model;

import java.math.BigDecimal;
import java.util.*;

public class Carrito {
    private final Map<Long, CarritoItem> items = new LinkedHashMap<>();

    public Collection<CarritoItem> getItems() {
        return items.values();
    }

    public int getTotalCantidad() {
        return items.values().stream().mapToInt(CarritoItem::getCantidad).sum();
    }

    public BigDecimal getSubtotal() {
        return items.values().stream()
                .map(CarritoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void add(CarritoItem item) {
        CarritoItem existente = items.get(item.getProductoId());
        if (existente == null) {
            items.put(item.getProductoId(), item);
        } else {
            existente.setCantidad(existente.getCantidad() + item.getCantidad());
        }
    }

    public void updateCantidad(Long productoId, int cantidad) {
        CarritoItem i = items.get(productoId);
        if (i != null) i.setCantidad(cantidad);
    }

    public void remove(Long productoId) {
        items.remove(productoId);
    }

    public void clear() {
        items.clear();
    }
}
