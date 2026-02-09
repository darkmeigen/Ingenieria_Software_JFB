package com.facturacion.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "productos")
public class Producto {
    @Id
    private String id;

    @org.springframework.data.mongodb.core.index.Indexed(unique = true)
    private String codigo; // Auto-generated: C001, etc.

    private String nombre;
    private String descripcion;
    private String categoria;
    private String marca;
    private Double precio;
    private Integer stock;
}