package com.ingsoftware.services;

import com.ingsoftware.models.Factura;
import com.ingsoftware.models.Producto;
import com.ingsoftware.models.ResponseRest;
import com.ingsoftware.repositories.FacturaRepository;
import com.ingsoftware.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacturaService {
    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private ProductoRepository productoRepository;

    public ResponseRest guardarFactura(Factura factura) {
        for (var detalle : factura.getDetalles()) {
            Producto producto = productoRepository.findById(detalle.getProducto().getId()).orElse(null);
            if (producto == null || producto.getStock() < detalle.getCantidad()) {
                return new ResponseRest(null, "No hay suficiente stock para el producto: " + (producto != null ? producto.getNombreProducto() : "Desconocido"), "Stock insuficiente");
            }
        }
        for (var detalle : factura.getDetalles()) {
            Producto producto = productoRepository.findById(detalle.getProducto().getId()).orElse(null);
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);
        }
        Factura facturaGuardada = facturaRepository.save(factura);
        return new ResponseRest(facturaGuardada, "Factura guardada correctamente", null);
    }
}
