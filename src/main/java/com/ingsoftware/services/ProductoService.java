package com.ingsoftware.services;

import com.ingsoftware.models.Producto;
import com.ingsoftware.models.ResponseRest;
import com.ingsoftware.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public ResponseRest listarProductos() {
        List<Producto> productos = productoRepository.findAll();
        return new ResponseRest(productos, "Lista de productos", null);
    }

    public ResponseRest guardarProducto(Producto producto) {
        Producto guardado = productoRepository.save(producto);
        return new ResponseRest(guardado, "Producto guardado", null);
    }
}
