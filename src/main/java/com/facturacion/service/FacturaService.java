package com.facturacion.service;

import com.facturacion.dto.FacturaRequest;
import com.facturacion.model.*;
import com.facturacion.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FacturaService {

    @Autowired private FacturaRepository facturaRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private ClienteRepository clienteRepository;

    @Transactional // Garantiza que si falla algo (ej. stock), no se guarde la factura
    public Factura crearFactura(FacturaRequest request) {
        
        // 1. Buscar Cliente
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + request.getClienteId()));

        Factura factura = new Factura();
        factura.setCliente(cliente);
        factura.setFecha(LocalDateTime.now());
        factura.setEstado("CREADA"); // Estado inicial según diagrama de estados

        double subtotalAcumulado = 0.0;

        // 2. Procesar Productos
        for (var item : request.getProductos()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + item.getProductoId()));

            // Validar Stock (RF02)
            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            // Descontar Stock (RF06)
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            // Crear Detalle
            DetalleFactura detalle = new DetalleFactura();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio() * item.getCantidad());

            factura.agregarDetalle(detalle);
            subtotalAcumulado += detalle.getSubtotal();
        }

        // 3. Calcular Totales
        factura.setSubtotal(subtotalAcumulado);
        factura.setIva(subtotalAcumulado * 0.15); // Ejemplo IVA 15%
        factura.setTotal(subtotalAcumulado * 1.15);

        // 4. Guardar
        return facturaRepository.save(factura);
    }

    public List<Factura> listarTodas() {
        return facturaRepository.findAll();
    }
}