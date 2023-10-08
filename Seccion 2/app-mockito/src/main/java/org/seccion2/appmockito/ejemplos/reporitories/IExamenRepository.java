package org.seccion2.appmockito.ejemplos.reporitories;

import org.seccion2.appmockito.ejemplos.models.Examen;

import java.util.List;

public interface IExamenRepository {
    List<Examen> findAll();
}
