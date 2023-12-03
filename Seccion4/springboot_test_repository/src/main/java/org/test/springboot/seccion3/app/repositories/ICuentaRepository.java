package org.test.springboot.seccion3.app.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.test.springboot.seccion3.app.models.Cuenta;

import java.util.List;
import java.util.Optional;

public interface ICuentaRepository extends JpaRepository<Cuenta,Long> {

    Optional<Cuenta> findByPersona(String persona);
}
