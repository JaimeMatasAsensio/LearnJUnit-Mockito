package org.test.springboot.seccion6.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.test.springboot.seccion6.app.models.Cuenta;
import org.test.springboot.seccion6.app.models.TransaccionDto;
import org.test.springboot.seccion6.app.services.ICuentaService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ICuentaService cuentaService;

    private Cuenta cuenta;

    private TransaccionDto dto;

    private Long id;

    private Long idPadre;

    private String texto;

    private BigDecimal saldo;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        id = 1L;
        idPadre = 2L;
        texto = "test";
        saldo = new BigDecimal(1500);

        cuenta = new Cuenta();
        cuenta.setPersona(texto);
        cuenta.setId(id);
        cuenta.setSaldo(saldo);

        dto = new TransaccionDto();
        dto.setCuentaOrigenId(id);
        dto.setCuentaDestinoId(idPadre);
        dto.setMonto(new BigDecimal(100));
        dto.setBancoId(id);

        objectMapper = new ObjectMapper();

    }

    @Test
    void testDetalle() throws Exception {
        //Given
        when(cuentaService.findById(any())).thenReturn(cuenta);

        //When
        mvc.perform(MockMvcRequestBuilders.get("/api/cuentas/1")
                .contentType(MediaType.APPLICATION_JSON))
        //Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persona").value("test"))
                .andExpect(jsonPath("$.saldo").value("1500"));
        verify(cuentaService).findById(any());
    }

    @Test
    void testTransferir() throws Exception, JsonProcessingException {
        //Given

        Map<String,Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status","Ok");
        response.put("mensaje", "Transferencia realizada con exito!");
        response.put("transaccion",dto);

        System.out.println(objectMapper.writeValueAsString(dto));
        System.out.println(objectMapper.writeValueAsString(response));

        //When
        mvc.perform(MockMvcRequestBuilders.post("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        //Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.mensaje").value("Transferencia realizada con exito!"))
                .andExpect(jsonPath("$.transaccion.cuentaOrigenId").value(id))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }


    @Test
    void testListar() throws Exception {
        //Given
        when(cuentaService.findAll()).thenReturn(List.of(cuenta,cuenta));
        //When
        mvc.perform(MockMvcRequestBuilders.get("/api/cuentas").contentType(MediaType.APPLICATION_JSON))
        //Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].persona").value("test"))
                .andExpect(jsonPath("$[1].saldo").value("1500"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(cuenta,cuenta))));

        verify(cuentaService).findAll();

    }

    @Test
    void testCrear() throws Exception {
        //Given
        cuenta.setId(null);
        when(cuentaService.save(any())).then(invocation -> {
            Cuenta c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });
        //When
        mvc.perform(MockMvcRequestBuilders.post("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuenta)))
        //Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",is(3)))
                .andExpect(jsonPath("$.persona",is("test")))
                .andExpect(jsonPath("$.saldo", is(1500)));

        verify(cuentaService).save(any());

    }
}