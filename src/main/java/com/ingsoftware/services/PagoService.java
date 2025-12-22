package com.ingsoftware.services;

import com.ingsoftware.models.Pago;
import com.ingsoftware.models.ResponseRest;
import com.ingsoftware.repositories.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagoService {
    @Autowired
    private PagoRepository pagoRepository;

    public ResponseRest listarPagos() {
        List<Pago> pagos = pagoRepository.findAll();
        return new ResponseRest(pagos, "Lista de pagos", null);
    }

    public ResponseRest guardarPago(Pago pago) {
        Pago guardado = pagoRepository.save(pago);
        return new ResponseRest(guardado, "Pago guardado", null);
    }
}
