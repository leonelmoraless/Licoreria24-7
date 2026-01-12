package com.licoreria.licoreria0.servicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas Unitarias de Caja Blanca para ServicioEmail
 * 
 * Análisis de flujo de control:
 * - Envío de correo simple
 * - Envío de correo HTML con imagen inline
 * - Manejo de excepciones
 * - Ejecución asíncrona
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ServicioEmail - Pruebas de Caja Blanca")
class ServicioEmailTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private ServicioEmail servicioEmail;

    @Test
    @DisplayName("CP01: Debe enviar correo simple correctamente")
    void testEnviarCorreoSimple() {
        // Arrange
        String destinatario = "test@example.com";
        String asunto = "Asunto de Prueba";
        String texto = "Contenido del correo";

        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        // Act
        assertDoesNotThrow(() -> {
            servicioEmail.enviarCorreo(destinatario, asunto, texto);
        });

        // Assert
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("CP02: Debe manejar excepción al enviar correo simple")
    void testEnviarCorreoSimpleConError() {
        // Arrange
        String destinatario = "test@example.com";
        String asunto = "Asunto";
        String texto = "Texto";

        doThrow(new RuntimeException("Error de conexión"))
                .when(javaMailSender).send(any(SimpleMailMessage.class));

        // Act - No debe lanzar excepción (se captura internamente)
        assertDoesNotThrow(() -> {
            servicioEmail.enviarCorreo(destinatario, asunto, texto);
        });

        // Assert
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("CP03: Debe enviar correo HTML con imagen inline")
    void testEnviarCorreoHtmlConInline() {
        // Arrange
        String destinatario = "test@example.com";
        String asunto = "Correo HTML";
        String html = "<html><body><h1>Test</h1><img src='cid:logo'/></body></html>";
        String contentId = "logo";
        String classpathImagen = "static/imagenes/test.jpg";
        String contentType = "image/jpeg";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        // Act
        assertDoesNotThrow(() -> {
            servicioEmail.enviarCorreoHtmlConInline(
                    destinatario, asunto, html, contentId, classpathImagen, contentType);
        });

        // Assert
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("CP04: Debe manejar excepción al enviar correo HTML")
    void testEnviarCorreoHtmlConError() {
        // Arrange
        String destinatario = "test@example.com";
        String asunto = "Correo HTML";
        String html = "<html><body>Test</body></html>";

        when(javaMailSender.createMimeMessage()).thenThrow(new RuntimeException("Error al crear mensaje"));

        // Act - No debe lanzar excepción
        assertDoesNotThrow(() -> {
            servicioEmail.enviarCorreoHtmlConInline(
                    destinatario, asunto, html, "id", "path", "type");
        });

        // Assert
        verify(javaMailSender, times(1)).createMimeMessage();
    }

    @Test
    @DisplayName("CP05: Debe enviar correo con destinatario válido")
    void testEnviarCorreoDestinatarioValido() {
        // Arrange
        String destinatario = "usuario@dominio.com.ec";
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        // Act
        assertDoesNotThrow(() -> {
            servicioEmail.enviarCorreo(destinatario, "Asunto", "Texto");
        });

        // Assert
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("CP06: Debe enviar correo con asunto vacío")
    void testEnviarCorreoAsuntoVacio() {
        // Arrange
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        // Act
        assertDoesNotThrow(() -> {
            servicioEmail.enviarCorreo("test@test.com", "", "Texto");
        });

        // Assert
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("CP07: Debe enviar correo con texto vacío")
    void testEnviarCorreoTextoVacio() {
        // Arrange
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        // Act
        assertDoesNotThrow(() -> {
            servicioEmail.enviarCorreo("test@test.com", "Asunto", "");
        });

        // Assert
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("CP08: Debe enviar correo HTML con contenido complejo")
    void testEnviarCorreoHtmlComplejo() {
        // Arrange
        String htmlComplejo = """
                <html>
                    <head><style>body{font-family: Arial;}</style></head>
                    <body>
                        <h1>Título</h1>
                        <p>Párrafo con <strong>negrita</strong> y <em>cursiva</em></p>
                        <img src='cid:imagen' alt='Logo'/>
                    </body>
                </html>
                """;

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        // Act
        assertDoesNotThrow(() -> {
            servicioEmail.enviarCorreoHtmlConInline(
                    "test@test.com", "HTML Complejo", htmlComplejo,
                    "imagen", "path/to/image.png", "image/png");
        });

        // Assert
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }
}
