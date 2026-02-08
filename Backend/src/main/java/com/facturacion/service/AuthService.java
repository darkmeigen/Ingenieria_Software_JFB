package com.facturacion.service;

import com.facturacion.dto.LoginRequest;
import com.facturacion.model.User;
import com.facturacion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(request.getPassword())) {
                return user;
            }
        }
        throw new RuntimeException("Credenciales inválidas");
    }

    @Autowired
    private com.facturacion.repository.ClienteRepository clienteRepository;

    public User register(User user) {
        // Validation (Basic)
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        // Save User
        User savedUser = userRepository.save(user);

        // If Role is CLIENT, create a Client entity automatically
        if ("CLIENT".equalsIgnoreCase(user.getRole())) {
            com.facturacion.model.Cliente cliente = new com.facturacion.model.Cliente();
            cliente.setNombre(user.getNombre());
            cliente.setIdentificacion(user.getCedula());
            cliente.setDireccion(user.getDireccion());
            cliente.setEmail(user.getUsername()); // Assuming username is email
            clienteRepository.save(cliente);
        }

        return savedUser;
    }
}
