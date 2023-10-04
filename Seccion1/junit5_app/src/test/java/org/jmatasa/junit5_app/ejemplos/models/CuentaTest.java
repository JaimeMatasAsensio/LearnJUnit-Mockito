package org.jmatasa.junit5_app.ejemplos.models;

import org.jmatasa.junit5_app.ejemplos.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {
    private Cuenta cuentaOrigen, cuentaDestino;

    private Banco banco;
    private String nombre;
    private BigDecimal saldo;
    @BeforeEach
    void setUpTest(){
        nombre = "Jaime Matas Asensio";
        saldo = new BigDecimal("156987.123");
        banco = new Banco("Santander");
        cuentaOrigen = new Cuenta(saldo,nombre,banco);
        cuentaDestino = new Cuenta(saldo,nombre,banco);

    }

    @Test
    @DisplayName("Cuenta getPersona OK")
    void nombreCuentaOK() {
        assertEquals("Jaime Matas Asensio", cuentaOrigen.getPersona(),"El nombre no puede ser null");
    }

    @Test
    @DisplayName("Cuenta getPersona KO")
    void nombreCuentaKO() {
        assertNotEquals("Jaime", cuentaOrigen.getPersona(),"No es el nombre esperado");
    }

    @Test
    @DisplayName("Cuenta ContructorOK")
    void constructorCuentaOK() {
        Cuenta cuenta = new Cuenta(new BigDecimal("15478.6978"), "Jaime");
        assertNotNull(cuenta.getSaldo(),"El saldo no puede ser null");
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
        assertFalse(cuentaOrigen.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuentaOrigen.getSaldo().compareTo(BigDecimal.ZERO) > 0);
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
        cuentaOrigen.debito(new BigDecimal(100));
        assertNotNull(cuentaOrigen.getSaldo());
        assertEquals(156887.123, cuentaOrigen.getSaldo().doubleValue());
        assertEquals("156887.123", cuentaOrigen.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Test para metodo debito")
    @Disabled //Notacion JUnit para deshabilitar un test
    void testCreditoCuenta() {
        cuentaOrigen.credito(new BigDecimal(100));

        assertAll(
                ()->assertNotNull(cuentaOrigen.getSaldo(),()->"El saldo no puede ser null"),
                ()->assertEquals(157087.123, cuentaOrigen.getSaldo().doubleValue(),
                        ()->"No se ha realizado la tranferencia desde la cuenta origen"),
                ()->assertEquals("157087.123", cuentaOrigen.getSaldo().toPlainString(),
                        ()->"No se ha realizado la tranferencia desde la cuenta origen"));


        //Prueba con assertAll -> Todos fallan
        fail(); //Metodo JUnit para forzar el fallo
        assertAll(
                ()->assertNotNull(null,()->"El saldo no puede ser null. Se espera: " + cuentaOrigen.getSaldo().toPlainString()),
                ()->assertEquals(157087.23, cuentaOrigen.getSaldo().doubleValue(),
                        ()->"No se ha realizado la credito para la cuenta origen. Se espera: " + cuentaOrigen.getSaldo().toPlainString()),
                ()->assertEquals("157087.13", cuentaOrigen.getSaldo().toPlainString(),
                        ()->"No se ha realizado la credito para la cuenta origen. Se espera: " + cuentaOrigen.getSaldo().toPlainString()));

    }

    @Test
    @DisplayName("Test para excepcion Insuficiente dinero")
    void testDineroInsuficienteException() {
        Exception ex = assertThrows(DineroInsuficienteException.class, () -> {
            cuentaOrigen.debito(new BigDecimal("2000000"));
        },"Dinero insuficiente");
        String actual =ex.getMessage();
        String esperado ="Dinero insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    @DisplayName("Test Transferir dinero entre cuentas") //156987.123
    void testTransferirDineroCuentas() {
        banco.transferir(cuentaOrigen,cuentaDestino, new BigDecimal("1000.00"));
        assertEquals("155987.123",cuentaOrigen.getSaldo().toPlainString());
        assertEquals("157987.123",cuentaDestino.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Test relacion entre cuentas") //156987.123
    void testRelacionBancoCuenta() {
        banco.addCuenta(cuentaOrigen);
        banco.addCuenta(cuentaDestino);

        assertEquals(2,banco.getCuentas().size());
        assertEquals("Santander",cuentaOrigen.getBanco().getNombre());
        assertEquals("Santander",cuentaDestino.getBanco().getNombre());
        assertTrue(banco.getCuentas().stream().
                anyMatch(c -> c.getPersona().equals("Jaime Matas Asensio")));
    }


    @Test
    @DisplayName("Test relacion entre cuentas") //156987.123
    void testRelacionBancoCuentaAssertAll() {
        banco.addCuenta(cuentaOrigen);
        banco.addCuenta(cuentaDestino);

        assertAll(()->{assertEquals(2,banco.getCuentas().size());},
                () -> assertEquals("Santander",cuentaOrigen.getBanco().getNombre()),
                () -> assertEquals("Santander",cuentaDestino.getBanco().getNombre()),
                () -> assertTrue(banco.getCuentas().stream().
                anyMatch(c -> c.getPersona().equals("Jaime Matas Asensio"))),
                () -> assertEquals("Jaime Matas Asensio",
                        banco.getCuentas().stream().filter(
                                c->c.getPersona().equals("Jaime Matas Asensio"))
                                .findFirst().get().getPersona()));

}

}