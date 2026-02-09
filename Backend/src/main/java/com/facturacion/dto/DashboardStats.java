package com.facturacion.dto;

import lombok.Data;
import java.util.List;

@Data
public class DashboardStats {
    private Double totalVentas;
    private Double subTotal;
    private Double impuestos;
    private Double notasCredito; // Placeholder
    private List<MonthlySales> salesLast6Months;
    private List<ClientSales> topClients;
    private List<ProductSales> topProducts;

    @Data
    public static class MonthlySales {
        private String month;
        private Double total;
    }

    @Data
    public static class ClientSales {
        private String identificacion;
        private String nombre;
        private String telefono;
        private Double totalCompras;
    }

    @Data
    public static class ProductSales {
        private String nombre;
        private String categoria;
        private String marca;
        private Integer cantidad;
        private Double totalVentas;
    }
}
