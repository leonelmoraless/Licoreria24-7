package com.licoreria.licoreria0.patrones.factory;

import com.licoreria.licoreria0.modelo.Producto;
import com.licoreria.licoreria0.modelo.Proveedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas Unitarias para ProductoFactory (Caja Blanca)
 * Verifica la lógica interna y las validaciones del Factory
 */
@DisplayName("ProductoFactory - Pruebas Unitarias (Caja Blanca)")
class ProductoFactoryTest {

    private ProductoFactory productoFactory;
    private Proveedor proveedorValido;

    @BeforeEach
    void setUp() {
        productoFactory = new ProductoFactory();

        // Crear un proveedor válido para las pruebas
        proveedorValido = new Proveedor();
        proveedorValido.setIdProveedor(1L);
        proveedorValido.setNombre("Proveedor Test");
        proveedorValido.setRuc("12345678901");
    }

    @Test
    @DisplayName("Debe crear producto exitosamente con datos válidos")
    void testCrearProductoExitoso() throws Exception {
        // Arrange
        String nombre = "Ron Bacardi";
        Double precioCompra = 15.50;
        Double precioVenta = 20.00;
        Integer stock = 10;

        // Act
        Producto producto = productoFactory.crearProducto(nombre, precioCompra, precioVenta, stock, proveedorValido);

        // Assert
        assertNotNull(producto, "El producto no debe ser nulo");
        assertEquals("Ron Bacardi", producto.getNombre(), "El nombre debe coincidir");
        assertEquals(15.50, producto.getPrecioCompra(), "El precio de compra debe coincidir");
        assertEquals(20.00, producto.getPrecioVenta(), "El precio de venta debe coincidir");
        assertEquals(10, producto.getStock(), "El stock debe coincidir");
        assertEquals(proveedorValido, producto.getProveedor(), "El proveedor debe coincidir");
    }

    @Test
    @DisplayName("Debe eliminar espacios en blanco del nombre del producto")
    void testCrearProductoConNombreConEspacios() throws Exception {
        // Arrange
        String nombreConEspacios = "  Vodka Absolut  ";

        // Act
        Producto producto = productoFactory.crearProducto(nombreConEspacios, 25.0, 30.0, 5, proveedorValido);

        // Assert
        assertEquals("Vodka Absolut", producto.getNombre(), "El nombre debe estar sin espacios al inicio y final");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el nombre es nulo")
    void testCrearProductoConNombreNulo() {
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            productoFactory.crearProducto(null, 15.0, 20.0, 10, proveedorValido);
        });

        assertEquals("El nombre del producto no puede ser nulo o vacío.", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el nombre está vacío")
    void testCrearProductoConNombreVacio() {
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            productoFactory.crearProducto("   ", 15.0, 20.0, 10, proveedorValido);
        });

        assertEquals("El nombre del producto no puede ser nulo o vacío.", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el precio de compra es nulo")
    void testCrearProductoConPrecioCompraNulo() {
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            productoFactory.crearProducto("Cerveza", null, 20.0, 10, proveedorValido);
        });

        assertEquals("El precio de compra debe ser positivo.", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el precio de compra es cero")
    void testCrearProductoConPrecioCompraCero() {
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            productoFactory.crearProducto("Cerveza", 0.0, 20.0, 10, proveedorValido);
        });

        assertEquals("El precio de compra debe ser positivo.", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el precio de compra es negativo")
    void testCrearProductoConPrecioCompraNegativo() {
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            productoFactory.crearProducto("Cerveza", -5.0, 20.0, 10, proveedorValido);
        });

        assertEquals("El precio de compra debe ser positivo.", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el precio de venta es nulo")
    void testCrearProductoConPrecioVentaNulo() {
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            productoFactory.crearProducto("Cerveza", 15.0, null, 10, proveedorValido);
        });

        assertEquals("El precio de venta debe ser positivo.", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el precio de venta es cero")
    void testCrearProductoConPrecioVentaCero() {
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            productoFactory.crearProducto("Cerveza", 15.0, 0.0, 10, proveedorValido);
        });

        assertEquals("El precio de venta debe ser positivo.", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el precio de venta es negativo")
    void testCrearProductoConPrecioVentaNegativo() {
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            productoFactory.crearProducto("Cerveza", 15.0, -10.0, 10, proveedorValido);
        });

        assertEquals("El precio de venta debe ser positivo.", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el stock es nulo")
    void testCrearProductoConStockNulo() {
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            productoFactory.crearProducto("Cerveza", 15.0, 20.0, null, proveedorValido);
        });

        assertEquals("El stock no puede ser negativo.", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el stock es negativo")
    void testCrearProductoConStockNegativo() {
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            productoFactory.crearProducto("Cerveza", 15.0, 20.0, -5, proveedorValido);
        });

        assertEquals("El stock no puede ser negativo.", exception.getMessage());
    }

    @Test
    @DisplayName("Debe permitir crear producto con stock cero")
    void testCrearProductoConStockCero() throws Exception {
        // Act
        Producto producto = productoFactory.crearProducto("Cerveza", 15.0, 20.0, 0, proveedorValido);

        // Assert
        assertNotNull(producto);
        assertEquals(0, producto.getStock(), "El stock debe ser cero");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el proveedor es nulo")
    void testCrearProductoConProveedorNulo() {
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            productoFactory.crearProducto("Cerveza", 15.0, 20.0, 10, null);
        });

        assertEquals("El producto debe tener un proveedor asociado.", exception.getMessage());
    }

    @Test
    @DisplayName("REGLA DE NEGOCIO: Debe lanzar excepción cuando precio de venta es menor al precio de compra")
    void testCrearProductoConPrecioVentaMenorAlPrecioCompra() {
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            productoFactory.crearProducto("Cerveza", 20.0, 15.0, 10, proveedorValido);
        });

        assertEquals("El precio de venta no puede ser menor al precio de compra.", exception.getMessage());
    }

    @Test
    @DisplayName("REGLA DE NEGOCIO: Debe permitir crear producto cuando precio de venta es igual al precio de compra")
    void testCrearProductoConPrecioVentaIgualAlPrecioCompra() throws Exception {
        // Act
        Producto producto = productoFactory.crearProducto("Cerveza", 20.0, 20.0, 10, proveedorValido);

        // Assert
        assertNotNull(producto);
        assertEquals(20.0, producto.getPrecioCompra());
        assertEquals(20.0, producto.getPrecioVenta());
    }

    @Test
    @DisplayName("Debe crear producto con precio de venta mayor al precio de compra (caso normal)")
    void testCrearProductoConPrecioVentaMayorAlPrecioCompra() throws Exception {
        // Act
        Producto producto = productoFactory.crearProducto("Whisky", 50.0, 75.0, 5, proveedorValido);

        // Assert
        assertNotNull(producto);
        assertEquals(50.0, producto.getPrecioCompra());
        assertEquals(75.0, producto.getPrecioVenta());
        assertTrue(producto.getPrecioVenta() > producto.getPrecioCompra(),
                "El precio de venta debe ser mayor al precio de compra");
    }
}
