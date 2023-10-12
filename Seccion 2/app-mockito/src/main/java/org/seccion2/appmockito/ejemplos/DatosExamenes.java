package org.seccion2.appmockito.ejemplos;

import org.seccion2.appmockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class DatosExamenes {

    public final static List<Examen> EXAMENES = Arrays.asList(new Examen(5L,"Matematicas"),
            new Examen(6L,"Lengauje"),
            new Examen(7L, "Historia"),
            new Examen(1L, "Ciencias"));

    public final static List<Examen> EXAMENES_ID_NULL = Arrays.asList(new Examen(null,"Matematicas"),
            new Examen(null,"Lengauje"),
            new Examen(null, "Historia"),
            new Examen(null, "Ciencias"));

    public final static List<Examen> EXAMENES_ID_NEGATIVOS = Arrays.asList(new Examen(-5L,"Matematicas"),
            new Examen(-6L,"Lengauje"),
            new Examen(-7L, "Historia"),
            new Examen(-1L, "Ciencias"));
    public final static List<String> PREGUNTAS = Arrays.asList("aritmetica","integrales","derivadas","trigonometria","geometria");

    public final static Examen EXAMEN = new Examen(8L,"Fisica");

}
