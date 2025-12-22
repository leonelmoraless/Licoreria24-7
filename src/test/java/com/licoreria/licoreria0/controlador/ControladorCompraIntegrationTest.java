package com.licoreria.licoreria0.controlador;

import com.licoreria.licoreria0.modelo.*;
import com.licoreria.licoreria0.repositorio.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.licoreria.licoreria0.config.TestSecurityConfig;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Integración para ControladorCompra (Caja Gris)
 * Verifica el flujo completo de compras y actualización de stock
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@Transactional
@DisplayName("ControladorCompra - Pruebas de Integración (Caja Gris)")
class ControladorCompraIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private CompraRepositorio compraRepositorio;

        @Autowired
        private ProductoRepositorio productoRepositorio;

        @Autowired
        private ProveedorRepositorio proveedorRepositorio;

        @Autowired
        private DetalleCompraRepositorio detalleCompraRepositorio;

        private Proveedor proveedorTest;
        private Producto producto1;
        private Producto producto2;

        @BeforeEach
        void setUp() {
                // Limpiar datos previos
                detalleCompraRepositorio.deleteAll();
                compraRepositorio.deleteAll();
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

                // Crear productos de prueba
                producto1 = new Producto();
                producto1.setNombre("Ron Bacardi");
                producto1.setPrecioCompra(15.0);
                producto1.setPrecioVenta(20.0);
                producto1.setStock(10);
                producto1.setProveedor(proveedorTest);
                producto1 = productoRepositorio.save(producto1);

                producto2 = new Producto();
                producto2.setNombre("Vodka Absolut");
                producto2.setPrecioCompra(25.0);
                producto2.setPrecioVenta(35.0);
                producto2.setStock(5);
                producto2.setProveedor(proveedorTest);
                producto2 = productoRepositorio.save(producto2);
        }

        @Test
        @DisplayName("Debe mostrar página de compras correctamente")
        void testMostrarPaginaCompras() throws Exception {
                mockMvc.perform(get("/compras")
                                .with(user("admin@test.com").roles("ADMIN")))
                                .andExpect(status().isOk())
                                .andExpect(view().name("compras"))
                                .andExpect(model().attributeExists("listaProveedores"))
                                .andExpect(model().attributeExists("listaProductos"));
        }

        @Test
        @DisplayName("Debe mostrar historial de compras correctamente")
        void testMostrarHistorialCompras() throws Exception {
                mockMvc.perform(get("/historial-compras")
                                .with(user("admin@test.com").roles("ADMIN")))
                                .andExpect(status().isOk())
                                .andExpect(view().name("historial-compras"))
                                .andExpect(model().attributeExists("listaCompras"));
        }

        @Test
        @DisplayName("Debe registrar compra exitosamente y actualizar stock")
        void testRegistrarCompraExitoso() throws Exception {
                // Arrange
                int stockInicialProducto1 = producto1.getStock();
                int stockInicialProducto2 = producto2.getStock();

                String jsonPeticion = String.format(
                                "{\"idProveedor\":%d,\"items\":[" +
                                                "{\"idProducto\":%d,\"cantidad\":3,\"precioUnitario\":15.0}," +
                                                "{\"idProducto\":%d,\"cantidad\":2,\"precioUnitario\":25.0}" +
                                                "]}",
                                proveedorTest.getIdProveedor(),
                                producto1.getIdProducto(),
                                producto2.getIdProducto());

                long countComprasAntes = compraRepositorio.count();

                // Act
                mockMvc.perform(post("/api/compras/registrar")
                                .with(csrf())
                                .with(user("admin@test.com").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPeticion))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.idCompra").exists())
                                .andExpect(jsonPath("$.total").value(95.0)); // (3*15) + (2*25) = 45 + 50 = 95

                // Assert
                long countComprasDespues = compraRepositorio.count();
                assertEquals(countComprasAntes + 1, countComprasDespues, "Debe haberse registrado una compra");

                // Verificar actualización de stock
                Producto producto1Actualizado = productoRepositorio.findById(producto1.getIdProducto()).orElseThrow();
                Producto producto2Actualizado = productoRepositorio.findById(producto2.getIdProducto()).orElseThrow();

                assertEquals(stockInicialProducto1 + 3, producto1Actualizado.getStock(),
                                "Stock de producto 1 debe aumentar en 3");
                assertEquals(stockInicialProducto2 + 2, producto2Actualizado.getStock(),
                                "Stock de producto 2 debe aumentar en 2");

                // Verificar detalles de compra
                List<Compra> compras = compraRepositorio.findAll();
                Compra compraGuardada = compras.get(compras.size() - 1);
                assertEquals(2, compraGuardada.getDetalles().size(), "Debe tener 2 detalles");
                assertEquals(95.0, compraGuardada.getTotal(), "Total debe ser 95.0");
        }

        @Test
        @DisplayName("Debe rechazar compra con producto inexistente")
        void testRegistrarCompraProductoInexistente() throws Exception {
                // Arrange
                String jsonPeticion = String.format(
                                "{\"idProveedor\":%d,\"items\":[" +
                                                "{\"idProducto\":999,\"cantidad\":5,\"precioUnitario\":10.0}" +
                                                "]}",
                                proveedorTest.getIdProveedor());

                // Act & Assert
                mockMvc.perform(post("/api/compras/registrar")
                                .with(csrf())
                                .with(user("admin@test.com").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPeticion))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe eliminar compra y revertir stock correctamente")
        void testEliminarCompraYRevertirStock() throws Exception {
                // Arrange - Crear una compra primero
                String jsonPeticion = String.format(
                                "{\"idProveedor\":%d,\"items\":[" +
                                                "{\"idProducto\":%d,\"cantidad\":5,\"precioUnitario\":15.0}" +
                                                "]}",
                                proveedorTest.getIdProveedor(),
                                producto1.getIdProducto());

                mockMvc.perform(post("/api/compras/registrar")
                                .with(csrf())
                                .with(user("admin@test.com").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPeticion))
                                .andExpect(status().isOk());

                // Verificar stock después de compra
                Producto productoAntesEliminar = productoRepositorio.findById(producto1.getIdProducto()).orElseThrow();
                int stockDespuesCompra = productoAntesEliminar.getStock();
                assertEquals(15, stockDespuesCompra, "Stock debe ser 10 + 5 = 15");

                // Obtener ID de la compra
                List<Compra> compras = compraRepositorio.findAll();
                Compra compraCreada = compras.get(compras.size() - 1);
                Long idCompra = compraCreada.getIdCompra();

                long countComprasAntes = compraRepositorio.count();

                // Act - Eliminar compra
                mockMvc.perform(post("/compras/eliminar")
                                .with(csrf())
                                .with(user("admin@test.com").roles("ADMIN"))
                                .param("id", idCompra.toString()))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/historial-compras"));

                // Assert
                long countComprasDespues = compraRepositorio.count();
                assertEquals(countComprasAntes - 1, countComprasDespues, "Debe haberse eliminado la compra");

                // Verificar reversión de stock
                Producto productoDespuesEliminar = productoRepositorio.findById(producto1.getIdProducto())
                                .orElseThrow();
                assertEquals(10, productoDespuesEliminar.getStock(), "Stock debe volver a 10 (15 - 5)");
        }

        @Test
        @DisplayName("Debe calcular total de compra correctamente con múltiples productos")
        void testCalculoTotalCompraMultiplesProductos() throws Exception {
                // Arrange
                String jsonPeticion = String.format(
                                "{\"idProveedor\":%d,\"items\":[" +
                                                "{\"idProducto\":%d,\"cantidad\":10,\"precioUnitario\":15.0}," +
                                                "{\"idProducto\":%d,\"cantidad\":5,\"precioUnitario\":25.0}" +
                                                "]}",
                                proveedorTest.getIdProveedor(),
                                producto1.getIdProducto(),
                                producto2.getIdProducto());

                // Act
                mockMvc.perform(post("/api/compras/registrar")
                                .with(csrf())
                                .with(user("admin@test.com").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPeticion))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.total").value(275.0)); // (10*15) + (5*25) = 150 + 125 = 275
        }
}
