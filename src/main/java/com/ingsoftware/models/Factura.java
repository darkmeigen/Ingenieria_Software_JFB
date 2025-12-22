package com.ingsoftware.models;

import java.util.Date;
import java.util.List;

public class Factura {
    private Long id;
    private Date fecha;
    private Cliente cliente;
    private List<DetalleFactura> detalles;
    private Double total;
    private Pago pago;

    // Getters y setters
}
