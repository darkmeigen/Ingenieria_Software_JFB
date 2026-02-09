package com.facturacion.service;

import com.facturacion.model.Cliente;
import com.facturacion.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    @Autowired
    private com.facturacion.repository.UserRepository userRepository;

    public Cliente guardarCliente(Cliente cliente) {
        if (cliente.getIdentificacion() != null
                && !userRepository.findByUsername(cliente.getIdentificacion()).isPresent()) {
            com.facturacion.model.User newUser = new com.facturacion.model.User();
            newUser.setUsername(cliente.getIdentificacion());
            newUser.setPassword("cliente1234");
            newUser.setRole("CLIENT");
            newUser.setTipoUsuario(0);
            newUser.setNombre(cliente.getNombre());
            newUser.setCedula(cliente.getIdentificacion());
            newUser.setDireccion(cliente.getDireccion());
            newUser.setTelefono(cliente.getTelefono());
            userRepository.save(newUser);
        }
        return clienteRepository.save(cliente);
    }

    public java.util.Optional<Cliente> buscarPorIdentificacion(String identificacion) {
        return clienteRepository.findByIdentificacion(identificacion);
    }

    public java.util.List<Cliente> buscarClientes(String query) {
        // Search by identification or name (contains, case insensitive)
        return clienteRepository.buscarPorIdentificacionONombre(query);
    }

    public Cliente obtenerCliente(String id) {
        return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    public void eliminarCliente(String id) {
        clienteRepository.deleteById(id);
    }
}
