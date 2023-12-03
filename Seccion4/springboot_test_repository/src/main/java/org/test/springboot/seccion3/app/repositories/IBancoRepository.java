package org.test.springboot.seccion3.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.test.springboot.seccion3.app.models.Banco;

import java.util.List;

public interface IBancoRepository extends JpaRepository<Banco,Long> {
}
