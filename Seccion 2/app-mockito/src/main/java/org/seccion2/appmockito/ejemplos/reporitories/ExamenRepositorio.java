package org.seccion2.appmockito.ejemplos.reporitories;

import org.seccion2.appmockito.ejemplos.models.Examen;

import java.util.List;

public interface ExamenRepositorio {
    List<Examen> findAll();
}
