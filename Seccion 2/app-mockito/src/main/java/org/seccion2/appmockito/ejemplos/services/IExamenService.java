package org.seccion2.appmockito.ejemplos.services;

import org.seccion2.appmockito.ejemplos.models.Examen;

public interface IExamenService {
    Examen findExamenPorNombre(String nombre);
}
