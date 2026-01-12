package com.licoreria.licoreria0.patrones.strategy;

import com.licoreria.licoreria0.modelo.Venta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas Unitarias de Caja Blanca para PagoEfectivoStrategy
 * 
 * Análisis de flujo:
 * - Procesamiento de pago en efectivo
 * - Validación de nombre de estrategia
 */
@DisplayName("PagoEfectivoStrategy - Pruebas de Caja Blanca")
class PagoEfectivoStrategyTest {

    private PagoEfectivoStrategy estrategia;
    private Venta ventaMock;

    @BeforeEach
    void setUp() {
        estrategia = new PagoEfectivoStrategy();

        ventaMock = new Venta();
        ventaMock.setIdVenta(1L);
        ventaMock.setTotal(100.0);
    }

    @Test
    @DisplayName("CP01: Debe procesar pago en efectivo correctamente")
    void testProcesarPagoEfectivo() {
        // Act - No debe lanzar excepciones
        assertDoesNotThrow(() -> {
            estrategia.procesarPago(ventaMock, 100.0);
        });
    }

    @Test
    @DisplayName("CP02: Debe procesar pago con monto diferente al total")
    void testProcesarPagoMontoDiferente() {
        // Act - Simular pago con cambio
        assertDoesNotThrow(() -> {
            estrategia.procesarPago(ventaMock, 150.0);
        });
    }

    @Test
    @DisplayName("CP03: Debe procesar pago con monto cero")
    void testProcesarPagoMontoCero() {
        // Act
        assertDoesNotThrow(() -> {
            estrategia.procesarPago(ventaMock, 0.0);
        });
    }

    @Test
    @DisplayName("CP04: Debe retornar nombre correcto de la estrategia")
    void testObtenerNombre() {
        // Act
        String nombre = estrategia.obtenerNombre();

        // Assert
        assertEquals("EFECTIVO", nombre);
    }

    @Test
    @DisplayName("CP05: Debe implementar interfaz MetodoPagoStrategy")
    void testImplementaInterfaz() {
        // Assert
        assertTrue(estrategia instanceof MetodoPagoStrategy);
    }

    @Test
    @DisplayName("CP06: Debe procesar pago con venta null sin errores")
    void testProcesarPagoVentaNull() {
        // Act - Aunque no es un caso de uso real, probamos robustez
        assertDoesNotThrow(() -> {
            estrategia.procesarPago(null, 100.0);
        });
    }
}
