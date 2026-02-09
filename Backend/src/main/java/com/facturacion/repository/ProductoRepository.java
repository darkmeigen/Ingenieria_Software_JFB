package com.facturacion.repository;

import com.facturacion.model.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends MongoRepository<Producto, String> {
    java.util.Optional<Producto> findByCodigo(String codigo);

    @org.springframework.data.mongodb.repository.Query("{ '$or': [ { 'nombre': { '$regex': ?0, '$options': 'i' } }, { 'codigo': { '$regex': ?0, '$options': 'i' } } ] }")
    java.util.List<Producto> buscarPorNombreOCodigo(String regex);

    java.util.List<Producto> findByCodigoStartingWith(String prefix);
}