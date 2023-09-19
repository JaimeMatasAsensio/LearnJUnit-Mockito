package org.jmatasa.junit5_app.ejemplos.models;

import java.math.BigDecimal;

public class Cuenta {

    private BigDecimal saldo;
    private String persona;

    public Cuenta(BigDecimal saldo, String persona) {
        this.saldo = saldo;
        this.persona = persona;
    }

    public Cuenta(){}

    //Get&set para Persona
    public String getPersona() {
        return persona;
    }
    public void setPersona(String persona) {
        this.persona = persona;
    }

    //Get&Set para Saldo
    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }



}
