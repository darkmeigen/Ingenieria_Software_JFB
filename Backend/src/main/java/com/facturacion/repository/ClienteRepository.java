package com.facturacion.repository;

import com.facturacion.model.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, String> {
    java.util.Optional<Cliente> findByIdentificacion(String identificacion);

    // For Autocomplete (Name or ID)
    @org.springframework.data.mongodb.repository.Query("{ '$or': [ { 'identificacion': { '$regex': ?0, '$options': 'i' } }, { 'nombre': { '$regex': ?0, '$options': 'i' } } ] }")
    java.util.List<Cliente> buscarPorIdentificacionONombre(String regex);
}