package org.test.springboot.seccion3.app;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.test.springboot.seccion3.app.models.Banco;
import org.test.springboot.seccion3.app.models.Cuenta;
import org.test.springboot.seccion3.app.repositories.IBancoRepository;
import org.test.springboot.seccion3.app.repositories.ICuentaRepository;
import org.test.springboot.seccion3.app.services.CuentaServiceImpl;
import org.test.springboot.seccion3.app.services.ICuentaService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpringbootTestApplicationTests {

	private Cuenta cuenta1;
	private Cuenta cuenta2;
	private Banco banco;

	@Mock
	ICuentaRepository cuentaRepository;
	@Mock
	IBancoRepository bancoRepository;
	@InjectMocks
	CuentaServiceImpl service;

	@BeforeEach
	void setUp(){
		cuenta1 = new Cuenta(1L,"Persona1",new BigDecimal(1000));
		cuenta2 = new Cuenta(2L,"Persona2",new BigDecimal(2000));

		banco = new Banco(1L,"Banco1",0);
	}

	@Test
	void contextLoads() {

		when(cuentaRepository.findById(anyLong())).thenReturn(cuenta1).thenReturn(cuenta2)
				.thenReturn(cuenta1).thenReturn(cuenta2).thenReturn(cuenta1).thenReturn(cuenta2);
		when(bancoRepository.findById(anyLong())).thenReturn(banco);

		BigDecimal saldoOrigen = service.revisarSaldo(1L);
		BigDecimal saldoDestino = service.revisarSaldo(2L);

		assertEquals(new BigDecimal(1000),saldoOrigen );
		assertEquals(new BigDecimal(2000),saldoDestino );

		service.transferir(1L,2L,new BigDecimal(100),1L);

		saldoOrigen = service.revisarSaldo(1L);
		saldoDestino = service.revisarSaldo(2L);

		assertEquals(new BigDecimal(900),saldoOrigen );
		assertEquals(new BigDecimal(2100),saldoDestino );

		verify(cuentaRepository, times(6)).findById(anyLong());
		verify(bancoRepository, times(1)).findById(anyLong());

		verify(cuentaRepository, times(2)).update(any(Cuenta.class));
		verify(bancoRepository,times(1)).update(any(Banco.class));

	}

}
