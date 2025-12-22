package com.ingsoftware.controllers;

import com.ingsoftware.models.Cliente;
import com.ingsoftware.models.ResponseRest;
import com.ingsoftware.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseRest listarClientes() {
        return clienteService.listarClientes();
    }

    @PostMapping
    public ResponseRest guardarCliente(@RequestBody Cliente cliente) {
        return clienteService.guardarCliente(cliente);
    }
}
