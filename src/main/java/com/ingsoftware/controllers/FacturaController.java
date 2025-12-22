package com.ingsoftware.controllers;

import com.ingsoftware.models.Factura;
import com.ingsoftware.models.ResponseRest;
import com.ingsoftware.services.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facturas")
public class FacturaController {
    @Autowired
    private FacturaService facturaService;

    @PostMapping
    public ResponseRest crearFactura(@RequestBody Factura factura) {
        return facturaService.guardarFactura(factura);
    }
}
