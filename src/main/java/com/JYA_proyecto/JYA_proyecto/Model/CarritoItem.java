package com.JYA_proyecto.JYA_proyecto.model;

import java.math.BigDecimal;

public class CarritoItem {
    private Long productoId;
    private String nombre;
    private BigDecimal precioUnitario;
    private int cantidad;
    private String imagenUrl;

    public CarritoItem(Long productoId, String nombre, BigDecimal precioUnitario, int cantidad, String imagenUrl) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.imagenUrl = imagenUrl;
    }

    public Long getProductoId() { return productoId; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = Math.max(1, cantidad); }
    public String getImagenUrl() { return imagenUrl; }

    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
