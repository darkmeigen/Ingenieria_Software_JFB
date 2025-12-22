package com.ingsoftware.controllers;

import com.ingsoftware.models.Pago;
import com.ingsoftware.models.ResponseRest;
import com.ingsoftware.services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagos")
public class PagoController {
    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseRest listarPagos() {
        return pagoService.listarPagos();
    }

    @PostMapping
    public ResponseRest guardarPago(@RequestBody Pago pago) {
        return pagoService.guardarPago(pago);
    }
}
