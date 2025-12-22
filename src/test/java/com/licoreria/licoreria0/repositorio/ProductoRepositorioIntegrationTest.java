package com.licoreria.licoreria0.repositorio;

import com.licoreria.licoreria0.modelo.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración para ProductoRepositorio (Caja Gris)
 * Verifica la persistencia y relaciones con la base de datos
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("ProductoRepositorio - Pruebas de Integración (Caja Gris)")
class ProductoRepositorioIntegrationTest {

    @org.springframework.boot.test.mock.mockito.MockBean
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    private Proveedor proveedorTest;

    @BeforeEach
    void setUp() {
        // Limpiar datos previos
        productoRepositorio.deleteAll();
        proveedorRepositorio.deleteAll();

        // Crear proveedor de prueba
        proveedorTest = new Proveedor();
        proveedorTest.setNombre("Distribuidora Test");
        proveedorTest.setRuc("12345678901");
        proveedorTest.setTelefono("987654321");
        proveedorTest.setDireccion("Av. Test 123");
        proveedorTest.setCorreo("test@proveedor.com");
        proveedorTest = proveedorRepositorio.save(proveedorTest);
    }

    @Test
    @DisplayName("Debe guardar y recuperar producto correctamente")
    void testGuardarYRecuperarProducto() {
        // Arrange
        Producto producto = new Producto();
        producto.setNombre("Ron Test");
        producto.setPrecioCompra(15.0);
        producto.setPrecioVenta(20.0);
        producto.setStock(10);
        producto.setProveedor(proveedorTest);

        // Act
        Producto productoGuardado = productoRepositorio.save(producto);

        // Assert
        assertNotNull(productoGuardado.getIdProducto(), "El ID debe ser generado automáticamente");

        Optional<Producto> productoRecuperado = productoRepositorio.findById(productoGuardado.getIdProducto());
        assertTrue(productoRecuperado.isPresent(), "El producto debe existir en la BD");
        assertEquals("Ron Test", productoRecuperado.get().getNombre());
    }

    @Test
    @DisplayName("Debe mantener relación ManyToOne con Proveedor (EAGER)")
    void testRelacionConProveedor() {
        // Arrange
        Producto producto = new Producto();
        producto.setNombre("Vodka Test");
        producto.setPrecioCompra(25.0);
        producto.setPrecioVenta(35.0);
        producto.setStock(5);
        producto.setProveedor(proveedorTest);

        // Act
        Producto productoGuardado = productoRepositorio.save(producto);

        // Limpiar caché de JPA para forzar carga desde BD
        productoRepositorio.flush();

        Producto productoRecuperado = productoRepositorio.findById(productoGuardado.getIdProducto()).orElseThrow();

        // Assert
        assertNotNull(productoRecuperado.getProveedor(), "El proveedor debe estar cargado (EAGER)");
        assertEquals(proveedorTest.getIdProveedor(), productoRecuperado.getProveedor().getIdProveedor());
        assertEquals("Distribuidora Test", productoRecuperado.getProveedor().getNombre());
    }

    @Test
    @DisplayName("Debe actualizar producto correctamente")
    void testActualizarProducto() {
        // Arrange
        Producto producto = new Producto();
        producto.setNombre("Gin Original");
        producto.setPrecioCompra(20.0);
        producto.setPrecioVenta(30.0);
        producto.setStock(8);
        producto.setProveedor(proveedorTest);
        producto = productoRepositorio.save(producto);

        // Act
        producto.setNombre("Gin Actualizado");
        producto.setPrecioVenta(35.0);
        producto.setStock(12);
        Producto productoActualizado = productoRepositorio.save(producto);

        // Assert
        Producto productoRecuperado = productoRepositorio.findById(productoActualizado.getIdProducto()).orElseThrow();
        assertEquals("Gin Actualizado", productoRecuperado.getNombre());
        assertEquals(35.0, productoRecuperado.getPrecioVenta());
        assertEquals(12, productoRecuperado.getStock());
    }

    @Test
    @DisplayName("Debe eliminar producto correctamente")
    void testEliminarProducto() {
        // Arrange
        Producto producto = new Producto();
        producto.setNombre("Producto a Eliminar");
        producto.setPrecioCompra(10.0);
        producto.setPrecioVenta(15.0);
        producto.setStock(0);
        producto.setProveedor(proveedorTest);
        producto = productoRepositorio.save(producto);

        Long idProducto = producto.getIdProducto();

        // Act
        productoRepositorio.deleteById(idProducto);

        // Assert
        Optional<Producto> productoEliminado = productoRepositorio.findById(idProducto);
        assertFalse(productoEliminado.isPresent(), "El producto no debe existir después de eliminarlo");
    }

    @Test
    @DisplayName("Debe listar todos los productos correctamente")
    void testListarTodosLosProductos() {
        // Arrange
        Producto producto1 = new Producto("Ron", 15.0, 20.0, 10, proveedorTest);
        Producto producto2 = new Producto("Vodka", 25.0, 35.0, 5, proveedorTest);
        Producto producto3 = new Producto("Gin", 30.0, 40.0, 8, proveedorTest);

        productoRepositorio.save(producto1);
        productoRepositorio.save(producto2);
        productoRepositorio.save(producto3);

        // Act
        List<Producto> productos = productoRepositorio.findAll();

        // Assert
        assertEquals(3, productos.size(), "Debe haber 3 productos en la BD");
    }

    @Test
    @DisplayName("Debe persistir valores decimales correctamente")
    void testValoresDecimales() {
        // Arrange
        Producto producto = new Producto();
        producto.setNombre("Whisky");
        producto.setPrecioCompra(45.99);
        producto.setPrecioVenta(65.50);
        producto.setStock(3);
        producto.setProveedor(proveedorTest);

        // Act
        Producto productoGuardado = productoRepositorio.save(producto);
        Producto productoRecuperado = productoRepositorio.findById(productoGuardado.getIdProducto()).orElseThrow();

        // Assert
        assertEquals(45.99, productoRecuperado.getPrecioCompra(), 0.001);
        assertEquals(65.50, productoRecuperado.getPrecioVenta(), 0.001);
    }

    @Test
    @DisplayName("Debe manejar stock cero correctamente")
    void testStockCero() {
        // Arrange
        Producto producto = new Producto();
        producto.setNombre("Producto Sin Stock");
        producto.setPrecioCompra(10.0);
        producto.setPrecioVenta(15.0);
        producto.setStock(0);
        producto.setProveedor(proveedorTest);

        // Act
        Producto productoGuardado = productoRepositorio.save(producto);
        Producto productoRecuperado = productoRepositorio.findById(productoGuardado.getIdProducto()).orElseThrow();

        // Assert
        assertEquals(0, productoRecuperado.getStock());
    }
}
