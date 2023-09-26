package org.jmatasa.junit5_app.ejemplos.models;

import org.jmatasa.junit5_app.ejemplos.exceptions.DineroInsuficienteException;
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
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getPersona() != null && cuenta.getSaldo() != null);

    }

    @Test
    @DisplayName("Cuenta contructorKO")
    void constructorCuentaK0() {
        Cuenta cuenta = new Cuenta();
        assertTrue(cuenta.getPersona() == null || cuenta.getSaldo() == null);
    }

    @Test
    @DisplayName("Test saldo cuenta mayor a 0")
    void testCuentaSaldo(){
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Test Referencia de cuenta")
    void testReferenciaCuenta() {
        Cuenta cuentaA = new Cuenta(new BigDecimal("123.456"),"Manolo");
        Cuenta cuentaB = new Cuenta(new BigDecimal("123.456"),"Manolo");

        assertEquals(cuentaA,cuentaB);
    }

    @Test
    @DisplayName("Test para metodo debito")
    void testDebitoCuenta() {
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(156887.123, cuenta.getSaldo().doubleValue());
        assertEquals("156887.123", cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Test para metodo debito")
    void testCreditoCuenta() {
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(157087.123, cuenta.getSaldo().doubleValue());
        assertEquals("157087.123", cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Test para excepcion Insuficiente dinero")
    void testDineroInsuficienteException() {
        Exception ex = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal("2000000"));
        },"Dinero insuficiente");
        String actual =ex.getMessage();
        String esperado ="Dinero insuficiente";
        assertEquals(esperado, actual);

    }
}