package com.licoreria.licoreria0.controlador;

import com.licoreria.licoreria0.modelo.*;
import com.licoreria.licoreria0.repositorio.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.licoreria.licoreria0.config.TestSecurityConfig;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración para ControladorProducto (Caja Gris)
 * Verifica la interacción entre Controller → Facade → Factory → Repository → BD
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@Transactional
@DisplayName("ControladorProducto - Pruebas de Integración (Caja Gris)")
class ControladorProductoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
    @DisplayName("Debe mostrar página de productos correctamente")
    void testMostrarPaginaProductos() throws Exception {
        mockMvc.perform(get("/productos")
                .with(user("admin@test.com").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("productos"))
                .andExpect(model().attributeExists("listaProductos"))
                .andExpect(model().attributeExists("nuevoProducto"))
                .andExpect(model().attributeExists("productoEditar"))
                .andExpect(model().attributeExists("listaProveedores"));
    }

    @Test
    @DisplayName("Debe registrar producto exitosamente (flujo completo)")
    void testRegistrarProductoExitoso() throws Exception {
        // Arrange
        long countAntes = productoRepositorio.count();

        // Act
        mockMvc.perform(post("/productos/registrar")
                .with(csrf())
                .with(user("admin@test.com").roles("ADMIN"))
                .param("nombre", "Ron Bacardi")
                .param("precioCompra", "15.50")
                .param("precioVenta", "20.00")
                .param("stock", "10")
                .param("idProveedor", proveedorTest.getIdProveedor().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/productos"))
                .andExpect(flash().attributeExists("mensajeExito"));

        // Assert
        long countDespues = productoRepositorio.count();
        assertEquals(countAntes + 1, countDespues, "Debe haber un producto más en la BD");

        Producto productoGuardado = productoRepositorio.findAll().get(0);
        assertEquals("Ron Bacardi", productoGuardado.getNombre());
        assertEquals(15.50, productoGuardado.getPrecioCompra());
        assertEquals(20.00, productoGuardado.getPrecioVenta());
        assertEquals(10, productoGuardado.getStock());
        assertEquals(proveedorTest.getIdProveedor(), productoGuardado.getProveedor().getIdProveedor());
    }

    @Test
    @DisplayName("Debe rechazar producto con precio de venta menor al de compra")
    void testRegistrarProductoPrecioVentaMenor() throws Exception {
        // Arrange
        long countAntes = productoRepositorio.count();

        // Act
        mockMvc.perform(post("/productos/registrar")
                .with(csrf())
                .with(user("admin@test.com").roles("ADMIN"))
                .param("nombre", "Vodka")
                .param("precioCompra", "30.00")
                .param("precioVenta", "20.00") // Menor al precio de compra
                .param("stock", "5")
                .param("idProveedor", proveedorTest.getIdProveedor().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/productos"))
                .andExpect(flash().attributeExists("mensajeError"));

        // Assert
        long countDespues = productoRepositorio.count();
        assertEquals(countAntes, countDespues, "No debe haberse registrado el producto");
    }

    @Test
    @DisplayName("Debe actualizar producto exitosamente")
    void testActualizarProductoExitoso() throws Exception {
        // Arrange - Crear producto inicial
        Producto productoInicial = new Producto();
        productoInicial.setNombre("Gin Original");
        productoInicial.setPrecioCompra(25.0);
        productoInicial.setPrecioVenta(35.0);
        productoInicial.setStock(8);
        productoInicial.setProveedor(proveedorTest);
        productoInicial = productoRepositorio.save(productoInicial);

        // Act - Actualizar producto
        mockMvc.perform(post("/productos/actualizar")
                .with(csrf())
                .with(user("admin@test.com").roles("ADMIN"))
                .param("idProducto", productoInicial.getIdProducto().toString())
                .param("nombre", "Gin Bombay Actualizado")
                .param("precioCompra", "28.00")
                .param("precioVenta", "40.00")
                .param("stock", "12")
                .param("idProveedorEdit", proveedorTest.getIdProveedor().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/productos"))
                .andExpect(flash().attributeExists("mensajeExito"));

        // Assert
        Producto productoActualizado = productoRepositorio.findById(productoInicial.getIdProducto()).orElseThrow();
        assertEquals("Gin Bombay Actualizado", productoActualizado.getNombre());
        assertEquals(28.00, productoActualizado.getPrecioCompra());
        assertEquals(40.00, productoActualizado.getPrecioVenta());
        assertEquals(12, productoActualizado.getStock());
    }

    @Test
    @DisplayName("Debe eliminar producto exitosamente cuando stock es cero")
    void testEliminarProductoExitoso() throws Exception {
        // Arrange - Crear producto con stock cero
        Producto producto = new Producto();
        producto.setNombre("Producto a Eliminar");
        producto.setPrecioCompra(10.0);
        producto.setPrecioVenta(15.0);
        producto.setStock(0); // Stock cero
        producto.setProveedor(proveedorTest);
        producto = productoRepositorio.save(producto);

        long countAntes = productoRepositorio.count();

        // Act
        mockMvc.perform(post("/productos/eliminar")
                .with(csrf())
                .with(user("admin@test.com").roles("ADMIN"))
                .param("id", producto.getIdProducto().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/productos"))
                .andExpect(flash().attributeExists("mensajeExito"));

        // Assert
        long countDespues = productoRepositorio.count();
        assertEquals(countAntes - 1, countDespues, "Debe haberse eliminado el producto");
        assertFalse(productoRepositorio.findById(producto.getIdProducto()).isPresent());
    }

    @Test
    @DisplayName("Debe rechazar eliminación de producto con stock")
    void testEliminarProductoConStock() throws Exception {
        // Arrange - Crear producto con stock
        Producto producto = new Producto();
        producto.setNombre("Producto con Stock");
        producto.setPrecioCompra(10.0);
        producto.setPrecioVenta(15.0);
        producto.setStock(5); // Stock mayor a cero
        producto.setProveedor(proveedorTest);
        producto = productoRepositorio.save(producto);

        long countAntes = productoRepositorio.count();

        // Act
        mockMvc.perform(post("/productos/eliminar")
                .with(csrf())
                .with(user("admin@test.com").roles("ADMIN"))
                .param("id", producto.getIdProducto().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/productos"))
                .andExpect(flash().attributeExists("mensajeError"));

        // Assert
        long countDespues = productoRepositorio.count();
        assertEquals(countAntes, countDespues, "No debe haberse eliminado el producto");
        assertTrue(productoRepositorio.findById(producto.getIdProducto()).isPresent());
    }
}
