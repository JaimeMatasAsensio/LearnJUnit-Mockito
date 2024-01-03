package org.test.springboot.seccion5.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.springboot.seccion5.app.models.Cuenta;
import org.test.springboot.seccion5.app.models.TransaccionDto;
import org.test.springboot.seccion5.app.services.ICuentaService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("/api/cuentas")
@RestController
public class CuentaController {

    @Autowired
    private ICuentaService cuentaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cuenta> Listar(){
        return cuentaService.findAll();

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cuenta crear(@RequestBody Cuenta cuenta){
        return cuentaService.save(cuenta);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cuenta detalle(@PathVariable Long id){
        return cuentaService.findById(id);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir (@RequestBody TransaccionDto dto){

        cuentaService.transferir(dto.getCuentaOrigenId(),
                dto.getCuentaDestinoId(),
                dto.getMonto(), dto.getBancoId());

        Map<String,Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status","Ok");
        response.put("mensaje", "Transferencia realizada con exito!");
        response.put("transaccion",dto);

        return ResponseEntity.ok(response);

    }


}
