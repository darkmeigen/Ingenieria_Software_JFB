package com.facturacion.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "clientes")
public class Cliente {
    @Id
    private String id; // Changed to String for MongoDB ObjectId

    private String nombre;
    private String identificacion; // RUC o Cédula
    private String direccion;
    private String telefono;
    private String email;
}