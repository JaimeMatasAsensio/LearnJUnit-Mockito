package org.test.springboot.seccion7.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.test.springboot.seccion7.app.models.Banco;

public interface IBancoRepository extends JpaRepository<Banco,Long> {
}
