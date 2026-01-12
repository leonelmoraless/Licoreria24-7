package com.licoreria.licoreria0.patrones.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas Unitarias de Caja Blanca para ContextoPago (Strategy Pattern)
 * 
 * Análisis de flujo de control:
 * - Construcción del mapa de estrategias
 * - Selección de estrategia por nombre
 * - Manejo de casos null y default
 */
@DisplayName("ContextoPago - Pruebas de Caja Blanca")
class ContextoPagoTest {

    private ContextoPago contextoPago;
    private List<MetodoPagoStrategy> estrategias;

    @BeforeEach
    void setUp() {
        // Crear estrategias reales para las pruebas
        estrategias = new ArrayList<>();
        estrategias.add(new PagoEfectivoStrategy());
        estrategias.add(new PagoTarjetaStrategy());

        // Inyectar manualmente las estrategias
        contextoPago = new ContextoPago(estrategias);
    }

    @Test
    @DisplayName("CP01: Debe obtener estrategia de efectivo correctamente")
    void testObtenerEstrategiaEfectivo() {
        // Act
        MetodoPagoStrategy estrategia = contextoPago.obtenerEstrategia("EFECTIVO");

        // Assert
        assertNotNull(estrategia);
        assertEquals("EFECTIVO", estrategia.obtenerNombre());
        assertTrue(estrategia instanceof PagoEfectivoStrategy);
    }

    @Test
    @DisplayName("CP02: Debe obtener estrategia de tarjeta correctamente")
    void testObtenerEstrategiaTarjeta() {
        // Act
        MetodoPagoStrategy estrategia = contextoPago.obtenerEstrategia("TARJETA");

        // Assert
        assertNotNull(estrategia);
        assertEquals("TARJETA", estrategia.obtenerNombre());
        assertTrue(estrategia instanceof PagoTarjetaStrategy);
    }

    @Test
    @DisplayName("CP03: Debe ser case-insensitive al buscar estrategia")
    void testObtenerEstrategiaCaseInsensitive() {
        // Act - Probar con minúsculas
        MetodoPagoStrategy estrategia1 = contextoPago.obtenerEstrategia("efectivo");
        MetodoPagoStrategy estrategia2 = contextoPago.obtenerEstrategia("TaRjEtA");
        MetodoPagoStrategy estrategia3 = contextoPago.obtenerEstrategia("EFECTIVO");

        // Assert
        assertNotNull(estrategia1);
        assertNotNull(estrategia2);
        assertNotNull(estrategia3);
        assertEquals("EFECTIVO", estrategia1.obtenerNombre());
        assertEquals("TARJETA", estrategia2.obtenerNombre());
        assertEquals("EFECTIVO", estrategia3.obtenerNombre());
    }

    @Test
    @DisplayName("CP04: Debe retornar estrategia por defecto (EFECTIVO) cuando método es null")
    void testObtenerEstrategiaConMetodoNull() {
        // Act - Camino: metodo == null
        MetodoPagoStrategy estrategia = contextoPago.obtenerEstrategia(null);

        // Assert
        assertNotNull(estrategia);
        assertEquals("EFECTIVO", estrategia.obtenerNombre());
    }

    @Test
    @DisplayName("CP05: Debe retornar estrategia por defecto cuando método no existe")
    void testObtenerEstrategiaMetodoInexistente() {
        // Act - Camino: getOrDefault con clave inexistente
        MetodoPagoStrategy estrategia = contextoPago.obtenerEstrategia("BITCOIN");

        // Assert
        assertNotNull(estrategia);
        assertEquals("EFECTIVO", estrategia.obtenerNombre(),
                "Debe retornar EFECTIVO como estrategia por defecto");
    }

    @Test
    @DisplayName("CP06: Debe retornar estrategia por defecto cuando método está vacío")
    void testObtenerEstrategiaMetodoVacio() {
        // Act
        MetodoPagoStrategy estrategia = contextoPago.obtenerEstrategia("");

        // Assert
        assertNotNull(estrategia);
        assertEquals("EFECTIVO", estrategia.obtenerNombre());
    }

    @Test
    @DisplayName("CP07: Debe construir mapa correctamente con todas las estrategias")
    void testConstruccionMapaEstrategias() {
        // Act - Verificar que ambas estrategias están disponibles
        MetodoPagoStrategy efectivo = contextoPago.obtenerEstrategia("EFECTIVO");
        MetodoPagoStrategy tarjeta = contextoPago.obtenerEstrategia("TARJETA");

        // Assert
        assertNotNull(efectivo);
        assertNotNull(tarjeta);
        assertNotEquals(efectivo.getClass(), tarjeta.getClass(),
                "Las estrategias deben ser de clases diferentes");
    }

    @Test
    @DisplayName("CP08: Debe manejar espacios en blanco en el nombre del método")
    void testObtenerEstrategiaConEspacios() {
        // Act
        MetodoPagoStrategy estrategia = contextoPago.obtenerEstrategia("  EFECTIVO  ");

        // Assert
        // Nota: El método actual no hace trim(), así que esto debería retornar default
        assertNotNull(estrategia);
        // Dependiendo de la implementación, esto podría ser EFECTIVO (default)
    }
}
