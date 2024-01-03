package org.test.springboot.seccion6.app.services;

import org.springframework.stereotype.Service;
import org.test.springboot.seccion6.app.models.Cuenta;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface ICuentaService {

    List<Cuenta> findAll();

    Cuenta findById(Long id);

    Cuenta save(Cuenta cuenta);

    int revisarTotalTransferencias(Long bancoId);

    BigDecimal revisarSaldo(Long cuentaId);

    void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long bancoId);
}
