package org.test.springboot.seccion6.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.test.springboot.seccion6.app.models.Banco;

public interface IBancoRepository extends JpaRepository<Banco,Long> {
}
