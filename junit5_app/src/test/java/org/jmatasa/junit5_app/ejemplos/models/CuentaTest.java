package org.jmatasa.junit5_app.ejemplos.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    @DisplayName("Cuenta getPersona OK")
    void nombreCuentaOK() {
        Cuenta cuenta = new Cuenta();
        cuenta.setPersona("Jaime");
        assertEquals("Jaime",cuenta.getPersona());
    }

    @Test
    @DisplayName("Cuenta getPersona KO")
    void nombreCuentaKO() {
        Cuenta cuenta = new Cuenta();
        assertNotEquals("Jaime",cuenta.getPersona());
    }

    @Test
    @DisplayName("Cuenta ContructorOK")
    void constructorCuentaOK() {
        Cuenta cuenta = new Cuenta(new BigDecimal("15478.6978"),"Jaime");
        assertTrue(cuenta.getPersona()!=null&&cuenta.getSaldo()!=null?true:false);

    }

    @Test
    @DisplayName("Cuenta contructorKO")
    void constructorCuentaK0() {
        Cuenta cuenta = new Cuenta();
        assertTrue(cuenta.getPersona()!=null&&cuenta.getSaldo()!=null?false:true);
    }
}