package com.facturacion.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "productos")
public class Producto {
    @Id
    private String id; // Changed to String for MongoDB ObjectId

    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
}