package com.facturacion.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String role; // ADMIN, CLIENT
    private int tipoUsuario; // 1: Admin/Empresa, 0: Cliente/Comprador

    // New fields for Registration
    private String nombre;
    private String cedula;
    private String direccion;
    private String telefono;
}
