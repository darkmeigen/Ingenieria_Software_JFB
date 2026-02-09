package com.facturacion.model;

import lombok.Data;

@Data
public class Pago {
    private String metodo; // EFECTIVO, TRANSFERENCIA, TARJETA_DEBITO, TARJETA_CREDITO
    private Double monto;
}
