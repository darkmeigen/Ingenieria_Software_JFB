package com.facturacion.dto;

import com.facturacion.model.Pago;
import lombok.Data;
import java.util.List;

@Data
public class FacturaRequest {
    private String clienteId;
    private List<DetalleRequest> productos;
    private List<Pago> pagos;
}