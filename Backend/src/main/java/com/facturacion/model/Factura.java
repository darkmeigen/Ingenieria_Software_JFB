package com.facturacion.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "facturas")
public class Factura {
    @Id
    private String id; // Changed to String for MongoDB ObjectId

    private LocalDateTime fecha;

    @DBRef
    private Cliente cliente;

    private List<DetalleFactura> detalles = new ArrayList<>();

    private Double subtotal;
    private Double iva;
    private Double total;

    private String estado;

    public void agregarDetalle(DetalleFactura detalle) {
        detalles.add(detalle);
    }
}