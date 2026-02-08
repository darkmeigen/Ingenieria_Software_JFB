package com.facturacion.model;

import lombok.Data;

@Data
public class DetalleFactura {
    // No @Id needed as it will be embedded
    private Producto producto; // Embedding product snapshot or reference
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}