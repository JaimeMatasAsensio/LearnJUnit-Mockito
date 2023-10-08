package org.seccion2.appmockito.ejemplos.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.seccion2.appmockito.ejemplos.models.Examen;
import org.seccion2.appmockito.ejemplos.reporitories.ExamenRepositoryOtro;
import org.seccion2.appmockito.ejemplos.reporitories.IExamenRepository;
import org.seccion2.appmockito.ejemplos.reporitories.IPreguntaRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamenServiceImplTest {

    private IExamenRepository repository;
    private IPreguntaRepository repositoryPregunta;
    private IExamenService service;
    private List<Examen> datosEmpty;

    @BeforeEach
    void setUp() {
        //Haciendo los mocks de los servicios pasaremos los datos simulados
        repository = mock(IExamenRepository.class);
        repositoryPregunta = mock(IPreguntaRepository.class);
        service = new ExamenServiceImpl(repository,repositoryPregunta);

        datosEmpty =  Collections.emptyList();
    }

    @Test
    @DisplayName("Metodo estatico When")
    void findExamenPorNombre() {
        when(repository.findAll()).thenReturn(DatosExamenes.EXAMENES);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");

        assertNotNull(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.orElseThrow().getNombre());
    }

    @Test
    @DisplayName("Metodo estatico When")
    void findExamenPorNombreEmptyList() {
        when(repository.findAll()).thenReturn(datosEmpty);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");

        assertFalse(examen.isPresent());

    }

    @Test
    @DisplayName("Metodo estatico Verify")
    void testPreguntaExamen() {
        when(repository.findAll()).thenReturn(DatosExamenes.EXAMENES);
        when(repositoryPregunta.findPreguntasPorExamenId(anyLong())).thenReturn(DatosExamenes.PREGUNTAS);

        Examen examen = service.findExamenPornombreConPreguntas("Matematicas");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Metodo estatico Verify")
    void testPreguntaExamenVerify() {
        when(repository.findAll()).thenReturn(DatosExamenes.EXAMENES);
        when(repositoryPregunta.findPreguntasPorExamenId(anyLong())).thenReturn(DatosExamenes.PREGUNTAS);

        Examen examen = service.findExamenPornombreConPreguntas("Matematicas");

        assertAll(
                ()-> assertEquals(5, examen.getPreguntas().size()),
                ()-> assertTrue(examen.getPreguntas().contains("aritmetica")),
                ()-> verify(repository).findAll(),
                ()-> verify(repositoryPregunta).findPreguntasPorExamenId(5L)
        );

    }

    @Test
    @DisplayName("Metodo estatico Verify")
    void testPreguntaExamenVerifyEmptyList() {
        when(repository.findAll()).thenReturn(datosEmpty);
        when(repositoryPregunta.findPreguntasPorExamenId(anyLong())).thenReturn(DatosExamenes.PREGUNTAS);

        Examen examen = service.findExamenPornombreConPreguntas("Matematicas");

        assertAll(
                () -> assertNull(examen),
                () -> verify(repository).findAll(),
                () -> verify(repositoryPregunta,never()).findPreguntasPorExamenId(anyLong())
        );

    }

}