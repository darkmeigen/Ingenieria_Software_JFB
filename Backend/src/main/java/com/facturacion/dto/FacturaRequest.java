package com.facturacion.dto;

import lombok.Data;
import java.util.List;

@Data
public class FacturaRequest {
    private String clienteId;
    private List<DetalleRequest> productos;
}