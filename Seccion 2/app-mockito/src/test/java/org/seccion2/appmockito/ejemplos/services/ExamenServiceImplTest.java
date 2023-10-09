package org.seccion2.appmockito.ejemplos.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
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

@ExtendWith(MockitoExtension.class) //Otra forma de habilidar las anotaciones
class ExamenServiceImplTest {

    @Mock // Con las etiquetas mock creamos las instancias Mock
    private IExamenRepository repository;
    @Mock
    private IPreguntaRepository repositoryPregunta;
    @InjectMocks //Crear la instancia del objeto e inyecta los mocks, debe ser sobre la clase concreta no sobre la interfaz
    private ExamenServiceImpl service;
    private List<Examen> datosEmpty;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); //Habilitamos el uso de notaciones para esta clase
        //Haciendo los mocks de los servicios pasaremos los datos simulados
        //repository = mock(IExamenRepository.class);
        //repositoryPregunta = mock(IPreguntaRepository.class);
        //service = new ExamenServiceImpl(repository,repositoryPregunta);

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
        //when(repositoryPregunta.findPreguntasPorExamenId(anyLong())).thenReturn(DatosExamenes.PREGUNTAS);

        Examen examen = service.findExamenPornombreConPreguntas("Matematicas");

        assertAll(
                //() -> assertNull(examen),
                () -> verify(repository).findAll(),
                () -> verify(repositoryPregunta,never()).findPreguntasPorExamenId(anyLong())
        );

    }

    @Test
    void testGuardarExamen() {
        //Given
        Examen examenPreguntas = DatosExamenes.EXAMEN;
        examenPreguntas.setPreguntas(DatosExamenes.PREGUNTAS);
        when(repository.guardar(any(Examen.class))).thenReturn(DatosExamenes.EXAMEN);
        //When
        Examen examen = service.guardar(examenPreguntas);

        //Then
        assertNotNull(examen.getId());
        assertEquals(8L,examen.getId());
        verify(repository).guardar(any(Examen.class));
        verify(repositoryPregunta).guardarVarias(anyList());
    }

    @Test
    void testGuardarExamenSecuencia() {
        //Given - Preparacion
        Examen examenPreguntas = DatosExamenes.EXAMEN;
        examenPreguntas.setPreguntas(DatosExamenes.PREGUNTAS);
        when(repository.guardar(any(Examen.class))).then(new Answer<Examen>(){
            Long secuencia = 8L;
            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });

        //When - Ejecucion
        Examen examen = service.guardar(examenPreguntas);

        //Then - Comprobacion
        assertNotNull(examen.getId());
        assertEquals(8L,examen.getId());
        verify(repository).guardar(any(Examen.class));
        verify(repositoryPregunta).guardarVarias(anyList());
    }

    @Test
    void testManejoException() {
        when(repository.findAll()).thenReturn(DatosExamenes.EXAMENES_ID_NULL);
        when(repositoryPregunta.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.findExamenPornombreConPreguntas("Matematicas"));

        assertEquals(IllegalArgumentException.class,exception.getClass());
        verify(repositoryPregunta).findPreguntasPorExamenId(null);
    }
}