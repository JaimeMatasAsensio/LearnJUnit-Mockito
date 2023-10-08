package org.seccion2.appmockito.ejemplos.services;

import org.seccion2.appmockito.ejemplos.models.Examen;

import java.util.Optional;

public interface IExamenService {
    Optional<Examen> findExamenPorNombre(String nombre);

    Examen findExamenPornombreConPreguntas(String nombre);
}
