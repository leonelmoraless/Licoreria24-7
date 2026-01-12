package com.licoreria.licoreria0.patrones.builder;

import com.licoreria.licoreria0.modelo.Cliente;
import com.licoreria.licoreria0.modelo.Producto;
import com.licoreria.licoreria0.modelo.Venta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de Pruebas Unitarias para el Patrón Builder.
 * Objetivo: Validar la lógica matemática y las restricciones sin tocar la base
 * de datos.
 */
public class VentaBuilderTest {

    @Test
    public void testCalculoTotalCorrecto() throws Exception {
        // 1. Preparación (Arrange)
        Cliente clienteDummy = new Cliente();
        clienteDummy.setNombre("Test User");

        Producto producto1 = new Producto();
        producto1.setNombre("Cerveza");
        producto1.setStock(100); // Stock suficiente

        Producto producto2 = new Producto();
        producto2.setNombre("Vino");
        producto2.setStock(50); // Stock suficiente

        // 2. Ejecución (Act)
        VentaBuilder builder = new VentaBuilder();
        Venta venta = builder.conCliente(clienteDummy)
                // 2 Cervezas a $10.00 = $20.00
                .agregarDetalle(producto1, 2, 10.00, 0.0)
                // 1 Vino a $50.00 = $50.00
                .agregarDetalle(producto2, 1, 50.00, 0.0)
                .construir();

        // 3. Verificación (Assert)
        assertNotNull(venta);

        // Subtotal esperado: 20 + 50 = 70.0
        // IVA defecto: 15% -> 70 * 0.15 = 10.5
        // Total esperado: 70 + 10.5 = 80.5

        assertEquals(70.00, venta.getSubtotal(), 0.001, "El subtotal debe ser 70.00");
        assertEquals(10.5, venta.getMontoIva(), 0.001, "El IVA debe ser el 15% del subtotal");
        assertEquals(80.50, venta.getTotal(), 0.001, "El total debe incluir el IVA");
        assertEquals(2, venta.getDetalles().size(), "Debe tener 2 items en el detalle");
    }

    @Test
    public void testCalculoConDescuento() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setNombre("Test Descuento");
        Producto prod = new Producto();
        prod.setNombre("Item");
        prod.setStock(10);

        VentaBuilder builder = new VentaBuilder();
        Venta venta = builder.conCliente(cliente)
                // 2 items a $100 = $200. Descuento 10% -> 20. Subtotal item = 180.
                .agregarDetalle(prod, 2, 100.0, 10.0)
                .construir();

        // Subtotal venta = 180 (antes de IVA)
        // IVA default 15% de 180 = 27
        // Total = 180 + 27 = 207

        assertEquals(180.0, venta.getSubtotal(), 0.001);
        assertEquals(27.0, venta.getMontoIva(), 0.001);
        assertEquals(207.0, venta.getTotal(), 0.001);
    }

    @Test
    public void testCalculoConIvaPersonalizado() throws Exception {
        Cliente cliente = new Cliente();
        Producto prod = new Producto();
        prod.setStock(10);

        VentaBuilder builder = new VentaBuilder();
        Venta venta = builder.conCliente(cliente)
                .conIva(10.0) // 10% IVA
                // 1 item a $100. No descuento. Subtotal = 100.
                .agregarDetalle(prod, 1, 100.0, 0.0)
                .construir();

        // Subtotal = 100
        // IVA 10% = 10
        // Total = 110

        assertEquals(100.0, venta.getSubtotal(), 0.001);
        assertEquals(10.0, venta.getMontoIva(), 0.001);
        assertEquals(110.0, venta.getTotal(), 0.001);
    }

    @Test
    public void testErrorSinCliente() {
        // Intento de construir una venta sin asignar cliente
        VentaBuilder builder = new VentaBuilder();

        // Esperamos que lance una excepción
        Exception exception = assertThrows(Exception.class, () -> {
            builder.construir();
        });

        // Verificamos que el mensaje sea el correcto
        String mensajeEsperado = "No se puede crear una venta sin cliente.";
        String mensajeActual = exception.getMessage();

        assertTrue(mensajeActual.contains(mensajeEsperado), "El mensaje de error debe indicar falta de cliente");
    }

    @Test
    public void testValidacionStockInsuficiente() {
        // Preparar producto con poco stock
        Producto productoSinStock = new Producto();
        productoSinStock.setNombre("Whisky Caro");
        productoSinStock.setStock(2); // Solo quedan 2

        VentaBuilder builder = new VentaBuilder();
        builder.conCliente(new Cliente());

        // Intentamos comprar 5 (Supera stock de 2)
        Exception exception = assertThrows(Exception.class, () -> {
            builder.agregarDetalle(productoSinStock, 5, 100.00, 0.0);
        });

        assertTrue(exception.getMessage().contains("Stock insuficiente"), "Debe impedir agregar productos sin stock");
    }
}
