package org.test.springboot.seccion7.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.test.springboot.seccion7.app.models.Cuenta;
import org.test.springboot.seccion7.app.models.TransaccionDto;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@Tag("Integracion_WC")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class CuentaControllerWebTestClientTests {

    @Autowired
    private WebTestClient client;

    private TransaccionDto dto;
    private Long id;
    private Long idPadre;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        id = 1L;
        idPadre = 2L;

        dto = new TransaccionDto();
        dto.setCuentaOrigenId(id);
        dto.setCuentaDestinoId(idPadre);
        dto.setMonto(new BigDecimal(100));
        dto.setBancoId(id);

        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testTransferir() throws JsonProcessingException {
        //Given
        System.out.println(dto.toString());

        Map<String,Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status","Ok");
        response.put("mensaje", "Transferencia realizada con exito!");
        response.put("transaccion",dto);

        //When
        client.post().uri("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
        //Then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(r -> {
                    try {

                        JsonNode json = objectMapper.readTree( r.getResponseBody() );
                        assertEquals("Transferencia realizada con exito!",json.path("mensaje").asText());
                        assertEquals(1,json.path("transaccion").path("cuentaOrigenId").asLong());
                        assertEquals(LocalDate.now().toString(),json.path("date").asText());
                        assertEquals("100",json.path("transaccion").path("monto").asText());

                    } catch (IOException e) {

                        e.printStackTrace();

                    }
                })
                    .jsonPath("$.mensaje").isNotEmpty()
                    .jsonPath("$.mensaje").value(is("Transferencia realizada con exito!"))
                    .jsonPath("$.mensaje").value(valor -> assertEquals("Transferencia realizada con exito!",valor))
                    .jsonPath("$.mensaje").isEqualTo("Transferencia realizada con exito!")
                    .jsonPath("$.transaccion.cuentaOrigenId").isEqualTo(dto.getCuentaOrigenId())
                    .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                    .json(objectMapper.writeValueAsString(response));
    }

    @Test
    @Order(2)
    void testDetalle() throws JsonProcessingException {

        Cuenta cuenta = new Cuenta(id,"Jaime",new BigDecimal(900));

        client.get().uri("/api/cuentas/1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.persona").isEqualTo("Jaime")
                .jsonPath("$.saldo").isEqualTo(900)
                .json(objectMapper.writeValueAsString(cuenta));



    }

    @Test
    @Order(3)
    void testDetalle1() {

        client.get().uri("/api/cuentas/2").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(r ->{
                    Cuenta cuenta = r.getResponseBody();
                    assertEquals("John",cuenta.getPersona());
                    assertEquals("2100.00", cuenta.getSaldo().toString());
                });

    }

    @Test
    @Order(4)
    void testListar() {

        client.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].persona").isEqualTo("Jaime")
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].saldo").isEqualTo(900)
                .jsonPath("$[1].persona").isEqualTo("John")
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].saldo").isEqualTo(2100)
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(2));

    }
    @Test
    @Order(5)
    void testListar1() {

        client.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .consumeWith( r ->{
                    List<Cuenta> listCuenta = r.getResponseBody();
                    assertNotNull(listCuenta);
                    assertEquals(2, listCuenta.size());
                    assertEquals(1L, listCuenta.get(0).getId());
                    assertEquals("Jaime", listCuenta.get(0).getPersona());
                    assertEquals("900.00", listCuenta.get(0).getSaldo().toString());
                    assertEquals(2L, listCuenta.get(1).getId());
                    assertEquals("John", listCuenta.get(1).getPersona());
                    assertEquals("2100.00", listCuenta.get(1).getSaldo().toString());
                })
                .hasSize(2);

    }

    @Test
    @Order(6)
    void testGuardar() {
        //Given
        Cuenta cuenta = new Cuenta(null,"nombre",new BigDecimal(500));
        //When
        client.post().uri("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
        //Then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.persona").isEqualTo("nombre")
                .jsonPath("$.persona").value(is("nombre"))
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.saldo").isEqualTo(500);
    }

    @Test
    @Order(7)
    void testGuardar1() {
        //Given
        Cuenta cuenta = new Cuenta(null,"Name",new BigDecimal(600));
        //When
        client.post().uri("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
                //Then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(r ->{
                    Cuenta c = r.getResponseBody();
                    assertNotNull( c.getId());
                    assertEquals("Name", c.getPersona());
                    assertEquals("600",c.getSaldo().toString());
                });
    }


    @Test
    @Order(8)
    void testEliminar() {

        client.get().uri("/api/cuentas")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .hasSize(4);

        client.delete().uri("/api/cuentas/3")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        client.get().uri("/api/cuentas")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .hasSize(3);

        client.get().uri("/api/cuentas/3")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();

    }
}