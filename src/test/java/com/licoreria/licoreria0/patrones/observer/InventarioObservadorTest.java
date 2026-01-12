package com.licoreria.licoreria0.patrones.observer;

import com.licoreria.licoreria0.modelo.*;
import com.licoreria.licoreria0.repositorio.ProductoRepositorio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas Unitarias de Caja Blanca para InventarioObservador (Observer Pattern)
 * 
 * Análisis de flujo de control:
 * - Iteración sobre detalles de venta
 * - Actualización de stock por producto
 * - Validación de stock negativo
 * - Manejo de productos no encontrados
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("InventarioObservador - Pruebas de Caja Blanca")
class InventarioObservadorTest {

    @Mock
    private ProductoRepositorio productoRepositorio;

    @InjectMocks
    private InventarioObservador observador;

    private Venta ventaMock;
    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        // Crear productos mock
        producto1 = new Producto();
        producto1.setIdProducto(1L);
        producto1.setNombre("Cerveza");
        producto1.setStock(50);

        producto2 = new Producto();
        producto2.setIdProducto(2L);
        producto2.setNombre("Vino");
        producto2.setStock(30);

        // Crear venta mock con detalles
        ventaMock = new Venta();
        ventaMock.setIdVenta(1L);
        ventaMock.setDetalles(new ArrayList<>());
    }

    @Test
    @DisplayName("CP01: Debe actualizar stock correctamente para un producto")
    void testNotificarVentaUnProducto() throws Exception {
        // Arrange - Camino: 1 detalle, stock suficiente
        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto1);
        detalle.setCantidad(5);
        ventaMock.getDetalles().add(detalle);

        when(productoRepositorio.findById(1L)).thenReturn(Optional.of(producto1));
        when(productoRepositorio.save(any(Producto.class))).thenReturn(producto1);

        // Act
        observador.notificarVenta(ventaMock);

        // Assert
        assertEquals(45, producto1.getStock()); // 50 - 5
        verify(productoRepositorio, times(1)).findById(1L);
        verify(productoRepositorio, times(1)).save(producto1);
    }

    @Test
    @DisplayName("CP02: Debe actualizar stock para múltiples productos")
    void testNotificarVentaMultiplesProductos() throws Exception {
        // Arrange - Camino: múltiples detalles
        DetalleVenta detalle1 = new DetalleVenta();
        detalle1.setProducto(producto1);
        detalle1.setCantidad(10);

        DetalleVenta detalle2 = new DetalleVenta();
        detalle2.setProducto(producto2);
        detalle2.setCantidad(5);

        ventaMock.getDetalles().add(detalle1);
        ventaMock.getDetalles().add(detalle2);

        when(productoRepositorio.findById(1L)).thenReturn(Optional.of(producto1));
        when(productoRepositorio.findById(2L)).thenReturn(Optional.of(producto2));
        when(productoRepositorio.save(any(Producto.class))).thenReturn(producto1, producto2);

        // Act
        observador.notificarVenta(ventaMock);

        // Assert
        assertEquals(40, producto1.getStock()); // 50 - 10
        assertEquals(25, producto2.getStock()); // 30 - 5
        verify(productoRepositorio, times(2)).save(any(Producto.class));
    }

    @Test
    @DisplayName("CP03: Debe lanzar excepción cuando producto no existe")
    void testNotificarVentaProductoNoExiste() {
        // Arrange - Camino: producto no encontrado
        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto1);
        detalle.setCantidad(5);
        ventaMock.getDetalles().add(detalle);

        when(productoRepositorio.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            observador.notificarVenta(ventaMock);
        });

        assertTrue(exception.getMessage().contains("no encontrado"));
        verify(productoRepositorio, never()).save(any(Producto.class));
    }

    @Test
    @DisplayName("CP04: Debe lanzar excepción cuando stock resultante es negativo")
    void testNotificarVentaStockNegativo() {
        // Arrange - Camino: nuevoStock < 0
        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto1);
        detalle.setCantidad(60); // Mayor que stock de 50
        ventaMock.getDetalles().add(detalle);

        when(productoRepositorio.findById(1L)).thenReturn(Optional.of(producto1));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            observador.notificarVenta(ventaMock);
        });

        assertTrue(exception.getMessage().contains("Stock negativo"));
        verify(productoRepositorio, never()).save(any(Producto.class));
    }

    @Test
    @DisplayName("CP05: Debe actualizar stock a cero cuando cantidad vendida es exacta")
    void testNotificarVentaStockCero() throws Exception {
        // Arrange - Camino: nuevoStock == 0
        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto1);
        detalle.setCantidad(50); // Exactamente el stock disponible
        ventaMock.getDetalles().add(detalle);

        when(productoRepositorio.findById(1L)).thenReturn(Optional.of(producto1));
        when(productoRepositorio.save(any(Producto.class))).thenReturn(producto1);

        // Act
        observador.notificarVenta(ventaMock);

        // Assert
        assertEquals(0, producto1.getStock());
        verify(productoRepositorio, times(1)).save(producto1);
    }

    @Test
    @DisplayName("CP06: Debe manejar venta sin detalles")
    void testNotificarVentaSinDetalles() throws Exception {
        // Arrange - Camino: lista vacía (bucle no se ejecuta)
        ventaMock.setDetalles(new ArrayList<>());

        // Act
        observador.notificarVenta(ventaMock);

        // Assert
        verify(productoRepositorio, never()).findById(any(Long.class));
        verify(productoRepositorio, never()).save(any(Producto.class));
    }

    @Test
    @DisplayName("CP07: Debe actualizar stock correctamente con cantidad 1")
    void testNotificarVentaCantidadUno() throws Exception {
        // Arrange - Camino: cantidad mínima
        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto1);
        detalle.setCantidad(1);
        ventaMock.getDetalles().add(detalle);

        when(productoRepositorio.findById(1L)).thenReturn(Optional.of(producto1));
        when(productoRepositorio.save(any(Producto.class))).thenReturn(producto1);

        // Act
        observador.notificarVenta(ventaMock);

        // Assert
        assertEquals(49, producto1.getStock()); // 50 - 1
        verify(productoRepositorio, times(1)).save(producto1);
    }

    @Test
    @DisplayName("CP08: Debe refrescar producto desde BD antes de actualizar")
    void testNotificarVentaRefrescaProducto() throws Exception {
        // Arrange - Verificar que se obtiene el producto actualizado de la BD
        Producto productoActualizado = new Producto();
        productoActualizado.setIdProducto(1L);
        productoActualizado.setNombre("Cerveza");
        productoActualizado.setStock(45); // Stock diferente al original

        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto1); // Producto con stock 50
        detalle.setCantidad(5);
        ventaMock.getDetalles().add(detalle);

        when(productoRepositorio.findById(1L)).thenReturn(Optional.of(productoActualizado));
        when(productoRepositorio.save(any(Producto.class))).thenReturn(productoActualizado);

        // Act
        observador.notificarVenta(ventaMock);

        // Assert
        assertEquals(40, productoActualizado.getStock()); // 45 - 5 (usa el stock actualizado)
        verify(productoRepositorio, times(1)).findById(1L);
    }

    @Test
    @DisplayName("CP09: Debe implementar interfaz ObservadorVenta")
    void testImplementaInterfaz() {
        // Assert
        assertTrue(observador instanceof ObservadorVenta);
    }
}
