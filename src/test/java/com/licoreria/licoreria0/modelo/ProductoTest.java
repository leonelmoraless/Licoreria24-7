package com.licoreria.licoreria0.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas Unitarias para el modelo Producto (Caja Blanca)
 * Verifica getters, setters y métodos del modelo
 */
@DisplayName("Producto - Pruebas Unitarias (Caja Blanca)")
class ProductoTest {

    private Producto producto;
    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        producto = new Producto();

        proveedor = new Proveedor();
        proveedor.setIdProveedor(1L);
        proveedor.setNombre("Distribuidora ABC");
        proveedor.setRuc("20123456789");
    }

    @Test
    @DisplayName("Debe crear producto vacío con constructor por defecto")
    void testConstructorPorDefecto() {
        // Arrange & Act
        Producto nuevoProducto = new Producto();

        // Assert
        assertNotNull(nuevoProducto, "El producto no debe ser nulo");
        assertNull(nuevoProducto.getIdProducto(), "El ID debe ser nulo inicialmente");
        assertNull(nuevoProducto.getNombre(), "El nombre debe ser nulo inicialmente");
    }

    @Test
    @DisplayName("Debe crear producto con constructor parametrizado")
    void testConstructorParametrizado() {
        // Arrange & Act
        Producto nuevoProducto = new Producto("Tequila", 30.0, 45.0, 8, proveedor);

        // Assert
        assertNotNull(nuevoProducto);
        assertEquals("Tequila", nuevoProducto.getNombre());
        assertEquals(30.0, nuevoProducto.getPrecioCompra());
        assertEquals(45.0, nuevoProducto.getPrecioVenta());
        assertEquals(8, nuevoProducto.getStock());
        assertEquals(proveedor, nuevoProducto.getProveedor());
    }

    @Test
    @DisplayName("Debe establecer y obtener ID del producto correctamente")
    void testGetSetIdProducto() {
        // Act
        producto.setIdProducto(100L);

        // Assert
        assertEquals(100L, producto.getIdProducto());
    }

    @Test
    @DisplayName("Debe establecer y obtener nombre del producto correctamente")
    void testGetSetNombre() {
        // Act
        producto.setNombre("Gin Bombay");

        // Assert
        assertEquals("Gin Bombay", producto.getNombre());
    }

    @Test
    @DisplayName("Debe establecer y obtener precio de compra correctamente")
    void testGetSetPrecioCompra() {
        // Act
        producto.setPrecioCompra(25.50);

        // Assert
        assertEquals(25.50, producto.getPrecioCompra());
    }

    @Test
    @DisplayName("Debe establecer y obtener precio de venta correctamente")
    void testGetSetPrecioVenta() {
        // Act
        producto.setPrecioVenta(35.00);

        // Assert
        assertEquals(35.00, producto.getPrecioVenta());
    }

    @Test
    @DisplayName("Debe establecer y obtener stock correctamente")
    void testGetSetStock() {
        // Act
        producto.setStock(50);

        // Assert
        assertEquals(50, producto.getStock());
    }

    @Test
    @DisplayName("Debe establecer y obtener proveedor correctamente")
    void testGetSetProveedor() {
        // Act
        producto.setProveedor(proveedor);

        // Assert
        assertEquals(proveedor, producto.getProveedor());
        assertEquals("Distribuidora ABC", producto.getProveedor().getNombre());
    }

    @Test
    @DisplayName("Debe retornar ID del proveedor cuando el proveedor no es nulo")
    void testGetProveedorIdConProveedorValido() {
        // Arrange
        producto.setProveedor(proveedor);

        // Act
        Long proveedorId = producto.getProveedorId();

        // Assert
        assertNotNull(proveedorId, "El ID del proveedor no debe ser nulo");
        assertEquals(1L, proveedorId, "El ID del proveedor debe ser 1");
    }

    @Test
    @DisplayName("Debe retornar null cuando el proveedor es nulo")
    void testGetProveedorIdConProveedorNulo() {
        // Arrange
        producto.setProveedor(null);

        // Act
        Long proveedorId = producto.getProveedorId();

        // Assert
        assertNull(proveedorId, "El ID del proveedor debe ser nulo cuando no hay proveedor");
    }

    @Test
    @DisplayName("Debe manejar correctamente la actualización de proveedor")
    void testActualizarProveedor() {
        // Arrange
        producto.setProveedor(proveedor);
        assertEquals(1L, producto.getProveedorId());

        Proveedor nuevoProveedor = new Proveedor();
        nuevoProveedor.setIdProveedor(2L);
        nuevoProveedor.setNombre("Distribuidora XYZ");

        // Act
        producto.setProveedor(nuevoProveedor);

        // Assert
        assertEquals(2L, producto.getProveedorId());
        assertEquals("Distribuidora XYZ", producto.getProveedor().getNombre());
    }

    @Test
    @DisplayName("Debe manejar valores de stock cero")
    void testStockCero() {
        // Act
        producto.setStock(0);

        // Assert
        assertEquals(0, producto.getStock());
    }

    @Test
    @DisplayName("Debe manejar precios con decimales")
    void testPreciosConDecimales() {
        // Act
        producto.setPrecioCompra(12.99);
        producto.setPrecioVenta(19.99);

        // Assert
        assertEquals(12.99, producto.getPrecioCompra(), 0.001);
        assertEquals(19.99, producto.getPrecioVenta(), 0.001);
    }
}
