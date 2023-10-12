package org.seccion2.appmockito.ejemplos.services;

import org.seccion2.appmockito.ejemplos.models.Examen;
import org.seccion2.appmockito.ejemplos.repositories.IExamenRepository;
import org.seccion2.appmockito.ejemplos.repositories.IPreguntaRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements IExamenService{

    private IExamenRepository examenRepository;
    private IPreguntaRepository preguntaRepository;

    public ExamenServiceImpl(IExamenRepository examenRepository, IPreguntaRepository preguntaRepository) {
        this.examenRepository = examenRepository;
        this.preguntaRepository = preguntaRepository;
    }

    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {
        Optional<Examen> examenOptional = examenRepository.findAll()
                .stream()
                .filter(e -> e.getNombre().contains(nombre))
                .findFirst();
        return examenOptional;

    }

    @Override
    public Examen findExamenPornombreConPreguntas(String nombre) {
        Optional<Examen> examenOptional = findExamenPorNombre(nombre);
        Examen examen = null;
        if(examenOptional.isPresent()){
            examen = examenOptional.orElseThrow();
            List<String> preguntas = preguntaRepository.findPreguntasPorExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }

    @Override
    public Examen guardar(Examen examen) {
        if (!examen.getPreguntas().isEmpty()){
            preguntaRepository.guardarVarias(examen.getPreguntas());
        }
        return examenRepository.guardar(examen);
    }
}
