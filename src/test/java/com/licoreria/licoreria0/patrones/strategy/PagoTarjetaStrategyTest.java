package com.licoreria.licoreria0.patrones.strategy;

import com.licoreria.licoreria0.modelo.Venta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas Unitarias de Caja Blanca para PagoTarjetaStrategy
 * 
 * Análisis de flujo:
 * - Procesamiento de pago con tarjeta
 * - Validación de nombre de estrategia
 */
@DisplayName("PagoTarjetaStrategy - Pruebas de Caja Blanca")
class PagoTarjetaStrategyTest {

    private PagoTarjetaStrategy estrategia;
    private Venta ventaMock;

    @BeforeEach
    void setUp() {
        estrategia = new PagoTarjetaStrategy();

        ventaMock = new Venta();
        ventaMock.setIdVenta(1L);
        ventaMock.setTotal(250.0);
    }

    @Test
    @DisplayName("CP01: Debe procesar pago con tarjeta correctamente")
    void testProcesarPagoTarjeta() {
        // Act - No debe lanzar excepciones
        assertDoesNotThrow(() -> {
            estrategia.procesarPago(ventaMock, 250.0);
        });
    }

    @Test
    @DisplayName("CP02: Debe procesar pago con monto exacto")
    void testProcesarPagoMontoExacto() {
        // Act
        assertDoesNotThrow(() -> {
            estrategia.procesarPago(ventaMock, ventaMock.getTotal());
        });
    }

    @Test
    @DisplayName("CP03: Debe procesar pago con monto alto")
    void testProcesarPagoMontoAlto() {
        // Act - Simular compra grande
        assertDoesNotThrow(() -> {
            estrategia.procesarPago(ventaMock, 10000.0);
        });
    }

    @Test
    @DisplayName("CP04: Debe retornar nombre correcto de la estrategia")
    void testObtenerNombre() {
        // Act
        String nombre = estrategia.obtenerNombre();

        // Assert
        assertEquals("TARJETA", nombre);
    }

    @Test
    @DisplayName("CP05: Debe implementar interfaz MetodoPagoStrategy")
    void testImplementaInterfaz() {
        // Assert
        assertTrue(estrategia instanceof MetodoPagoStrategy);
    }

    @Test
    @DisplayName("CP06: Debe procesar pago con decimales")
    void testProcesarPagoConDecimales() {
        // Act
        assertDoesNotThrow(() -> {
            estrategia.procesarPago(ventaMock, 99.99);
        });
    }

    @Test
    @DisplayName("CP07: Debe procesar pago con venta null sin errores")
    void testProcesarPagoVentaNull() {
        // Act
        assertDoesNotThrow(() -> {
            estrategia.procesarPago(null, 100.0);
        });
    }
}
