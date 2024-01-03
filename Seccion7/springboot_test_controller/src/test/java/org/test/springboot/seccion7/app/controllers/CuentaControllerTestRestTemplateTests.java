package org.test.springboot.seccion7.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.test.springboot.seccion7.app.models.Cuenta;
import org.test.springboot.seccion7.app.models.TransaccionDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Integracion_RT") //Mediante etiquetas podemos excluir la ejecucion de test en la configuracion
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CuentaControllerTestRestTemplateTests {

    @Autowired
    private TestRestTemplate client;

    private ObjectMapper objectMapper;

    private Long id;

    private Long idPadre;

    private BigDecimal monto;
    @LocalServerPort
    private int puerto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        id = 1L;
        idPadre = 2L;
        monto = new BigDecimal(100);

    }

    @Test
    @Order(1)
    void testTransferir() throws JsonProcessingException {

        TransaccionDto dto = new TransaccionDto();
        dto.setCuentaOrigenId(id);
        dto.setCuentaDestinoId(idPadre);
        dto.setMonto(monto);
        dto.setBancoId(id);

        ResponseEntity<String> responseEntity =
                client.postForEntity(crearUri("/api/cuentas/transferir"),dto, String.class);
        System.out.println(puerto);

        String json = responseEntity.getBody();

        System.out.println(json);

        //Comprobamos mediante Json
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertNotNull(json);
        assertTrue(json.contains("Transferencia realizada con exito!"));
        assertTrue(json.contains("{\"cuentaOrigenId\":1,\"cuentaDestinoId\":2,\"monto\":100,\"bancoId\":1},\"mensaje\":\"Transferencia realizada con exito!\",\"status\":\"Ok\"}"));

        //Comprobaciones mediante JsonNode
        JsonNode jsonNode = objectMapper.readTree(json);
        assertEquals("Transferencia realizada con exito!", jsonNode.path("mensaje").asText());
        assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
        assertEquals("100", jsonNode.path("transaccion").path("monto").asText());
        assertEquals(1L, jsonNode.path("transaccion").path("cuentaOrigenId").asLong());

        //Comprobaciones mediante JsonFull
        Map<String,Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status","Ok");
        response.put("mensaje", "Transferencia realizada con exito!");
        response.put("transaccion",dto);

        assertEquals(objectMapper.writeValueAsString(response),responseEntity.getBody());

    }

    @Test
    @Order(2)
    void testDetalle() {

        ResponseEntity<Cuenta> responseEntity =
                client.getForEntity(crearUri("/api/cuentas/1"), Cuenta.class);
        Cuenta cuenta = responseEntity.getBody();

        //Comprobaciones del estado y tipo de contenido
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());

        //Comprobaciones de datos
        assertNotNull(cuenta);
        assertEquals("Jaime", cuenta.getPersona());
        assertEquals("900.00", cuenta.getSaldo().toString());
        assertEquals(new Cuenta(1L,"Jaime",new BigDecimal("900.00")), cuenta);

    }

    @Test
    @Order(3)
    void testlistar() throws JsonProcessingException {
        ResponseEntity<Cuenta[]> responseEntity =
                client.getForEntity(crearUri("/api/cuentas"), Cuenta[].class);

        List<Cuenta> listCuentas = Arrays.asList(responseEntity.getBody());

        //Comprobaciones del estado y tipo de contenido
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());

        //Comprobamos datos
        assertEquals(2, listCuentas.size());
        assertEquals(1L, listCuentas.get(0).getId());
        assertEquals("Jaime", listCuentas.get(0).getPersona());
        assertEquals("900.00", listCuentas.get(0).getSaldo().toPlainString());
        assertEquals(2L, listCuentas.get(1).getId());
        assertEquals("John", listCuentas.get(1).getPersona());
        assertEquals("2100.00", listCuentas.get(1).getSaldo().toPlainString());

        //Comprobaciones con JsonNode
        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(listCuentas));
        assertEquals(1L, json.get(0).path("id").asLong());
        assertEquals("Jaime", json.get(0).path("persona").asText());
        assertEquals("900.0", json.get(0).path("saldo").asText());
        assertEquals(2L, json.get(1).path("id").asLong());
        assertEquals("John", json.get(1).path("persona").asText());
        assertEquals("2100.0", json.get(1).path("saldo").asText());

    }

    @Test
    @Order(4)
    void testGuardar() {
        Cuenta cuenta = new Cuenta(null,"nombre",new BigDecimal(500));

        ResponseEntity<Cuenta> responseEntity =
                client.postForEntity(crearUri("/api/cuentas"), cuenta, Cuenta.class);

        //Comprobaciones del estado y tipo de contenido
        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());

        //Comprobamos datos
        Cuenta cuentaNueva = responseEntity.getBody();
        assertNotNull(cuentaNueva);
        assertEquals(3L,cuentaNueva.getId());
        assertEquals("nombre", cuentaNueva.getPersona());
        assertEquals("500", cuentaNueva.getSaldo().toPlainString());
    }

    @Test
    @Order(5)
    void testEliminar() {
        //Buscamos el listado antes del borrado
        ResponseEntity<Cuenta[]> responseEntity =
                client.getForEntity(crearUri("/api/cuentas"), Cuenta[].class);

        List<Cuenta> listCuentas = Arrays.asList(responseEntity.getBody());

        //Comprobaciones del estado y tipo de contenido
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        //Comprobamos datos antes del borrado
        assertEquals(3, listCuentas.size());


        //client.delete(crearUri("/api/cuentas/3"));
        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id", 3L);
        ResponseEntity<Void> exchange =
                client.exchange(crearUri("/api/cuentas/{id}"),
                        HttpMethod.DELETE,
                        null,
                        Void.class,
                        pathVariables);

        assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
        assertFalse(exchange.hasBody());

        //Buscamos el listado despues del borrado
        responseEntity =
                client.getForEntity(crearUri("/api/cuentas"), Cuenta[].class);

        listCuentas = Arrays.asList(responseEntity.getBody());

        //Comprobaciones del estado y tipo de contenido
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());

        //Comprobamos datos despues del borrado
        assertEquals(2, listCuentas.size());


    }

    private String crearUri(String uri){

        return "http://localhost:" + puerto + uri;

    }
}