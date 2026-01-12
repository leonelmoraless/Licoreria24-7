package com.licoreria.licoreria0.patrones.facade;

import com.licoreria.licoreria0.modelo.*;
import com.licoreria.licoreria0.repositorio.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas Unitarias de Caja Blanca para Facade - Métodos de Proveedores
 * 
 * Técnicas aplicadas:
 * - Cobertura de sentencias: Todas las líneas ejecutables
 * - Cobertura de ramas: Todos los if/else
 * - Cobertura de caminos: Combinaciones de condiciones
 * - Análisis de flujo de datos: Validación de variables
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Facade Proveedores - Pruebas de Caja Blanca")
class FacadeProveedorTest {

    @Mock
    private ProveedorRepositorio proveedorRepositorio;

    @Mock
    private ProductoRepositorio productoRepositorio;

    @Mock
    private CompraRepositorio compraRepositorio;

    @InjectMocks
    private Facade facade;

    private Proveedor proveedorMock;

    @BeforeEach
    void setUp() {
        proveedorMock = new Proveedor();
        proveedorMock.setIdProveedor(1L);
        proveedorMock.setNombre("Distribuidora Test");
        proveedorMock.setRuc("1234567890123");
        proveedorMock.setTelefono("0987654321");
        proveedorMock.setDireccion("Av. Test 123");
        proveedorMock.setCorreo("test@proveedor.com");
    }

    // ==================== PRUEBAS DE REGISTRAR PROVEEDOR ====================

    @Test
    @DisplayName("CP01: Debe registrar proveedor exitosamente con RUC válido")
    void testRegistrarProveedorExitoso() throws Exception {
        // Arrange - Camino: RUC != null, !isEmpty(), no duplicado
        Proveedor nuevoProveedor = new Proveedor();
        nuevoProveedor.setNombre("Nuevo Proveedor");
        nuevoProveedor.setRuc("9876543210987");

        when(proveedorRepositorio.findByRuc("9876543210987")).thenReturn(Optional.empty());
        when(proveedorRepositorio.save(any(Proveedor.class))).thenReturn(proveedorMock);

        // Act
        Proveedor resultado = facade.registrarProveedor(nuevoProveedor);

        // Assert
        assertNotNull(resultado);
        verify(proveedorRepositorio, times(1)).findByRuc("9876543210987");
        verify(proveedorRepositorio, times(1)).save(nuevoProveedor);
    }

    @Test
    @DisplayName("CP02: Debe lanzar excepción cuando RUC está duplicado")
    void testRegistrarProveedorRucDuplicado() {
        // Arrange - Camino: RUC != null, !isEmpty(), duplicado encontrado
        Proveedor nuevoProveedor = new Proveedor();
        nuevoProveedor.setRuc("1234567890123");

        when(proveedorRepositorio.findByRuc("1234567890123")).thenReturn(Optional.of(proveedorMock));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            facade.registrarProveedor(nuevoProveedor);
        });

        assertTrue(exception.getMessage().contains("ya está registrado"));
        verify(proveedorRepositorio, never()).save(any(Proveedor.class));
    }

    @Test
    @DisplayName("CP03: Debe registrar proveedor cuando RUC es null")
    void testRegistrarProveedorRucNull() throws Exception {
        // Arrange - Camino: RUC == null
        Proveedor nuevoProveedor = new Proveedor();
        nuevoProveedor.setNombre("Proveedor Sin RUC");
        nuevoProveedor.setRuc(null);

        when(proveedorRepositorio.save(any(Proveedor.class))).thenReturn(proveedorMock);

        // Act
        Proveedor resultado = facade.registrarProveedor(nuevoProveedor);

        // Assert
        assertNotNull(resultado);
        verify(proveedorRepositorio, never()).findByRuc(anyString());
        verify(proveedorRepositorio, times(1)).save(nuevoProveedor);
    }

    @Test
    @DisplayName("CP04: Debe registrar proveedor cuando RUC está vacío")
    void testRegistrarProveedorRucVacio() throws Exception {
        // Arrange - Camino: RUC != null, isEmpty() == true
        Proveedor nuevoProveedor = new Proveedor();
        nuevoProveedor.setNombre("Proveedor RUC Vacío");
        nuevoProveedor.setRuc("");

        when(proveedorRepositorio.save(any(Proveedor.class))).thenReturn(proveedorMock);

        // Act
        Proveedor resultado = facade.registrarProveedor(nuevoProveedor);

        // Assert
        assertNotNull(resultado);
        verify(proveedorRepositorio, never()).findByRuc(anyString());
        verify(proveedorRepositorio, times(1)).save(nuevoProveedor);
    }

    // ==================== PRUEBAS DE ACTUALIZAR PROVEEDOR ====================

    @Test
    @DisplayName("CP05: Debe actualizar proveedor exitosamente sin cambiar RUC")
    void testActualizarProveedorSinCambiarRuc() throws Exception {
        // Arrange - Camino: RUC nuevo == RUC original
        Proveedor proveedorActualizado = new Proveedor();
        proveedorActualizado.setIdProveedor(1L);
        proveedorActualizado.setNombre("Nombre Actualizado");
        proveedorActualizado.setRuc("1234567890123"); // Mismo RUC

        when(proveedorRepositorio.findById(1L)).thenReturn(Optional.of(proveedorMock));
        when(proveedorRepositorio.save(any(Proveedor.class))).thenReturn(proveedorMock);

        // Act
        Proveedor resultado = facade.actualizarProveedor(proveedorActualizado);

        // Assert
        assertNotNull(resultado);
        verify(proveedorRepositorio, times(1)).findById(1L);
        verify(proveedorRepositorio, never()).findByRuc(anyString());
        verify(proveedorRepositorio, times(1)).save(proveedorMock);
    }

    @Test
    @DisplayName("CP06: Debe actualizar proveedor cambiando RUC a uno no usado")
    void testActualizarProveedorCambiandoRuc() throws Exception {
        // Arrange - Camino: RUC nuevo != RUC original, no duplicado
        Proveedor proveedorActualizado = new Proveedor();
        proveedorActualizado.setIdProveedor(1L);
        proveedorActualizado.setNombre("Nombre Actualizado");
        proveedorActualizado.setRuc("9999999999999"); // Nuevo RUC

        when(proveedorRepositorio.findById(1L)).thenReturn(Optional.of(proveedorMock));
        when(proveedorRepositorio.findByRuc("9999999999999")).thenReturn(Optional.empty());
        when(proveedorRepositorio.save(any(Proveedor.class))).thenReturn(proveedorMock);

        // Act
        Proveedor resultado = facade.actualizarProveedor(proveedorActualizado);

        // Assert
        assertNotNull(resultado);
        verify(proveedorRepositorio, times(1)).findByRuc("9999999999999");
        verify(proveedorRepositorio, times(1)).save(proveedorMock);
    }

    @Test
    @DisplayName("CP07: Debe lanzar excepción cuando nuevo RUC está en uso")
    void testActualizarProveedorRucEnUso() {
        // Arrange - Camino: RUC nuevo != RUC original, duplicado encontrado
        Proveedor proveedorActualizado = new Proveedor();
        proveedorActualizado.setIdProveedor(1L);
        proveedorActualizado.setRuc("9999999999999");

        Proveedor otroProveedor = new Proveedor();
        otroProveedor.setIdProveedor(2L);
        otroProveedor.setRuc("9999999999999");

        when(proveedorRepositorio.findById(1L)).thenReturn(Optional.of(proveedorMock));
        when(proveedorRepositorio.findByRuc("9999999999999")).thenReturn(Optional.of(otroProveedor));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            facade.actualizarProveedor(proveedorActualizado);
        });

        assertTrue(exception.getMessage().contains("ya está en uso"));
        verify(proveedorRepositorio, never()).save(any(Proveedor.class));
    }

    @Test
    @DisplayName("CP08: Debe lanzar excepción cuando proveedor no existe")
    void testActualizarProveedorNoExiste() {
        // Arrange
        Proveedor proveedorActualizado = new Proveedor();
        proveedorActualizado.setIdProveedor(999L);

        when(proveedorRepositorio.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            facade.actualizarProveedor(proveedorActualizado);
        });

        assertTrue(exception.getMessage().contains("no encontrado"));
        verify(proveedorRepositorio, never()).save(any(Proveedor.class));
    }

    @Test
    @DisplayName("CP09: Debe actualizar proveedor con RUC null")
    void testActualizarProveedorRucNull() throws Exception {
        // Arrange - Camino: nuevoRuc == null
        Proveedor proveedorActualizado = new Proveedor();
        proveedorActualizado.setIdProveedor(1L);
        proveedorActualizado.setNombre("Actualizado");
        proveedorActualizado.setRuc(null);

        when(proveedorRepositorio.findById(1L)).thenReturn(Optional.of(proveedorMock));
        when(proveedorRepositorio.save(any(Proveedor.class))).thenReturn(proveedorMock);

        // Act
        Proveedor resultado = facade.actualizarProveedor(proveedorActualizado);

        // Assert
        assertNotNull(resultado);
        verify(proveedorRepositorio, never()).findByRuc(anyString());
        verify(proveedorRepositorio, times(1)).save(proveedorMock);
    }

    // ==================== PRUEBAS DE ELIMINAR PROVEEDOR ====================

    @Test
    @DisplayName("CP10: Debe eliminar proveedor sin productos ni compras")
    void testEliminarProveedorExitoso() throws Exception {
        // Arrange - Camino: sin productos, sin compras
        proveedorMock.setProductos(new ArrayList<>());

        when(proveedorRepositorio.findById(1L)).thenReturn(Optional.of(proveedorMock));
        when(compraRepositorio.existsByProveedor_IdProveedor(1L)).thenReturn(false);

        // Act
        facade.eliminarProveedor(1L);

        // Assert
        verify(proveedorRepositorio, times(1)).findById(1L);
        verify(compraRepositorio, times(1)).existsByProveedor_IdProveedor(1L);
        verify(proveedorRepositorio, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("CP11: Debe lanzar excepción cuando proveedor tiene productos")
    void testEliminarProveedorConProductos() {
        // Arrange - Camino: con productos
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto());
        productos.add(new Producto());
        proveedorMock.setProductos(productos);

        when(proveedorRepositorio.findById(1L)).thenReturn(Optional.of(proveedorMock));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            facade.eliminarProveedor(1L);
        });

        assertTrue(exception.getMessage().contains("2 productos asociados"));
        verify(proveedorRepositorio, never()).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("CP12: Debe lanzar excepción cuando proveedor tiene compras")
    void testEliminarProveedorConCompras() {
        // Arrange - Camino: sin productos, con compras
        proveedorMock.setProductos(new ArrayList<>());

        when(proveedorRepositorio.findById(1L)).thenReturn(Optional.of(proveedorMock));
        when(compraRepositorio.existsByProveedor_IdProveedor(1L)).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            facade.eliminarProveedor(1L);
        });

        assertTrue(exception.getMessage().contains("compras registradas"));
        verify(proveedorRepositorio, never()).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("CP13: Debe lanzar excepción cuando proveedor no existe")
    void testEliminarProveedorNoExiste() {
        // Arrange
        when(proveedorRepositorio.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            facade.eliminarProveedor(999L);
        });

        assertTrue(exception.getMessage().contains("no encontrado"));
        verify(proveedorRepositorio, never()).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("CP14: Debe eliminar proveedor cuando lista de productos es null")
    void testEliminarProveedorProductosNull() throws Exception {
        // Arrange - Camino: productos == null
        proveedorMock.setProductos(null);

        when(proveedorRepositorio.findById(1L)).thenReturn(Optional.of(proveedorMock));
        when(compraRepositorio.existsByProveedor_IdProveedor(1L)).thenReturn(false);

        // Act
        facade.eliminarProveedor(1L);

        // Assert
        verify(proveedorRepositorio, times(1)).deleteById(1L);
    }

    // ==================== PRUEBAS DE OBTENER TODOS PROVEEDORES
    // ====================

    @Test
    @DisplayName("CP15: Debe retornar lista de proveedores")
    void testObtenerTodosProveedores() {
        // Arrange
        List<Proveedor> proveedores = new ArrayList<>();
        proveedores.add(proveedorMock);
        proveedores.add(new Proveedor());

        when(proveedorRepositorio.findAll()).thenReturn(proveedores);

        // Act
        List<Proveedor> resultado = facade.obtenerTodosProveedores();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(proveedorRepositorio, times(1)).findAll();
    }

    @Test
    @DisplayName("CP16: Debe retornar lista vacía cuando no hay proveedores")
    void testObtenerTodosProveedoresVacio() {
        // Arrange
        when(proveedorRepositorio.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Proveedor> resultado = facade.obtenerTodosProveedores();

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        verify(proveedorRepositorio, times(1)).findAll();
    }
}
