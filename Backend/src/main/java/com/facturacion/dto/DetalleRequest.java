package com.facturacion.dto;

import lombok.Data;

@Data
public class DetalleRequest {
    private String productoId;
    private Integer cantidad;
}