package org.test.springboot.seccion3.app.repositories;

import org.test.springboot.seccion3.app.models.Banco;

import java.util.List;

public interface IBancoRepository {

    List<Banco> findAll();
    Banco findById(Long id);
    void update(Banco banco);
}
