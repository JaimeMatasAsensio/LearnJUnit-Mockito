package org.jmatasa.junit5_app.ejemplos.models;

import org.jmatasa.junit5_app.ejemplos.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {
    private Cuenta cuentaOrigen, cuentaDestino;

    private Banco banco;
    private String nombre;
    private BigDecimal saldo;
    @BeforeEach
    void setUpTest(){
        nombre = "Jaime Matas Asensio";
        saldo = new BigDecimal("156987.123");
        banco = new Banco("Santander");
        cuentaOrigen = new Cuenta(saldo,nombre,banco);
        cuentaDestino = new Cuenta(saldo,nombre,banco);

    }

    @BeforeAll
    void beforeAll() {
        System.out.println("Inicializando Test...");
    }

    @AfterAll
    void afterAll() {
        System.out.println("... Test finalizados");
    }

    //Al utilizar @Nested podemos tener distintas configuraciones de lanzamiento para las clases.
    @Nested
    @DisplayName("Tests para comprobar atributos de la clase cuenta")
    class cuentaTestNombre{
        @Test
        @DisplayName("Cuenta getPersona OK")
        void nombreCuentaOK() {
            assertEquals("Jaime Matas Asensio", cuentaOrigen.getPersona(),"El nombre no puede ser null");
        }

        @Test
        @DisplayName("Cuenta getPersona KO")
        void nombreCuentaKO() {
            assertNotEquals("Jaime", cuentaOrigen.getPersona(),"No es el nombre esperado");
        }

        @Test
        @DisplayName("Cuenta ContructorOK")
        void constructorCuentaOK() {
            Cuenta cuenta = new Cuenta(new BigDecimal("15478.6978"), "Jaime");
            assertNotNull(cuenta.getSaldo(),"El saldo no puede ser null");
            assertTrue(cuenta.getPersona() != null && cuenta.getSaldo() != null);

        }

        @Test
        @DisplayName("Cuenta contructorKO")
        void constructorCuentaK0() {
            Cuenta cuenta = new Cuenta();
            assertTrue(cuenta.getPersona() == null || cuenta.getSaldo() == null);
        }

        //AssertFalse|True
        @Test
        @DisplayName("Test saldo cuenta mayor a 0")
        void testCuentaSaldo(){
            assertFalse(cuentaOrigen.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuentaOrigen.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        @DisplayName("Test Referencia de cuenta")
        void testReferenciaCuenta() {
            Cuenta cuentaA = new Cuenta(new BigDecimal("123.456"),"Manolo");
            Cuenta cuentaB = new Cuenta(new BigDecimal("123.456"),"Manolo");

            assertEquals(cuentaA,cuentaB);
        }

    }

    @Nested
    @DisplayName("Tests para comprobar los metodos credito y debito")
    class cuentaTestCreditoDebito{
        @Test
        @DisplayName("Test para metodo debito")
        void testDebitoCuenta() {
            cuentaOrigen.debito(new BigDecimal(100));
            assertNotNull(cuentaOrigen.getSaldo());
            assertEquals(156887.123, cuentaOrigen.getSaldo().doubleValue());
            assertEquals("156887.123", cuentaOrigen.getSaldo().toPlainString());
        }


        @Test
        @DisplayName("Test para metodo debito")
        @Disabled //Notacion JUnit para deshabilitar un test
        void testCreditoCuenta() {
            cuentaOrigen.credito(new BigDecimal(100));

            assertAll(
                    ()->assertNotNull(cuentaOrigen.getSaldo(),()->"El saldo no puede ser null"),
                    ()->assertEquals(157087.123, cuentaOrigen.getSaldo().doubleValue(),
                            ()->"No se ha realizado la tranferencia desde la cuenta origen"),
                    ()->assertEquals("157087.123", cuentaOrigen.getSaldo().toPlainString(),
                            ()->"No se ha realizado la tranferencia desde la cuenta origen"));


            //Prueba con assertAll -> Todos fallan
            fail(); //Metodo JUnit para forzar el fallo
            assertAll(
                    ()->assertNotNull(null,()->"El saldo no puede ser null. Se espera: " + cuentaOrigen.getSaldo().toPlainString()),
                    ()->assertEquals(157087.23, cuentaOrigen.getSaldo().doubleValue(),
                            ()->"No se ha realizado la credito para la cuenta origen. Se espera: " + cuentaOrigen.getSaldo().toPlainString()),
                    ()->assertEquals("157087.13", cuentaOrigen.getSaldo().toPlainString(),
                            ()->"No se ha realizado la credito para la cuenta origen. Se espera: " + cuentaOrigen.getSaldo().toPlainString()));

        }

        //AssertEquals
        @Test
        @DisplayName("Test para excepcion Insuficiente dinero")
        void testDineroInsuficienteException() {
            Exception ex = assertThrows(DineroInsuficienteException.class, () -> {
                cuentaOrigen.debito(new BigDecimal("2000000"));
            },"Dinero insuficiente");
            String actual =ex.getMessage();
            String esperado ="Dinero insuficiente";
            assertEquals(esperado, actual);
        }
    }

    @Nested
    @DisplayName("Tests para comprobar el metodo transferencia")
    class cuentaTestTransferencia{
        @Test
        @DisplayName("Test Transferir dinero entre cuentas") //156987.123
        void testTransferirDineroCuentas() {
            banco.transferir(cuentaOrigen,cuentaDestino, new BigDecimal("1000.00"));
            assertEquals("155987.123",cuentaOrigen.getSaldo().toPlainString());
            assertEquals("157987.123",cuentaDestino.getSaldo().toPlainString());
        }

        @Test
        @DisplayName("Test relacion entre cuentas") //156987.123
        void testRelacionBancoCuenta() {
            banco.addCuenta(cuentaOrigen);
            banco.addCuenta(cuentaDestino);

            assertEquals(2,banco.getCuentas().size());
            assertEquals("Santander",cuentaOrigen.getBanco().getNombre());
            assertEquals("Santander",cuentaDestino.getBanco().getNombre());
            assertTrue(banco.getCuentas().stream().
                    anyMatch(c -> c.getPersona().equals("Jaime Matas Asensio")));
        }

        //Test con varios Assert usando AssertAll
        @Test
        @DisplayName("Test relacion entre cuentas") //156987.123
        void testRelacionBancoCuentaAssertAll() {
            banco.addCuenta(cuentaOrigen);
            banco.addCuenta(cuentaDestino);

            assertAll(()->{assertEquals(2,banco.getCuentas().size());},
                    () -> assertEquals("Santander",cuentaOrigen.getBanco().getNombre()),
                    () -> assertEquals("Santander",cuentaDestino.getBanco().getNombre()),
                    () -> assertTrue(banco.getCuentas().stream().
                            anyMatch(c -> c.getPersona().equals("Jaime Matas Asensio"))),
                    () -> assertEquals("Jaime Matas Asensio",
                            banco.getCuentas().stream().filter(
                                            c->c.getPersona().equals("Jaime Matas Asensio"))
                                    .findFirst().get().getPersona()));

        }
    }

    @Nested
    @DisplayName("Tests para comprobar los assumptions")
    class cuentaTestAssumptions{
        //Asumptions
        //Para habilitar test en funcion de parametros
        @Test
        @DisplayName("Test para excepcion Insuficiente dinero")
        void testDineroInsuficienteExceptionAssumptionsPRO() {
            boolean esPro = "pro".equals(System.getProperty("ENV"));
            assumeTrue(esPro);
            Exception ex = assertThrows(DineroInsuficienteException.class, () -> {
                cuentaOrigen.debito(new BigDecimal("2000000"));
            },"Dinero insuficiente");
            String actual =ex.getMessage();
            String esperado ="Dinero insuficiente";
            assertEquals(esperado, actual);
        }

        @Test
        @DisplayName("Test para excepcion Insuficiente dinero")
        void testDineroInsuficienteExceptionAssumptionsDat() {
            boolean esDev = "pro".equals(System.getProperty("ENV"));
            Exception ex = assertThrows(DineroInsuficienteException.class, () -> {
                cuentaOrigen.debito(new BigDecimal("2000000"));
            },"Dinero insuficiente");
            String actual =ex.getMessage();
            String esperado ="Dinero insuficiente";
            assumingThat(esDev, () -> {
                assertEquals(esperado, actual);
            });

        }
    }

    @Nested
    @DisplayName("Tests para comprobar habilitar tests en funcion de parametros de sistema")
    class SistemaOperativoTest{
        //Test habilitados o deshabilitados en funcion del systema operativo utilizado
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.LINUX})
        void testSoloLinuxMac() {
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {
        }

        @Test
        @DisabledOnOs({OS.LINUX, OS.LINUX})
        void testNoLinuxMac() {
        }
    }

    @Nested
    @DisplayName("Tests para comprobar habilitar tests en funcion de versiones de JAVA")
    class JavaVersionTest{
        //Test habilitados para versiones de Java
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void testSoloJava8() {
        }

        //Test lanzados en funcion de variables de sistema
        //Metodo para obtener las propiedades del sistema
        @Test
        @Disabled //Habilitar para ver variables de sistema
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((k, v)-> System.out.println(k + ":"  + v));
        }

        @Test
        @EnabledIfSystemProperty(named= "java.version", matches = "17.0.2")
        void testJavaVersion() {
        }

        @Test
        @EnabledIfSystemProperty(named= "java.version", matches = ".*2.*")
        void testJavaVersionRegExp() {
        }
    }

    @Nested
    @DisplayName("Tests para comprobar habilitar tests en funcion de propiedades de sistema")
    class SystemPropertiesTest{
        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = "amd64")
        void testSolo32() {
        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testSolo64() {
        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")//Anadir a la configuracion de la clase de pruebas
        void testDev() {
        }
    }

    @Nested
    @DisplayName("Tests para comprobar habilitar tests en funcion de variables de entorno")
    class VariableAmbientetest{
        //test lanzados en funcion de la s variables de ambiente
        @Test
        @Disabled
        void imprimirVariblesAmbiente() {
            Map<String, String> paramEnv =System.getenv();
            paramEnv.forEach((k,v) -> System.out.println(k +" : "+v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named="USERDOMAIN_ROAMINGPROFILE",matches="DESKTOP-GKDARO5")
        void testJavaHome() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named="ENVIROMENT", matches="dev")//Anadir a la configuracion de la clase de pruebas
        void name() {
        }
    }

    @Nested
    @DisplayName("Tests para comprobar test de repeticion")
    class RepeatedTestClass{
        @RepeatedTest(value = 5,name = "{displayName} - Repeticion de test : {currentRepetition} de {totalRepetitions}")
        void testRepeticion() {
            cuentaOrigen.debito(new BigDecimal(100));
            assertNotNull(cuentaOrigen.getSaldo());
            assertEquals(156887.123, cuentaOrigen.getSaldo().doubleValue());
            assertEquals("156887.123", cuentaOrigen.getSaldo().toPlainString());
        }
    }

    @Nested
    @DisplayName("Test para comprobar los tests parametrizados")
    class ParametrizedTestsClass{
        @ParameterizedTest
        @ValueSource(strings = {"100000000","2000000","30000000","40000000","5000000"})
        void testParametrizadoSaldoCuenta(String monto) {
            Exception ex = assertThrows(DineroInsuficienteException.class, () -> {
                cuentaOrigen.debito(new BigDecimal(monto));
            },"Dinero insuficiente");
            String actual =ex.getMessage();
            String esperado ="Dinero insuficiente";
            assertEquals(esperado, actual);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @ValueSource(doubles = {100000000,2000000,30000000,40000000,5000000})
        void testParametrizadoSaldoCuenta1(Double monto) {
            Exception ex = assertThrows(DineroInsuficienteException.class, () -> {
                cuentaOrigen.debito(new BigDecimal(monto));
            },"Dinero insuficiente");
            String actual =ex.getMessage();
            String esperado ="Dinero insuficiente";
            assertEquals(esperado, actual);
        }
    }

    @Nested
    @DisplayName("Test para mostrar las distintas fuentes de datos para tests parametrizados")
    class SourcesForParametrizedTestsClass{
        @ParameterizedTest
        @CsvSource({"1,100000000","2,2000000","3,30000000","4,40000000","5,5000000"})
        void testParametrizadoSaldoCsvSource(String index, String monto) {
            System.out.println(index + " : " + monto);
            Exception ex = assertThrows(DineroInsuficienteException.class, () -> {
                cuentaOrigen.debito(new BigDecimal(monto));
            },"Dinero insuficiente");
            String actual =ex.getMessage();
            String esperado ="Dinero insuficiente";
            assertEquals(esperado, actual);
        }

        @ParameterizedTest
        @CsvFileSource(resources="/data.csv")
        void testParametrizadoSaldoCsvFileSource(String monto) {

            Exception ex = assertThrows(DineroInsuficienteException.class, () -> {
                cuentaOrigen.debito(new BigDecimal(monto));
            },"Dinero insuficiente");
            String actual =ex.getMessage();
            String esperado ="Dinero insuficiente";
            assertEquals(esperado, actual);
        }

        @ParameterizedTest
        @MethodSource("montoList")
        void testParametrizadoSaldoMethodSource(String monto) {

            Exception ex = assertThrows(DineroInsuficienteException.class, () -> {
                cuentaOrigen.debito(new BigDecimal(monto));
            },"Dinero insuficiente");
            String actual =ex.getMessage();
            String esperado ="Dinero insuficiente";
            assertEquals(esperado, actual);
        }

        static List<String> montoList(){
            return Arrays.asList("100000000","2000000","30000000","40000000","5000000");
        }


    }


}