package org.jmatasa.junit5_app.ejemplos.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Banco {
    private String nombre;

    private List<Cuenta> cuentas;

    public Banco(String nombre) {
        this.nombre = nombre;
        cuentas = new ArrayList<>();
    }
    public Banco() {
        cuentas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public void addCuenta(Cuenta cuenta){
        cuentas.add(cuenta);
    }

    public void transferir(Cuenta origen, Cuenta destino, BigDecimal monto){

        origen.debito(monto);
        destino.credito(monto);

    }


}
