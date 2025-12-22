package com.ingsoftware.controllers;

import com.ingsoftware.models.DetalleFactura;
import com.ingsoftware.models.ResponseRest;
import com.ingsoftware.services.DetalleFacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/detalles")
public class DetalleFacturaController {
    @Autowired
    private DetalleFacturaService detalleFacturaService;

    @GetMapping
    public ResponseRest listarDetalles() {
        return detalleFacturaService.listarDetalles();
    }

    @PostMapping
    public ResponseRest guardarDetalle(@RequestBody DetalleFactura detalle) {
        return detalleFacturaService.guardarDetalle(detalle);
    }
}
