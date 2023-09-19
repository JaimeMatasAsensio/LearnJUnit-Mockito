package org.jmatasa.junit5_app.ejemplos.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {
    private Cuenta cuenta;
    private String nombre;
    private BigDecimal saldo;
    @BeforeEach
    void setUpTest(){
        nombre = "Jaime Matas Asensio";
        saldo = new BigDecimal("156987.123");
        cuenta = new Cuenta(saldo,nombre);
    }

    @Test
    @DisplayName("Cuenta getPersona OK")
    void nombreCuentaOK() {
        assertEquals("Jaime Matas Asensio", cuenta.getPersona());
    }

    @Test
    @DisplayName("Cuenta getPersona KO")
    void nombreCuentaKO() {
        assertNotEquals("Jaime", cuenta.getPersona());
    }

    @Test
    @DisplayName("Cuenta ContructorOK")
    void constructorCuentaOK() {
        Cuenta cuenta = new Cuenta(new BigDecimal("15478.6978"), "Jaime");
        assertTrue(cuenta.getPersona() != null && cuenta.getSaldo() != null ? true : false);

    }

    @Test
    @DisplayName("Cuenta contructorKO")
    void constructorCuentaK0() {
        Cuenta cuenta = new Cuenta();
        assertTrue(cuenta.getPersona() != null && cuenta.getSaldo() != null ? false : true);
    }

    @Test
    @DisplayName("Test saldo cuenta mayor a 0")
    void testCuentaSaldo(){
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

}