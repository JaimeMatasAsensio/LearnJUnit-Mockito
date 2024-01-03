package org.test.springboot.seccion7.app.services;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.test.springboot.seccion7.app.models.Banco;
import org.test.springboot.seccion7.app.models.Cuenta;
import org.test.springboot.seccion7.app.repositories.IBancoRepository;
import org.test.springboot.seccion7.app.repositories.ICuentaRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CuentaServiceImpl implements ICuentaService {

    private ICuentaRepository cuentaRepository;

    private IBancoRepository bancoRepository;

    public CuentaServiceImpl(ICuentaRepository cuentaRepository, IBancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cuenta> findAll() {
        return cuentaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public Cuenta save(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        cuentaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public int revisarTotalTransferencias(Long bancoId) {
        return bancoRepository.findById(bancoId).orElseThrow().getTotalTransferencias();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal revisarSaldo(Long cuentaId) {
        return cuentaRepository.findById(cuentaId).orElseThrow().getSaldo();
    }

    @Override
    @Transactional
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
