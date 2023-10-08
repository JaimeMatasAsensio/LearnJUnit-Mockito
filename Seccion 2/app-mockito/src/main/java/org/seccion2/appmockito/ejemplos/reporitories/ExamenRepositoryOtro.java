package org.seccion2.appmockito.ejemplos.reporitories;

import org.seccion2.appmockito.ejemplos.models.Examen;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryOtro implements IExamenRepository{
    @Override
    public List<Examen> findAll() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
