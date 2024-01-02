package org.test.springboot.seccion5.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.test.springboot.seccion5.app.models.Cuenta;
import org.test.springboot.seccion5.app.repositories.ICuentaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IntegracionJpaTest {
    @Autowired
    ICuentaRepository cuentaRepository;

    @Test
    void testFindById() {
        Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Jaime",cuenta.orElseThrow().getPersona());
    }

    @Test
    void testFindByPersona() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Jaime");
        assertTrue(cuenta.isPresent());
        assertEquals("1000.00",cuenta.orElseThrow().getSaldo().toString());
    }

    @Test
    void testFindByPersonaKo() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Rod");
        assertThrows(NoSuchElementException.class, cuenta::orElseThrow );
    }

    @Test
    void testFindAll() {
        List<Cuenta> listCuentas = cuentaRepository.findAll();

        assertTrue(!listCuentas.isEmpty());
        assertTrue(listCuentas.size() > 0);
    }

    @Test
    void testCreate() {
        Cuenta nuevaCuenta = new Cuenta();
        nuevaCuenta.setPersona("Manolo");
        nuevaCuenta.setSaldo(new BigDecimal("1500"));

        cuentaRepository.save(nuevaCuenta);

        var cuenta = cuentaRepository.findByPersona("Manolo");
        assertTrue(cuenta.isPresent());
        Assertions.assertEquals(new BigDecimal("1500"), cuenta.orElseThrow().getSaldo());
    }

    @Test
    void testUpdate() {
        Cuenta cuentaUpdate = cuentaRepository.findById(1L).orElseThrow();
        cuentaUpdate.setPersona("Antonio");
        cuentaUpdate.setSaldo(new BigDecimal("500"));

        cuentaRepository.save(cuentaUpdate);

        var cuenta = cuentaRepository.findByPersona("Antonio");
        Assertions.assertEquals(1L,cuenta.orElseThrow().getId());
        Assertions.assertEquals("Antonio",cuenta.orElseThrow().getPersona());
        Assertions.assertEquals(new BigDecimal("500"),cuenta.orElseThrow().getSaldo());
    }

    @Test
    void testDelete() {
        Cuenta cuentaDel = cuentaRepository.findById(1L).orElseThrow();
        cuentaRepository.delete(cuentaDel);

        var cuenta = cuentaRepository.findById(1L);

        assertTrue(cuenta.isEmpty());
    }
}
