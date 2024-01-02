package org.test.springboot.seccion5.app.services;

import org.test.springboot.seccion5.app.models.Cuenta;

import java.math.BigDecimal;

public interface ICuentaService {
    Cuenta findById(Long id);
    int revisarTotalTransferencias(Long bancoId);

    BigDecimal revisarSaldo(Long cuentaId);

    void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long bancoId);
}
