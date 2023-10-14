package org.seccion2.appmockito.ejemplos.repositories;

import org.seccion2.appmockito.ejemplos.DatosExamenes;

import java.util.List;

public class PreguntasRepositoryImpl implements IPreguntaRepository{
    @Override
    public List<String> findPreguntasPorExamenId(Long id) {
        System.out.println("Metodo real " + this.getClass()+".findPreguntasPorExamenId");
        return DatosExamenes.PREGUNTAS;
    }

    @Override
    public void guardarVarias(List<String> preguntas) {
        System.out.println("Metodo real " + this.getClass()+".guardarVarias");
    }
}
