package org.test.springboot.seccion3.app.repositories;

import org.test.springboot.seccion3.app.models.Cuenta;

import java.util.List;

public interface ICuentaRepository {

    List<Cuenta> findAll();

    Cuenta findById(Long id);

    void update(Cuenta cuenta);
}
