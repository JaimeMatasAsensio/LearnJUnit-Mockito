package org.seccion2.appmockito.ejemplos.repositories;

import org.seccion2.appmockito.ejemplos.DatosExamenes;
import org.seccion2.appmockito.ejemplos.models.Examen;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryImpl implements IExamenRepository{
    @Override
    public List<Examen> findAll() {
        System.out.println("Metodo real" + this.getClass());
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return DatosExamenes.EXAMENES;
    }

    @Override
    public Examen guardar(Examen examen) {
        System.out.println("Metodo real" + this.getClass());
        return DatosExamenes.EXAMEN;
    }

}
