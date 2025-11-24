/*
 * es el servicio para enviar correos electrónicos usando Spring Mail
 */
package com.licoreria.licoreria0.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.*;
import org.springframework.core.io.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
public class ServicioEmail {

    @Autowired
    private JavaMailSender javaMailSender; 

    @Async
    public void enviarCorreo(String destinatario, String asunto, String texto) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(destinatario);
            mensaje.setSubject(asunto);
            mensaje.setText(texto);

            javaMailSender.send(mensaje);
            System.out.println("Correo enviado a: " + destinatario);
        } catch (Exception e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
        }
    }

    /*
     * envia el correo con un mensaje y una imagen 
    Se lo hace por medio de HTML  
     */
    @Async
    public void enviarCorreoHtmlConInline(String destinatario, String asunto, String html,
            String contentId, String classpathImagen, String contentType) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(html, true); 

            // Adjuntar imagen si existe
            Resource recurso = new ClassPathResource(classpathImagen);
            if (recurso.exists()) {
                helper.addInline(contentId, recurso, contentType);
            }

            javaMailSender.send(mimeMessage);
            System.out.println("Correo HTML enviado a: " + destinatario);
        } catch (Exception e) {
            System.err.println("Error al enviar correo HTML: " + e.getMessage());
        }
    }
}