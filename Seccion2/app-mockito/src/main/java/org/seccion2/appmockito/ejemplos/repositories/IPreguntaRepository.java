package org.seccion2.appmockito.ejemplos.repositories;

import java.util.List;

public interface IPreguntaRepository {
    List<String> findPreguntasPorExamenId(Long id);

    void guardarVarias(List<String> preguntas);

}
