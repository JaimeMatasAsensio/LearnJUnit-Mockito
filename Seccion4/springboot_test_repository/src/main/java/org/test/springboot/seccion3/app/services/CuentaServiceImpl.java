package org.test.springboot.seccion3.app.services;

import org.test.springboot.seccion3.app.models.Banco;
import org.test.springboot.seccion3.app.models.Cuenta;
import org.test.springboot.seccion3.app.repositories.IBancoRepository;
import org.test.springboot.seccion3.app.repositories.ICuentaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public class CuentaServiceImpl implements ICuentaService {

    private ICuentaRepository cuentaRepository;

    private IBancoRepository bancoRepository;

    public CuentaServiceImpl(ICuentaRepository cuentaRepository, IBancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id).orElseThrow();
    }

    @Override
    public int revisarTotalTransferencias(Long bancoId) {
        return bancoRepository.findById(bancoId).orElseThrow().getTotalTransferencias();
    }

    @Override
    public BigDecimal revisarSaldo(Long cuentaId) {
        return cuentaRepository.findById(cuentaId).orElseThrow().getSaldo();
    }

    @Override
    public void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long bancoId) {
        Cuenta cuentaOrigen = cuentaRepository.findById(numCuentaOrigen).orElseThrow();
        Cuenta cuentaDestino = cuentaRepository.findById(numCuentaDestino).orElseThrow();
        Banco banco = bancoRepository.findById(bancoId).orElseThrow();

        cuentaOrigen.debito(monto);
        cuentaRepository.save(cuentaOrigen);

        cuentaDestino.credito(monto);
        cuentaRepository.save(cuentaDestino);

        int totalTransferencias = banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencias);
        bancoRepository.save(banco);

    }
}
