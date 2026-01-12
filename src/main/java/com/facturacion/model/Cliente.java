package com.facturacion.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    private String nombre;
    private String identificacion; // RUC o Cédula
    private String direccion;
    private String email;
}