package com.licoreria.licoreria0.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas Unitarias de Caja Blanca para Cliente
 * 
 * Análisis de flujo de datos:
 * - Getters y Setters
 * - Validación de cédula
 * - Relaciones con ventas
 */
@DisplayName("Cliente - Pruebas de Caja Blanca")
class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
    }

    @Test
    @DisplayName("CP01: Debe crear cliente vacío correctamente")
    void testCrearClienteVacio() {
        // Assert
        assertNotNull(cliente);
        assertNull(cliente.getIdCliente());
        assertNull(cliente.getNombre());
        assertNull(cliente.getCedula());
    }

    @Test
    @DisplayName("CP02: Debe establecer y obtener ID correctamente")
    void testSetGetIdCliente() {
        // Act
        cliente.setIdCliente(1L);

        // Assert
        assertEquals(1L, cliente.getIdCliente());
    }

    @Test
    @DisplayName("CP03: Debe establecer y obtener nombre correctamente")
    void testSetGetNombre() {
        // Act
        cliente.setNombre("Juan Pérez");

        // Assert
        assertEquals("Juan Pérez", cliente.getNombre());
    }

    @Test
    @DisplayName("CP04: Debe establecer y obtener cédula correctamente")
    void testSetGetCedula() {
        // Act
        cliente.setCedula("1234567890");

        // Assert
        assertEquals("1234567890", cliente.getCedula());
    }

    @Test
    @DisplayName("CP05: Debe establecer y obtener teléfono correctamente")
    void testSetGetTelefono() {
        // Act
        cliente.setTelefono("0987654321");

        // Assert
        assertEquals("0987654321", cliente.getTelefono());
    }

    @Test
    @DisplayName("CP06: Debe establecer y obtener dirección correctamente")
    void testSetGetDireccion() {
        // Act
        cliente.setDireccion("Av. Cliente 789");

        // Assert
        assertEquals("Av. Cliente 789", cliente.getDireccion());
    }

    @Test
    @DisplayName("CP07: Debe establecer y obtener correo correctamente")
    void testSetGetCorreo() {
        // Act
        cliente.setCorreo("cliente@email.com");

        // Assert
        assertEquals("cliente@email.com", cliente.getCorreo());
    }

    @Test
    @DisplayName("CP08: Debe manejar cédula null")
    void testCedulaNull() {
        // Act
        cliente.setCedula(null);

        // Assert
        assertNull(cliente.getCedula());
    }

    @Test
    @DisplayName("CP09: Debe manejar cédula vacía")
    void testCedulaVacia() {
        // Act
        cliente.setCedula("");

        // Assert
        assertEquals("", cliente.getCedula());
    }

    @Test
    @DisplayName("CP10: Debe establecer todos los campos correctamente")
    void testSetTodosCampos() {
        // Act
        cliente.setIdCliente(1L);
        cliente.setNombre("María González");
        cliente.setCedula("0987654321");
        cliente.setTelefono("0991234567");
        cliente.setDireccion("Calle Principal 123");
        cliente.setCorreo("maria@email.com");

        // Assert
        assertEquals(1L, cliente.getIdCliente());
        assertEquals("María González", cliente.getNombre());
        assertEquals("0987654321", cliente.getCedula());
        assertEquals("0991234567", cliente.getTelefono());
        assertEquals("Calle Principal 123", cliente.getDireccion());
        assertEquals("maria@email.com", cliente.getCorreo());
    }

    @Test
    @DisplayName("CP11: Debe manejar nombre con caracteres especiales")
    void testNombreConCaracteresEspeciales() {
        // Act
        cliente.setNombre("José María Ñ-Pérez");

        // Assert
        assertEquals("José María Ñ-Pérez", cliente.getNombre());
    }
}
