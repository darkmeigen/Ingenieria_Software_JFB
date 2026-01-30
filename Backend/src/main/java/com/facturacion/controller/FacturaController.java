package com.facturacion.controller;

import com.facturacion.dto.FacturaRequest;
import com.facturacion.model.Factura;
import com.facturacion.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@Tag(name = "Facturación", description = "Endpoints para gestión de facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @PostMapping
    @Operation(summary = "Emitir Factura", description = "Crea una nueva factura, descuenta stock y calcula totales")
    public ResponseEntity<Factura> crearFactura(@RequestBody FacturaRequest request) {
        Factura nuevaFactura = facturaService.crearFactura(request);
        return ResponseEntity.ok(nuevaFactura);
    }

    @GetMapping
    @Operation(summary = "Listar Facturas", description = "Obtiene el historial de todas las facturas")
    public ResponseEntity<List<Factura>> listarFacturas() {
        return ResponseEntity.ok(facturaService.listarTodas());
    }
}