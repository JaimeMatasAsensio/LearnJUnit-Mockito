package org.seccion2.appmockito.ejemplos.services;

import org.seccion2.appmockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class DatosExamenes {

    public final static List<Examen> EXAMENES = Arrays.asList(new Examen(5L,"Matematicas"),
            new Examen(6L,"Lengauje"),
            new Examen(7L, "Historia"),
            new Examen(1L, "Ciencias"));

    public final static List<String> PREGUNTAS = Arrays.asList("aritmetica","integrales","derivadas","trigonometria","geometria");
}
