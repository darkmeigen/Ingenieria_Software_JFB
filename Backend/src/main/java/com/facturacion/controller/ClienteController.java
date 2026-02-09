package com.facturacion.controller;

import com.facturacion.model.Cliente;
import com.facturacion.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> listar() {
        return clienteService.listarClientes();
    }

    @PostMapping
    public Cliente guardar(@RequestBody Cliente cliente) {
        return clienteService.guardarCliente(cliente);
    }

    @GetMapping("/buscar/{identificacion}")
    public ResponseEntity<Cliente> buscarPorCedula(@PathVariable String identificacion) {
        return clienteService.buscarPorIdentificacion(identificacion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/autocomplete")
    public List<Cliente> autocomplete(@RequestParam String query) {
        return clienteService.buscarClientes(query);
    }

    @GetMapping("/{id}")
    public Cliente obtener(@PathVariable String id) {
        return clienteService.obtenerCliente(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
