package com.licoreria.licoreria0.patrones.facade;

import com.licoreria.licoreria0.modelo.*;
import com.licoreria.licoreria0.repositorio.*;
import com.licoreria.licoreria0.patrones.factory.ProductoFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas Unitarias para Facade - Métodos de Productos (Caja Blanca)
 * Usa Mockito para aislar la lógica del Facade de las dependencias
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Facade Productos - Pruebas Unitarias (Caja Blanca)")
class FacadeProductoTest {

    @Mock
    private ProductoRepositorio productoRepositorio;

    @Mock
    private ProveedorRepositorio proveedorRepositorio;

    @Mock
    private DetalleCompraRepositorio detalleCompraRepositorio;

    // Usamos la instancia real en lugar del Mock porque Mockito tiene problemas con
    // esta clase en Java 25+
    private ProductoFactory productoFactory = new ProductoFactory();

    @InjectMocks
    private Facade facade;

    private Proveedor proveedorMock;
    private Producto productoMock;

    @BeforeEach
    void setUp() {
        proveedorMock = new Proveedor();
        proveedorMock.setIdProveedor(1L);
        proveedorMock.setNombre("Proveedor Test");
        proveedorMock.setRuc("12345678901");

        productoMock = new Producto();
        productoMock.setIdProducto(1L);
        productoMock.setNombre("Ron Test");
        productoMock.setPrecioCompra(15.0);
        productoMock.setPrecioVenta(20.0);
        productoMock.setStock(10);
        productoMock.setProveedor(proveedorMock);

        // Inyectar manualmente la factoría real
        facade.setProductoFactory(productoFactory);
    }

    @Test
    @DisplayName("Debe registrar producto exitosamente cuando el proveedor existe")
    void testRegistrarProductoExitoso() throws Exception {
        // Arrange
        Producto productoEntrada = new Producto();
        productoEntrada.setNombre("Vodka");
        productoEntrada.setPrecioCompra(25.0);
        productoEntrada.setPrecioVenta(35.0);
        productoEntrada.setStock(5);

        when(proveedorRepositorio.findById(1L)).thenReturn(Optional.of(proveedorMock));
        // No necesitamos stubear productoFactory porque es real
        when(productoRepositorio.save(any(Producto.class))).thenReturn(productoMock);

        // Act
        Producto resultado = facade.registrarProducto(productoEntrada, 1L);

        // Assert
        assertNotNull(resultado);
        verify(proveedorRepositorio, times(1)).findById(1L);
        // Usamos any() porque la factory crea una nueva instancia
        verify(productoRepositorio, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el proveedor no existe")
    void testRegistrarProductoProveedorNoExiste() {
        // Arrange
        Producto productoEntrada = new Producto();
        productoEntrada.setNombre("Vodka");
        productoEntrada.setPrecioCompra(25.0);
        productoEntrada.setPrecioVenta(35.0);
        productoEntrada.setStock(5);

        when(proveedorRepositorio.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            facade.registrarProducto(productoEntrada, 999L);
        });

        assertTrue(exception.getMessage().contains("Proveedor no encontrado"));
        // No se debe llamar a crearProducto ni save cuando el proveedor no existe
        verify(productoRepositorio, never()).save(any(Producto.class));
    }

    @Test
    @DisplayName("Debe actualizar producto exitosamente")
    void testActualizarProductoExitoso() throws Exception {
        // Arrange
        Producto productoActualizado = new Producto();
        productoActualizado.setIdProducto(1L);
        productoActualizado.setNombre("Ron Actualizado");
        productoActualizado.setPrecioCompra(18.0);
        productoActualizado.setPrecioVenta(25.0);
        productoActualizado.setStock(15);

        when(productoRepositorio.findById(1L)).thenReturn(Optional.of(productoMock));
        when(proveedorRepositorio.findById(1L)).thenReturn(Optional.of(proveedorMock));
        when(productoRepositorio.save(any(Producto.class))).thenReturn(productoMock);

        // Act
        Producto resultado = facade.actualizarProducto(productoActualizado, 1L);

        // Assert
        assertNotNull(resultado);
        verify(productoRepositorio, times(1)).findById(1L);
        verify(proveedorRepositorio, times(1)).findById(1L);
        verify(productoRepositorio, times(1)).save(productoMock);
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar producto que no existe")
    void testActualizarProductoNoExiste() {
        // Arrange
        Producto productoActualizado = new Producto();
        productoActualizado.setIdProducto(999L);

        when(productoRepositorio.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            facade.actualizarProducto(productoActualizado, 1L);
        });

        assertTrue(exception.getMessage().contains("Producto no encontrado"));
        verify(productoRepositorio, never()).save(any(Producto.class));
    }

    @Test
    @DisplayName("Debe eliminar producto exitosamente cuando stock es cero y no tiene compras")
    void testEliminarProductoExitoso() throws Exception {
        // Arrange
        productoMock.setStock(0);
        when(productoRepositorio.findById(1L)).thenReturn(Optional.of(productoMock));
        when(detalleCompraRepositorio.existsByProducto_IdProducto(1L)).thenReturn(false);

        // Act
        facade.eliminarProducto(1L);

        // Assert
        verify(productoRepositorio, times(1)).findById(1L);
        verify(detalleCompraRepositorio, times(1)).existsByProducto_IdProducto(1L);
        verify(productoRepositorio, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar producto con stock mayor a cero")
    void testEliminarProductoConStock() {
        // Arrange
        productoMock.setStock(10);
        when(productoRepositorio.findById(1L)).thenReturn(Optional.of(productoMock));
        when(detalleCompraRepositorio.existsByProducto_IdProducto(1L)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            facade.eliminarProducto(1L);
        });

        assertTrue(exception.getMessage().contains("No se puede eliminar un producto con stock"));
        verify(productoRepositorio, never()).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar producto con compras asociadas")
    void testEliminarProductoConCompras() {
        // Arrange
        productoMock.setStock(0);
        when(productoRepositorio.findById(1L)).thenReturn(Optional.of(productoMock));
        when(detalleCompraRepositorio.existsByProducto_IdProducto(1L)).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            facade.eliminarProducto(1L);
        });

        assertTrue(exception.getMessage().contains("existe una compra con este producto"));
        verify(productoRepositorio, never()).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("Debe actualizar stock correctamente sumando cantidad")
    void testActualizarStockProducto() throws Exception {
        // Arrange
        when(productoRepositorio.findById(1L)).thenReturn(Optional.of(productoMock));
        when(productoRepositorio.save(any(Producto.class))).thenReturn(productoMock);

        // Act
        Producto resultado = facade.actualizarStockProducto(1L, 5);

        // Assert
        assertNotNull(resultado);
        verify(productoRepositorio, times(1)).findById(1L);
        verify(productoRepositorio, times(1)).save(productoMock);
        assertEquals(15, productoMock.getStock()); // 10 + 5
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando actualización resulta en stock negativo")
    void testActualizarStockNegativo() {
        // Arrange
        productoMock.setStock(5);
        when(productoRepositorio.findById(1L)).thenReturn(Optional.of(productoMock));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            facade.actualizarStockProducto(1L, -10); // 5 - 10 = -5
        });

        assertTrue(exception.getMessage().contains("Stock insuficiente"));
        verify(productoRepositorio, never()).save(any(Producto.class));
    }

    @Test
    @DisplayName("Debe permitir reducir stock cuando hay suficiente")
    void testReducirStockValido() throws Exception {
        // Arrange
        productoMock.setStock(10);
        when(productoRepositorio.findById(1L)).thenReturn(Optional.of(productoMock));
        when(productoRepositorio.save(any(Producto.class))).thenReturn(productoMock);

        // Act
        Producto resultado = facade.actualizarStockProducto(1L, -5);

        // Assert
        assertNotNull(resultado);
        assertEquals(5, productoMock.getStock()); // 10 - 5
        verify(productoRepositorio, times(1)).save(productoMock);
    }
}
