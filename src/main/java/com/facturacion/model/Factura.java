package com.facturacion.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFactura;

    private LocalDateTime fecha;
    
    // Relación con Cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // Relación con DetalleFactura (Composición)
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleFactura> detalles = new ArrayList<>();

    private Double subtotal;
    private Double iva;
    private Double total;
    
    private String estado; // CREADA, ANULADA, ETC.

    // Método auxiliar para mantener la consistencia de la relación bidireccional
    public void agregarDetalle(DetalleFactura detalle) {
        detalles.add(detalle);
        detalle.setFactura(this);
    }
}