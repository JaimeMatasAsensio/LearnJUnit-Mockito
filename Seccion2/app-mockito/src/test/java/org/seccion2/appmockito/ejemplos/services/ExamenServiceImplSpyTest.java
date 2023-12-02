package org.seccion2.appmockito.ejemplos.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.seccion2.appmockito.ejemplos.DatosExamenes;
import org.seccion2.appmockito.ejemplos.models.Examen;
import org.seccion2.appmockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.seccion2.appmockito.ejemplos.repositories.PreguntasRepositoryImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //Otra forma de habilidar las anotaciones
class ExamenServiceImplSpyTest {

    @Spy
    private ExamenRepositoryImpl repository;
    @Spy
    private PreguntasRepositoryImpl repositoryPregunta;
    @InjectMocks //Crear la instancia del objeto e inyecta los mocks, debe ser sobre la clase concreta no sobre la interfaz
    private ExamenServiceImpl service;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); //Habilitamos el uso de notaciones para esta clase
        //Haciendo los mocks de los servicios, pasaremos los datos simulados
        //repository = mock(IExamenRepository.class);
        //repositoryPregunta = mock(IPreguntaRepository.class);
        //service = new ExamenServiceImpl(repository,repositoryPregunta);

    }

    @Test
    void testSpy() {

        //Instanciamos nuestro servicio
        service = new ExamenServiceImpl(repository,repositoryPregunta);

        //Añadimos un comportamiento a uno de los metodos
            //Con este metodo de mockito hacemos una llamada al metodo real
        //when(repositoryPregunta.findPreguntasPorExamenId(anyLong())).thenReturn(DatosExamenes.PREGUNTAS_SPY);

            //Con este metodo por el contrario encapsulamos el spy y simulamos la llamada y respuesta de este metodo
        doReturn(DatosExamenes.PREGUNTAS_SPY).when(repositoryPregunta).findPreguntasPorExamenId(anyLong());

        //Ejecutamos nuestra prueba
        Examen examen = service.findExamenPornombreConPreguntas("Matematicas");

        //Realizamos las comprobaciones que creamos conveniente
        assertEquals(5,examen.getId());
        assertEquals(2,examen.getPreguntas().size());

        //En este test se observara que estamos ejecutando los metodos reales (Hay una salida por pantalla de ellos)
        //Ademas de entregar los datos que nosotros hemos indicado en el comportamiento
    }



}