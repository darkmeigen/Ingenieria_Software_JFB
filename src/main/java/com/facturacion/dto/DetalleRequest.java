package com.facturacion.dto;

import lombok.Data;

@Data
public class DetalleRequest {
    private Long productoId;
    private Integer cantidad;
}