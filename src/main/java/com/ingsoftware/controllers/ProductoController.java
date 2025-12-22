package com.ingsoftware.controllers;

import com.ingsoftware.models.Producto;
import com.ingsoftware.models.ResponseRest;
import com.ingsoftware.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseRest listarProductos() {
        return productoService.listarProductos();
    }

    @PostMapping
    public ResponseRest guardarProducto(@RequestBody Producto producto) {
        return productoService.guardarProducto(producto);
    }
}
