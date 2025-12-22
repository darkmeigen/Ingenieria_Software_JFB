package com.ingsoftware.services;

import com.ingsoftware.models.Cliente;
import com.ingsoftware.models.ResponseRest;
import com.ingsoftware.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public ResponseRest listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return new ResponseRest(clientes, "Lista de clientes", null);
    }

    public ResponseRest guardarCliente(Cliente cliente) {
        Cliente guardado = clienteRepository.save(cliente);
        return new ResponseRest(guardado, "Cliente guardado", null);
    }
}
