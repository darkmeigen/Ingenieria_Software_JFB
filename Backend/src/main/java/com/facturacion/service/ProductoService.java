package com.facturacion.service;

import com.facturacion.model.Producto;
import com.facturacion.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto guardarProducto(Producto producto) {
        if (producto.getCodigo() == null || producto.getCodigo().isEmpty()) {
            producto.setCodigo(generarCodigoUnico(producto.getNombre()));
        }
        return productoRepository.save(producto);
    }

    private String generarCodigoUnico(String nombre) {
        if (nombre == null || nombre.isEmpty())
            return "P001";

        // Strategy: Initials or First Letter
        String[] words = nombre.trim().split("\\s+");
        String prefix;
        if (words.length > 1) {
            StringBuilder sb = new StringBuilder();
            for (String w : words)
                if (!w.isEmpty())
                    sb.append(w.charAt(0));
            prefix = sb.toString().toUpperCase();
        } else {
            // Single word. User wants specific logic, but defaulting to First Letter
            // If collision logic needed, we might need more advanced rules.
            // For now: First Letter.
            prefix = nombre.substring(0, 1).toUpperCase();

            // Heuristic for "Corrector" -> "CTOR" if needed?
            // For now sticking to Initials/FirstChar + Sequence
        }

        // Find max sequence
        List<Producto> existing = productoRepository.findByCodigoStartingWith(prefix);
        int max = 0;
        for (Producto p : existing) {
            String code = p.getCodigo();
            if (code != null && code.startsWith(prefix)) {
                try {
                    String numPart = code.substring(prefix.length());
                    int val = Integer.parseInt(numPart);
                    if (val > max)
                        max = val;
                } catch (NumberFormatException e) {
                    // ignore malformed codes
                }
            }
        }

        return String.format("%s%03d", prefix, max + 1);
    }

    public Producto obtenerProducto(String id) {
        return productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // New Search Method
    public List<Producto> buscarProductos(String query) {
        return productoRepository.buscarPorNombreOCodigo(query);
    }

    public void eliminarProducto(String id) {
        productoRepository.deleteById(id);
    }
}
