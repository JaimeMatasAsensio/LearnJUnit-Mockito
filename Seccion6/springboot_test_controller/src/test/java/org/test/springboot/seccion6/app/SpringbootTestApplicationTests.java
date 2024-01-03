package org.test.springboot.seccion6.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.test.springboot.seccion6.app.exceptions.DineroInsuficienteException;
import org.test.springboot.seccion6.app.models.Banco;
import org.test.springboot.seccion6.app.models.Cuenta;
import org.test.springboot.seccion6.app.repositories.IBancoRepository;
import org.test.springboot.seccion6.app.repositories.ICuentaRepository;
import org.test.springboot.seccion6.app.services.CuentaServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
	void testSpring1() {

		when(cuentaRepository.findById(anyLong())).thenReturn(Optional.of(cuenta1))
				.thenReturn(Optional.of(cuenta2))
				.thenReturn(Optional.of(cuenta1))
				.thenReturn(Optional.of(cuenta2))
				.thenReturn(Optional.of(cuenta1))
				.thenReturn(Optional.of(cuenta2));
		when(bancoRepository.findById(anyLong())).thenReturn(Optional.of(banco));

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

		verify(cuentaRepository, times(2)).save(any(Cuenta.class));
		verify(bancoRepository,times(1)).save(any(Banco.class));

	}

	@Test
	void testSpring2() {

		when(cuentaRepository.findById(anyLong())).thenReturn(Optional.of(cuenta1))
				.thenReturn(Optional.of(cuenta2))
				.thenReturn(Optional.of(cuenta1))
				.thenReturn(Optional.of(cuenta2))
				.thenReturn(Optional.of(cuenta1))
				.thenReturn(Optional.of(cuenta2));
		when(bancoRepository.findById(anyLong())).thenReturn(Optional.of(banco));

		BigDecimal saldoOrigen = service.revisarSaldo(1L);
		BigDecimal saldoDestino = service.revisarSaldo(2L);

		assertEquals(new BigDecimal(1000),saldoOrigen );
		assertEquals(new BigDecimal(2000),saldoDestino );

		assertThrows(DineroInsuficienteException.class, () -> service.transferir(1L,2L,new BigDecimal(1100),1L));

		verify(cuentaRepository, times(4)).findById(anyLong());
		verify(bancoRepository, times(1)).findById(anyLong());

	}

	@Test
	void testFindAll() {
		//Given

		when(cuentaRepository.findAll()).thenReturn(List.of(cuenta1,cuenta2));

		//When
		List<Cuenta> cuentas = service.findAll();

		//Then
		assertFalse(cuentas.isEmpty());
		assertEquals(2,cuentas.size());
		assertTrue(cuentas.contains(cuenta1));

		verify(cuentaRepository).findAll();
	}

	@Test
	void testCrear() {
		//Given
		when(cuentaRepository.save(any())).then(invocation ->{
			Cuenta c = invocation.getArgument(0);
			c.setId(3L);
			return c;
		});
		//When
		Cuenta cuenta = service.save(cuenta1);

		//Then
		assertEquals(3,cuenta.getId());
		assertEquals("Persona1",cuenta.getPersona());
		verify(cuentaRepository).save(any());
	}
}
