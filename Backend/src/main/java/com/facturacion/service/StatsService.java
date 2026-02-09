package com.facturacion.service;

import com.facturacion.dto.DashboardStats;
import com.facturacion.model.DetalleFactura;
import com.facturacion.model.Factura;
import com.facturacion.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Locale;

@Service
public class StatsService {

    @Autowired
    private FacturaRepository facturaRepository;

    public DashboardStats calculateStats() {
        List<Factura> allFacturas = facturaRepository.findAll();
        DashboardStats stats = new DashboardStats();

        // 1. Totals
        double totalVentas = allFacturas.stream().mapToDouble(f -> f.getTotal() != null ? f.getTotal() : 0).sum();
        double subTotal = allFacturas.stream().mapToDouble(f -> f.getSubtotal() != null ? f.getSubtotal() : 0).sum();
        double impuestos = allFacturas.stream().mapToDouble(f -> f.getIva() != null ? f.getIva() : 0).sum();

        stats.setTotalVentas(totalVentas);
        stats.setSubTotal(subTotal);
        stats.setImpuestos(impuestos);
        stats.setNotasCredito(0.0); // Mock

        // 2. Monthly Sales (Last 6 Months)
        Map<Month, Double> salesByMonth = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            Month m = now.minusMonths(i).getMonth();
            salesByMonth.put(m, 0.0);
        }

        for (Factura f : allFacturas) {
            if (f.getFecha() != null) {
                Month m = f.getFecha().getMonth();
                if (salesByMonth.containsKey(m)) {
                    salesByMonth.put(m, salesByMonth.get(m) + (f.getTotal() != null ? f.getTotal() : 0));
                }
            }
        }

        List<DashboardStats.MonthlySales> monthlySalesList = new ArrayList<>();
        salesByMonth.forEach((k, v) -> {
            DashboardStats.MonthlySales ms = new DashboardStats.MonthlySales();
            ms.setMonth(k.getDisplayName(TextStyle.FULL, new Locale("es", "ES")));
            ms.setTotal(v);
            monthlySalesList.add(ms);
        });
        stats.setSalesLast6Months(monthlySalesList);

        // 3. Top Clients
        Map<String, DashboardStats.ClientSales> clientMap = new HashMap<>();
        for (Factura f : allFacturas) {
            if (f.getCliente() != null) {
                String clienteId = f.getCliente().getId();
                clientMap.putIfAbsent(clienteId, new DashboardStats.ClientSales());
                DashboardStats.ClientSales cs = clientMap.get(clienteId);
                cs.setIdentificacion(f.getCliente().getIdentificacion());
                cs.setNombre(f.getCliente().getNombre());
                cs.setTelefono(f.getCliente().getTelefono());
                cs.setTotalCompras((cs.getTotalCompras() != null ? cs.getTotalCompras() : 0)
                        + (f.getTotal() != null ? f.getTotal() : 0));
            }
        }
        List<DashboardStats.ClientSales> topClients = clientMap.values().stream()
                .sorted((c1, c2) -> Double.compare(c2.getTotalCompras(), c1.getTotalCompras()))
                .limit(5)
                .collect(Collectors.toList());
        stats.setTopClients(topClients);

        // 4. Top Products
        Map<String, DashboardStats.ProductSales> productMap = new HashMap<>();
        for (Factura f : allFacturas) {
            for (DetalleFactura d : f.getDetalles()) {
                if (d.getProducto() != null) {
                    String prodId = d.getProducto().getId();
                    productMap.putIfAbsent(prodId, new DashboardStats.ProductSales());
                    DashboardStats.ProductSales ps = productMap.get(prodId);
                    ps.setNombre(d.getProducto().getNombre());
                    ps.setCategoria(d.getProducto().getCategoria());
                    ps.setMarca(d.getProducto().getMarca() != null ? d.getProducto().getMarca() : "Genérica");
                    ps.setCantidad((ps.getCantidad() != null ? ps.getCantidad() : 0) + d.getCantidad());
                    ps.setTotalVentas((ps.getTotalVentas() != null ? ps.getTotalVentas() : 0) + d.getSubtotal());
                }
            }
        }
        List<DashboardStats.ProductSales> topProducts = productMap.values().stream()
                .sorted((p1, p2) -> Double.compare(p2.getTotalVentas(), p1.getTotalVentas()))
                .limit(5)
                .collect(Collectors.toList());
        stats.setTopProducts(topProducts);

        return stats;
    }
}
