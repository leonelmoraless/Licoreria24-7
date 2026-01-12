package com.licoreria.licoreria0.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas Unitarias de Caja Blanca para Proveedor
 * 
 * Análisis de flujo de datos:
 * - Getters y Setters
 * - Relaciones con productos
 * - Validación de datos
 */
@DisplayName("Proveedor - Pruebas de Caja Blanca")
class ProveedorTest {

    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        proveedor = new Proveedor();
    }

    @Test
    @DisplayName("CP01: Debe crear proveedor vacío correctamente")
    void testCrearProveedorVacio() {
        // Assert
        assertNotNull(proveedor);
        assertNull(proveedor.getIdProveedor());
        assertNull(proveedor.getNombre());
        assertNull(proveedor.getRuc());
    }

    @Test
    @DisplayName("CP02: Debe establecer y obtener ID correctamente")
    void testSetGetIdProveedor() {
        // Act
        proveedor.setIdProveedor(1L);

        // Assert
        assertEquals(1L, proveedor.getIdProveedor());
    }

    @Test
    @DisplayName("CP03: Debe establecer y obtener nombre correctamente")
    void testSetGetNombre() {
        // Act
        proveedor.setNombre("Distribuidora ABC");

        // Assert
        assertEquals("Distribuidora ABC", proveedor.getNombre());
    }

    @Test
    @DisplayName("CP04: Debe establecer y obtener RUC correctamente")
    void testSetGetRuc() {
        // Act
        proveedor.setRuc("1234567890123");

        // Assert
        assertEquals("1234567890123", proveedor.getRuc());
    }

    @Test
    @DisplayName("CP05: Debe establecer y obtener teléfono correctamente")
    void testSetGetTelefono() {
        // Act
        proveedor.setTelefono("0987654321");

        // Assert
        assertEquals("0987654321", proveedor.getTelefono());
    }

    @Test
    @DisplayName("CP06: Debe establecer y obtener dirección correctamente")
    void testSetGetDireccion() {
        // Act
        proveedor.setDireccion("Av. Principal 123");

        // Assert
        assertEquals("Av. Principal 123", proveedor.getDireccion());
    }

    @Test
    @DisplayName("CP07: Debe establecer y obtener correo correctamente")
    void testSetGetCorreo() {
        // Act
        proveedor.setCorreo("contacto@proveedor.com");

        // Assert
        assertEquals("contacto@proveedor.com", proveedor.getCorreo());
    }

    @Test
    @DisplayName("CP08: Debe manejar nombre null")
    void testNombreNull() {
        // Act
        proveedor.setNombre(null);

        // Assert
        assertNull(proveedor.getNombre());
    }

    @Test
    @DisplayName("CP09: Debe manejar RUC vacío")
    void testRucVacio() {
        // Act
        proveedor.setRuc("");

        // Assert
        assertEquals("", proveedor.getRuc());
    }

    @Test
    @DisplayName("CP10: Debe establecer todos los campos correctamente")
    void testSetTodosCampos() {
        // Act
        proveedor.setIdProveedor(1L);
        proveedor.setNombre("Proveedor Test");
        proveedor.setRuc("1234567890123");
        proveedor.setTelefono("0987654321");
        proveedor.setDireccion("Calle Test 456");
        proveedor.setCorreo("test@test.com");

        // Assert
        assertEquals(1L, proveedor.getIdProveedor());
        assertEquals("Proveedor Test", proveedor.getNombre());
        assertEquals("1234567890123", proveedor.getRuc());
        assertEquals("0987654321", proveedor.getTelefono());
        assertEquals("Calle Test 456", proveedor.getDireccion());
        assertEquals("test@test.com", proveedor.getCorreo());
    }
}
