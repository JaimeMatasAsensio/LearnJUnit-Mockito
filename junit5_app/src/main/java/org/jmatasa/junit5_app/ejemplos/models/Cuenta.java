package org.jmatasa.junit5_app.ejemplos.models;

import org.jmatasa.junit5_app.ejemplos.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;

public class Cuenta {

    private BigDecimal saldo;
    private String persona;

    private Banco banco;

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Cuenta(BigDecimal saldo, String persona, Banco banco) {
        this.saldo = saldo;
        this.persona = persona;
        this.banco = banco;
    }

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

    public void debito(BigDecimal monto){

        this.saldo = this.saldo.subtract(monto);

        if (saldo.compareTo(BigDecimal.ZERO) < 0)
            throw new DineroInsuficienteException("Dinero insuficiente");
    }

    public void credito(BigDecimal monto){

        this.saldo = this.saldo.add(monto);

    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || !(obj instanceof Cuenta)) return  false;

        Cuenta c = (Cuenta) obj;

        if (this.persona == null || this.saldo == null) return false;

        return this.persona.equals(c.getPersona()) && this.saldo.equals(c.getSaldo());
    }

}
