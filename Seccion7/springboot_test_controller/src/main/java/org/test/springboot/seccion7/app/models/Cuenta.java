package org.test.springboot.seccion7.app.models;

import jakarta.persistence.*;
import org.test.springboot.seccion7.app.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "cuentas")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String persona;
    private BigDecimal saldo;

    public Cuenta() {
    }

    public Cuenta(Long id, String persona, BigDecimal saldo) {
        this.id = id;
        this.persona = persona;
        this.saldo = saldo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void debito(BigDecimal monto){

        BigDecimal nuevoSaldo = this.saldo.subtract(monto);

        if(nuevoSaldo.compareTo(BigDecimal.ZERO) < 0)
            throw new DineroInsuficienteException("No hay dinero suficiente");

        this.saldo = nuevoSaldo;

    }

    public void credito(BigDecimal monto){
        this.saldo = this.saldo.add(monto);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Cuenta cuenta = (Cuenta) obj;

        return Objects.equals(id, cuenta.id) && Objects.equals(persona, cuenta.persona)  && Objects.equals(saldo, cuenta.saldo) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,persona,saldo);
    }
}
