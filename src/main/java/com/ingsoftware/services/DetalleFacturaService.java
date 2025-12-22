package com.ingsoftware.services;

import com.ingsoftware.models.DetalleFactura;
import com.ingsoftware.models.ResponseRest;
import com.ingsoftware.repositories.DetalleFacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleFacturaService {
    @Autowired
    private DetalleFacturaRepository detalleFacturaRepository;

    public ResponseRest listarDetalles() {
        List<DetalleFactura> detalles = detalleFacturaRepository.findAll();
        return new ResponseRest(detalles, "Lista de detalles de factura", null);
    }

    public ResponseRest guardarDetalle(DetalleFactura detalle) {
        DetalleFactura guardado = detalleFacturaRepository.save(detalle);
        return new ResponseRest(guardado, "Detalle guardado", null);
    }
}
